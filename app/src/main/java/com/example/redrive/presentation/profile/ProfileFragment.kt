package com.example.redrive.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.SignInStatus
import com.example.redrive.R
import com.example.redrive.databinding.FragmentProfileBinding
import com.example.redrive.findTopNavController
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding<FragmentProfileBinding>()
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectState()
        setupListeners()
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                updateUi(it)
            }
        }
    }

    private fun updateUi(state: FragmentProfileState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        when (state.signedInStatus) {
            SignInStatus.Failure -> TODO()
            SignInStatus.SignOut -> {
                binding.tvSignIn.visibility = View.VISIBLE
                binding.tvSignOut.visibility = View.GONE
            }

            SignInStatus.SignedIn -> {
                binding.tvSignIn.visibility = View.GONE
                binding.tvSignOut.visibility = View.VISIBLE
            }
        }

        binding.tvUserInitial.text = state.userInitials
    }

    private fun setupListeners() {
        binding.tvSignIn.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_auth_flow)
        }
        binding.tvSignOut.setOnClickListener {
            viewModel.signOut()

        }
        binding.tvEditVehicles.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_vehiclesFragment)
        }
        binding.tvSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

    }

}

