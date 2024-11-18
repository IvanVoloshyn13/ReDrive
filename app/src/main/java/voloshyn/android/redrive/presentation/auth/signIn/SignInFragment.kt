package voloshyn.android.redrive.presentation.auth.signIn

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentSignInBinding
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private val binding by viewBinding<FragmentSignInBinding>()
    private val viewmodel by viewModels<SignInViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spannableString = SpannableString(binding.tvSignUp.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.tvSignUp.text = spannableString

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.bttSignIn.setOnClickListener {
            emailPasswordSignIn()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.state.collectLatest {
                binding.progressBar.visibility = if (it.loading) View.VISIBLE else View.GONE
                when (it.signInStatus) {
                    SignInStatus.Failure -> {
                        it.errorMessage?.let { stringRes ->
                            Snackbar.make(view, getString(stringRes), Snackbar.LENGTH_LONG)
                                .setTextMaxLines(3)
                                .show()
                        }
                    }

                    is SignInStatus.SignIn -> {
                        findNavController().navigate(R.id.action_signInFragment_to_newVehicleFragment)
                    }

                    SignInStatus.SignOut -> {}
                }
            }
        }
    }

    private fun emailPasswordSignIn() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            lifecycleScope.launch {
                viewmodel.signIn(email, password)
            }
        }
    }

}
