package voloshyn.android.redrive.presentation.profile

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.core.view.children
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
import voloshyn.android.domain.models.User
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
                binding.bttSignUp.isEnabled = it.validationState
                binding.progressBar.visibility = if (it.loading) View.VISIBLE else View.GONE
                if (it.isSignUp) {
                    findNavController().navigate(R.id.action_signUpFragment_to_tabsFragment)
                } else if (it.isError) {
                    Toast.makeText(requireContext(), getString(it.errorMessage), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun isValidFullName() {
        binding.etFullName.doOnTextChanged { text, start, before, count ->
            val validateState = viewModel.isValidFullName(text.toString())
            binding.fullNameContainer.helperText = getString(validateState.message)
        }
    }


    private fun isValidEmail() {
        binding.etEmail.doOnTextChanged { text, start, before, count ->
            val validateState = viewModel.isValidEmail(text.toString())
            binding.emailContainer.helperText = getString(validateState.message)
        }
    }

    private fun isValidPassword() {
        binding.etPassword.doOnTextChanged { text, start, before, count ->
            val validateState = viewModel.isValidPassword(text.toString())
            binding.passwordContainer.helperText = getString(validateState.message)
        }
    }

    private fun isValidConfirmPassword() {
        binding.etConfirmPassword.doOnTextChanged { text, start, before, count ->
            val validateState = viewModel.isValidConfirmPassword(
                binding.etPassword.text.toString(),
                text.toString()
            )
            binding.confirmPasswordContainer.helperText = getString(validateState.message)
        }
    }

    private fun signUp() {
        binding.bttSignUp.setOnClickListener {
            val user = User(
                fullName = binding.etFullName.text.toString(),
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
            lifecycleScope.launch {
                viewModel.signUp(user)
            }
        }
    }

    private fun renderUiState() {
        binding.root.children.forEach {

        }
    }
}