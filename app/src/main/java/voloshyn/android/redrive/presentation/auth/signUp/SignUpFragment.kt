package voloshyn.android.redrive.presentation.auth.signUp

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentSignUpBinding
import voloshyn.android.domain.models.auth.UserCredentials
import voloshyn.android.domain.models.auth.SignUpStatus
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val binding by viewBinding<FragmentSignUpBinding>()
    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spannableString = SpannableString(binding.bttSignIn.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.bttSignIn.text = spannableString

        isValidFullName()
        isValidEmail()
        isValidPassword()
        isValidConfirmPassword()
        signUp()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                binding.progressBar.visibility = if (it.loading) View.VISIBLE else View.GONE
                when (it.signUpStatus) {
                    is SignUpStatus.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            getString(it.errorMessage),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    SignUpStatus.InProgress -> {}

                    SignUpStatus.SignUp -> {
                        findNavController().navigate(R.id.action_signUpFragment_to_newVehicleFragment)
                    }

                    SignUpStatus.SignOut -> {}
                }
            }
        }
        binding.bttSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun isValidFullName() {
        with(binding) {
            etFullName.doOnTextChanged { text, start, before, count ->
                val validateState = viewModel.isValidFullName(text.toString())
                fullNameContainer.helperText = getString(validateState.message)
            }
            etFullName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && etFullName.text.isNullOrEmpty()) {
                    fullNameContainer.isHelperTextEnabled = true
                    fullNameContainer.helperText = getString(R.string.required)
                } else {
                    fullNameContainer.isHelperTextEnabled =
                        !fullNameContainer.helperText.isNullOrEmpty()
                }
            }
        }
    }


    private fun isValidEmail() {
        with(binding) {
            etEmail.doOnTextChanged { text, start, before, count ->
                val validateState = viewModel.isValidEmail(text.toString())
                emailContainer.helperText = getString(validateState.message)
            }
            etEmail.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && binding.etEmail.text.isNullOrEmpty()) {
                    emailContainer.isHelperTextEnabled = true
                    emailContainer.helperText = getString(R.string.required)
                } else {
                    emailContainer.isHelperTextEnabled =
                        !emailContainer.helperText.isNullOrEmpty()
                }
            }
        }
    }

    private fun isValidPassword() {
        with(binding) {
            etPassword.doOnTextChanged { text, start, before, count ->
                val validateState = viewModel.isValidPassword(text.toString())
                passwordContainer.helperText = getString(validateState.message)
            }
            etPassword.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && etPassword.text.isNullOrEmpty()) {
                    passwordContainer.isHelperTextEnabled = true
                    passwordContainer.helperText = getString(R.string.required)
                } else {
                    passwordContainer.isHelperTextEnabled =
                        !passwordContainer.helperText.isNullOrEmpty()
                }
            }
        }

    }

    private fun isValidConfirmPassword() {
        with(binding) {
            etConfirmPassword.doOnTextChanged { text, start, before, count ->
                val validateState = viewModel.isValidConfirmPassword(
                    etPassword.text.toString(),
                    text.toString()
                )
                confirmPasswordContainer.helperText = getString(validateState.message)
            }
            etConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && etConfirmPassword.text.isNullOrEmpty()) {
                    confirmPasswordContainer.isHelperTextEnabled = true
                    confirmPasswordContainer.helperText = getString(R.string.required)
                } else {
                    confirmPasswordContainer.isHelperTextEnabled =
                        !confirmPasswordContainer.helperText.isNullOrEmpty()
                }
            }
        }
    }

    private fun signUp() {
        binding.bttSignUp.setOnClickListener {
            val user = UserCredentials(
                fullName = binding.etFullName.text.toString(),
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
            lifecycleScope.launch {
                viewModel.signUp(user)
            }
        }
    }

}