package com.example.redrive.presentation.auth.signIn

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.SignInStatus
import com.example.redrive.R
import com.example.redrive.databinding.FragmentSignInBinding
import com.example.redrive.findTopNavController
import com.example.redrive.hideSoftInputAndClearViewsFocus
import com.example.redrive.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding<FragmentSignInBinding>()
    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        setSpannableSignUpText()
        setupListeners()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                updateUI(state)
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

        when (state.signInStatus) {
            SignInStatus.Failure -> showError(state.errorMessage)
            SignInStatus.SignedIn -> navigateToTabs()
            SignInStatus.SignOut -> Unit
        }

    }

    private fun navigateToTabs() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation.collectLatest { navigate ->
                if (navigate) {
                    findTopNavController().navigate(R.id.action_global_tabsFragment)
                }
            }
        }
    }

    private fun showError(@StringRes errorMessage: Int) {
        if (errorMessage == NO_STRING_RES) return
        Snackbar.make(requireView(), getString(errorMessage), Snackbar.LENGTH_LONG)
            .setTextMaxLines(3)
            .show()
        viewModel.resetErrorState()
    }

    private fun setupListeners() {

        binding.bttSignIn.setOnClickListener { viewModel.signIn() }
        binding.tvSignUp.setOnClickListener { findNavController().navigate(R.id.action_signInFragment_to_signUpFragment) }

        binding.etEmail.doAfterTextChanged { viewModel.updateEmail(it.toString()) }
        binding.etPassword.doAfterTextChanged { viewModel.updatePassword(it.toString()) }

        hideSoftInputAndClearViewsFocus(binding.root)

    }

    private fun setSpannableSignUpText() {
        val spannableString = SpannableString(binding.tvSignUp.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.tvSignUp.text = spannableString
    }

}