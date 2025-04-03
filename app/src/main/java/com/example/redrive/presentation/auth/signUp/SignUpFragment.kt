package com.example.redrive.presentation.auth.signUp

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.useCase.signUpFieldValidation.PasswordValidationResult
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.databinding.FragmentSignUpBinding
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
import com.example.redrive.core.navigate
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.presentation.auth.signIn.SignInFragmentDirections
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
        setupSpannableText()
        setupListeners()
        collectState()
    }

    private fun setupListeners() {
        hideSoftInputAndClearViewsFocus(binding.root)

        binding.etFullName.setupRequireFieldListener(
            container = binding.fullNameContainer,
            onTextChange = { name -> viewModel.onFullNameTextChange(name) }
        )
        binding.etEmail.setupRequireFieldListener(
            container = binding.emailContainer,
            onTextChange = { email -> viewModel.onEmailTextChange(email) }
        )
        binding.etConfirmPassword.setupRequireFieldListener(
            container = binding.confirmPasswordContainer,
            onTextChange = { confPassword -> viewModel.onConfirmPasswordTextChange(confPassword) }
        )
        binding.bttSignUp.setOnClickListener {
            viewModel.onSignUpBttClick()
        }

        binding.bttSignIn.setOnClickListener {
            viewModel.navigate(Router.SignUpDirections.ToSignIn)
        }

        setupPasswordListeners()

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.state.collectLatest {
                    updateUi(it)
                }
            }

            launch {
                viewModel.error.collectLatest {
                    if (it.first) {
                        showErrorAndResetState(errorMessage = it.second) {
                            viewModel.onErrorShown()
                        }
                    }

                }
            }
            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        Router.SignUpDirections.ToSignIn -> {
                            findNavController().popBackStack()

                        }

                        Router.SignUpDirections.ToProfile -> {
                            navigate(SignInFragmentDirections.actionGlobalProfileFragment())
                        }
                    }
                }
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

    }

    private fun setupPasswordListeners() {
        with(binding.etPassword) {
            doAfterTextChanged { editable ->
                viewModel.onPasswordTextChange(editable.toString())
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

    private fun setupSpannableText() {
        val spannableString = SpannableString(binding.bttSignIn.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.bttSignIn.text = spannableString
    }
}