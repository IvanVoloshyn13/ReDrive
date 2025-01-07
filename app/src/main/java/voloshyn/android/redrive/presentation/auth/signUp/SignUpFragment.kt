package voloshyn.android.redrive.presentation.auth.signUp

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentSignUpBinding
import voloshyn.android.domain.models.auth.SignUpStatus
import voloshyn.android.redrive.utils.viewBinding

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

        setupFullNameListeners()
        setupEmailListeners()
        setupPasswordListeners()
        setupConfirmPasswordListeners()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                binding.progressBar.visibility = if (it.loading) View.VISIBLE else View.GONE
                when (it.signUpStatus) {
                    is SignUpStatus.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            getString(it.signUpErrorMessage),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        viewModel.resetState()
                    }

                    SignUpStatus.SignUp -> {
                        findNavController().popBackStack(R.id.signUpFragment, true)
                        findNavController().navigate(R.id.action_global_newVehicleFragment)
                    }

                    SignUpStatus.SignOut -> {
                        updateFullNameValidationMessage(it.fullNameInput)
                        updateEmailValidationMessage(it.emailInput)
                        updatePasswordValidationState(it.passwordInput)
                        updateConfirmPasswordValidationMessage(it.confirmPasswordInput)
                    }
                }
            }
        }
        binding.bttSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.bttSignUp.setOnClickListener {
                viewModel.signUp()
        }
    }

    private fun setupFullNameListeners() {
        with(binding.etFullName) {
            doAfterTextChanged { editable ->
                viewModel.setFullNameInput(editable.toString())
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (!text.isNullOrEmpty()) return@setOnFocusChangeListener
                if (hasFocus) {
                    if (text.isNullOrEmpty()) {
                        binding.fullNameContainer.helperText = null
                    }
                } else {
                    if (viewModel.state.value.fullNameInput.value.isEmpty()) {
                        binding.fullNameContainer.helperText = getString(R.string.required)
                    }
                }
            }
        }
    }

    private fun updateFullNameValidationMessage(state: FieldInputState) {
        if (state.isValid == null) return
        binding.fullNameContainer.helperText =
            if (state.validationMessage == null) null else getString(state.validationMessage)
    }

    private fun setupEmailListeners() {
        with(binding.etEmail) {
            doAfterTextChanged { editable ->
                viewModel.setEmailInput(editable.toString())
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (!text.isNullOrEmpty()) return@setOnFocusChangeListener
                if (hasFocus) {
                    if (text.isNullOrEmpty()) {
                        binding.emailContainer.helperText = null
                    }
                } else {
                    if (viewModel.state.value.emailInput.value.isEmpty()) {
                        binding.emailContainer.helperText = getString(R.string.required)
                    }
                }
            }
        }
    }

    private fun updateEmailValidationMessage(state: FieldInputState) {
        if (state.isValid == null) return
        binding.emailContainer.helperText =
            if (state.validationMessage == null) null else getString(state.validationMessage)
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
                    if (viewModel.state.value.passwordInput.password.isEmpty()) {
                        binding.passwordContainer.helperText = getString(R.string.required)
                    } else if (!viewModel.state.value.passwordInput.isValid) {
                        binding.passwordContainer.helperText = getString(R.string.password_error)
                    }
                }
            }
        }
    }

    private fun updatePasswordValidationState(state: PasswordInputState) {
        with(binding) {
            tvPasswordNumber.setTextColor(getColourFromRes(state.hasDigit))
            tvPasswordUpperLowerCase.setTextColor(
                getColourFromRes(state.hasUpperCase && state.hasLowerCase)
            )
            tvPasswordLength.setTextColor(getColourFromRes(state.hasValidLength))
            tvPasswordOneSymbol.setTextColor(getColourFromRes(state.hasSpecialChar))
        }
    }

    private fun setupConfirmPasswordListeners() {
        with(binding.etConfirmPassword) {
            doAfterTextChanged { editable ->
                viewModel.setConfirmPasswordInput(editable.toString())
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (!text.isNullOrEmpty()) return@setOnFocusChangeListener
                if (hasFocus) {
                    if (text.isNullOrEmpty()) {
                        binding.confirmPasswordContainer.helperText = null
                    }
                } else {
                    if (viewModel.state.value.confirmPasswordInput.value.isEmpty()) {
                        binding.confirmPasswordContainer.helperText = getString(R.string.required)
                    }
                }
            }
        }
    }

    private fun updateConfirmPasswordValidationMessage(state: FieldInputState) {
        if (state.isValid == null) return
        binding.confirmPasswordContainer.helperText =
            if (state.validationMessage == null) null else getString(state.validationMessage)
    }

    private fun getColourFromRes(condition: Boolean): Int {
        return ContextCompat.getColor(
            requireContext(),
            if (condition) R.color.md_theme_tertiary else R.color.white
        )
    }

}