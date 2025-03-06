package com.example.redrive.presentation.auth.signUp

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.useCase.PasswordValidationResult
import com.example.redrive.R
import com.example.redrive.databinding.FragmentSignUpBinding
import com.example.redrive.hideSoftInputAndClearViewsFocus
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding<FragmentSignUpBinding>()
    private val viewModel: SignUpViewModel by viewModels()
    private val passwordInfoView by lazy {
        arrayOf(
            binding.tvPasswordLength,
            binding.tvPasswordNumber,
            binding.tvPasswordOneSymbol,
            binding.tvPasswordUpperLowerCase
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spannableString = SpannableString(binding.bttSignIn.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.bttSignIn.text = spannableString

        setupListeners()
        observeState()

    }

    private fun setupListeners() {
        hideSoftInputAndClearViewsFocus(binding.root)

        setupFullNameEditTextListeners()
        setupEmailEditTextListeners()
        setupPasswordListeners()
        setupConfirmPasswordEditTextListeners()

        binding.bttSignUp.setOnClickListener {
            viewModel.signUp()
        }

        binding.bttSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                updateUi(it)
            }
        }
    }

    private fun updateUi(state: FragmentSignUpState) {
        binding.progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE
        binding.bttSignUp.isEnabled = state.signUpButtonState == SignUpButtonState.Enabled

        updateFullNameContainerHelperText(state.isValidFullName)
        updateEmailContainerHelperText(state.isValidEmail)
        updatePasswordValidationState(state.isValidPassword)
        updateConfirmPasswordContainerHelperText(state.isValidConfirmPassword)

        when (state.signUpStatus) {
            SignUpStatus.Failure -> {
                showError(state.signUpErrorMessage)
                viewModel.resetState()
            }

            SignUpStatus.SignOut -> return
            SignUpStatus.SignIn -> findNavController().navigate(R.id.action_global_tabsFragment)
        }
    }

    private fun showError(@StringRes error: Int) {
        Toast.makeText(
            requireContext(),
            getString(error),
            Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun setupFullNameEditTextListeners() {
        with(binding.etFullName) {
            doAfterTextChanged { editable ->
                viewModel.setFullNameInput(editable.toString())
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus && text.isNullOrEmpty()) binding.fullNameContainer.helperText = null
                if (!hasFocus && text.isNullOrEmpty()) binding.fullNameContainer.helperText =
                    getString(R.string.required)
            }
        }
    }

    private fun setupEmailEditTextListeners() {
        with(binding.etEmail) {
            doAfterTextChanged { editable ->
                viewModel.setEmailInput(editable.toString())
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus && text.isNullOrEmpty()) binding.emailContainer.helperText = null
                if (!hasFocus && text.isNullOrEmpty()) binding.emailContainer.helperText =
                    getString(R.string.required)
            }
        }
    }

    private fun setupPasswordListeners() {
        with(binding.etPassword) {
            doAfterTextChanged { editable ->
                viewModel.setPasswordInput(editable.toString())
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    passwordInfoView.forEach {
                        it.visibility = View.VISIBLE
                    }
                    binding.passwordContainer.helperText = null
                } else {
                    passwordInfoView.forEach {
                        it.visibility = View.GONE
                    }
                    if (text!!.isEmpty()) {
                        binding.passwordContainer.helperText = getString(R.string.required)
                    } else if (!viewModel.state.value.isValidPassword.isValid) {
                        binding.passwordContainer.helperText = getString(R.string.password_error)
                    }
                }
            }
        }
    }

    private fun setupConfirmPasswordEditTextListeners() {
        with(binding.etConfirmPassword) {
            doAfterTextChanged { editable ->
                viewModel.setConfirmPasswordInput(editable.toString())
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus && text.isNullOrEmpty()) binding.confirmPasswordContainer.helperText =
                    null
                if (!hasFocus && text.isNullOrEmpty()) binding.confirmPasswordContainer.helperText =
                    getString(R.string.required)
            }
        }
    }

    private fun updateFullNameContainerHelperText(isValidName: Boolean) {
        if (binding.etFullName.hasFocus() && !isValidName && binding.etFullName.text!!.isNotEmpty()) {
            binding.fullNameContainer.helperText = getString(R.string.please_enter_real_names)
        } else binding.fullNameContainer.helperText = null

    }

    private fun updateEmailContainerHelperText(isValidEmail: Boolean) {
        if (binding.etEmail.hasFocus() && !isValidEmail && binding.etEmail.text!!.isNotEmpty()) {
            binding.emailContainer.helperText = getString(R.string.invalid_email)
        } else binding.emailContainer.helperText = null

    }

    private fun updatePasswordValidationState(result: PasswordValidationResult) {
        with(binding) {
            tvPasswordNumber.setTextColor(getColourFromRes(result.hasDigit))
            tvPasswordUpperLowerCase.setTextColor(
                getColourFromRes(result.hasUpperCase && result.hasLowerCase)
            )
            tvPasswordLength.setTextColor(getColourFromRes(result.hasValidLength))
            tvPasswordOneSymbol.setTextColor(getColourFromRes(result.hasSpecialChar))
        }
    }

    private fun updateConfirmPasswordContainerHelperText(isValidConfirmPassword: Boolean) {
        if (binding.etConfirmPassword.hasFocus() && !isValidConfirmPassword && binding.etConfirmPassword.text!!.isNotEmpty()) {
            binding.confirmPasswordContainer.helperText = getString(R.string.confirm_password_error)
        } else binding.confirmPasswordContainer.helperText = null

    }

    private fun getColourFromRes(condition: Boolean): Int {
        return ContextCompat.getColor(
            requireContext(),
            if (condition) R.color.md_theme_light_primary else R.color.md_theme_light_error
        )
    }

}