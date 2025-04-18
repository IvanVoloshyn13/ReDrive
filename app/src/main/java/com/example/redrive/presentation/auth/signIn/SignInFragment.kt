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

        hideSoftInputAndClearViewsFocus(binding.root)

        observeViewModel()
        setSpannableSignUpText()
        setOnEditTextChangeListeners()
        setViewsClickListeners()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.state.collectLatest { state ->
                    renderUiWithState(state)
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

    private fun renderUiWithState(state: FragmentSignInState) {
        binding.progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE

        if (binding.etEmail.text.toString() != state.email) {
            binding.etEmail.setTextKeepState(state.email)
        }
        if (binding.etPassword.text.toString() != state.password) {
            binding.etPassword.setTextKeepState(state.password)
        }

    }

    private fun setViewsClickListeners() {
        binding.bttSignIn.setOnClickListener { viewModel.onSignInBtnClick() }
        binding.tvSignUp.setOnClickListener { viewModel.navigate(Router.SignInDirections.ToSignUp) }
    }

    private fun setOnEditTextChangeListeners() {
        binding.etEmail.doAfterTextChanged { viewModel.onEmailTextChange(it.toString()) }
        binding.etPassword.doAfterTextChanged { viewModel.onPasswordTextChange(it.toString()) }
    }

    private fun setSpannableSignUpText() {
        val spannableString = SpannableString(binding.tvSignUp.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.tvSignUp.text = spannableString
    }

}