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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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

        binding.etFullName.setupRequireFieldListener(
            container = binding.fullNameContainer,
            onTextChange = { name -> viewModel.setFullNameInput(name) }
        )
        binding.etEmail.setupRequireFieldListener(
            container = binding.emailContainer,
            onTextChange = { email -> viewModel.setEmailInput(email) }
        )
        binding.etConfirmPassword.setupRequireFieldListener(
            container = binding.confirmPasswordContainer,
            onTextChange = { confPassword -> viewModel.setConfirmPasswordInput(confPassword) }
        )

        setupPasswordListeners()

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

        binding.fullNameContainer.updateContainerHelperText(
            R.string.please_enter_real_names,
            state.isValidFullName
        )
        binding.emailContainer.updateContainerHelperText(
            R.string.invalid_email,
            state.isValidEmail
        )
        binding.confirmPasswordContainer.updateContainerHelperText(
            R.string.confirm_password_error,
            state.isValidConfirmPassword
        )
        updatePasswordValidationState(state.isValidPassword)

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

    private fun getColourFromRes(condition: Boolean): Int {
        return ContextCompat.getColor(
            requireContext(),
            if (condition) R.color.md_theme_light_primary else R.color.md_theme_light_error
        )
    }

    private fun TextInputEditText.setupRequireFieldListener(
        container: TextInputLayout,
        onTextChange: (String) -> Unit
    ) {
        this.doAfterTextChanged { editable -> onTextChange(editable.toString()) }
        this.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && text.isNullOrEmpty()) container.helperText = null

            if (!hasFocus && text.isNullOrEmpty()) container.helperText =
                getString(R.string.required)
        }
    }

    private fun TextInputLayout.updateContainerHelperText(
        @StringRes helperText: Int,
        isValid: Boolean,
    ) {
        if (this.editText?.hasFocus() == true && !isValid && this.editText?.text!!.isNotEmpty()) {
            this.helperText = getString(helperText)
        } else this.helperText = null
    }

}