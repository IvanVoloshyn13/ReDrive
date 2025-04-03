package com.example.redrive.presentation.auth.signIn

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.SignInStatus
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.databinding.FragmentSignInBinding
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
import com.example.redrive.core.navigate
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding<FragmentSignInBinding>()
    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectState()
        setSpannableSignUpText()
        setupListeners()
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.state.collectLatest { state ->
                    updateUI(state)
                }
            }
            launch {
                viewModel.error.collectLatest {
                    if (it.first) {
                        showErrorAndResetState(it.second) {
                            viewModel.onErrorShown()
                        }
                    }
                }
            }

            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        Router.SignInDirections.ToSignUp -> {
                            navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
                        }

                        Router.SignInDirections.ToProfile -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }

        }
    }

    private fun updateUI(state: FragmentSignInState) {
        binding.progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE

        // Prevent unnecessary updates
        if (binding.etEmail.text.toString() != state.email) {
            binding.etEmail.setTextKeepState(state.email)
        }
        if (binding.etPassword.text.toString() != state.password) {
            binding.etPassword.setTextKeepState(state.password)
        }

    }

    private fun setupListeners() {

        binding.bttSignIn.setOnClickListener { viewModel.signIn() }
        binding.tvSignUp.setOnClickListener { viewModel.navigate(Router.SignInDirections.ToSignUp) }

        binding.etEmail.doAfterTextChanged { viewModel.onEmailTextChange(it.toString()) }
        binding.etPassword.doAfterTextChanged { viewModel.onPasswordTextChange(it.toString()) }

        hideSoftInputAndClearViewsFocus(binding.root)

    }

    private fun setSpannableSignUpText() {
        val spannableString = SpannableString(binding.tvSignUp.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.tvSignUp.text = spannableString
    }

}