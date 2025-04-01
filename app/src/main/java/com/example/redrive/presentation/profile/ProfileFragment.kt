package com.example.redrive.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.domain.model.SignInStatus
import com.example.redrive.R
import com.example.redrive.databinding.FragmentProfileBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
            launch {
                viewModel.state.collectLatest {
                    updateUi(it)
                }
            }

            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        ProfileViewModel.Route.ToSettings -> {
                            navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
                        }

                        ProfileViewModel.Route.ToSignIn -> {
                            navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
                        }

                        ProfileViewModel.Route.ToEditVehicles -> {
                            navigate(ProfileFragmentDirections.actionProfileFragmentToVehicleFlow())
                        }

                        else -> return@collectLatest
                    }
                }
            }
        }
    }

    private fun updateUi(state: FragmentProfileState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.tvSignIn.visibility =
            if (state.signedInStatus == SignInStatus.SignOut) View.VISIBLE else View.GONE
        binding.tvSignOut.visibility =
            if (state.signedInStatus == SignInStatus.SignOut) View.GONE else View.VISIBLE
        binding.tvUserInitial.text = state.userInitials
    }

    private fun setupListeners() {
        binding.tvSignIn.setOnClickListener {
            viewModel.navigate(ProfileViewModel.Route.ToSignIn)
        }
        binding.tvEditVehicles.setOnClickListener {
            viewModel.navigate(ProfileViewModel.Route.ToEditVehicles)
        }
        binding.tvSettings.setOnClickListener {
            viewModel.navigate(ProfileViewModel.Route.ToSettings)
        }
        binding.tvSignOut.setOnClickListener {
            viewModel.onSignOutClick()
        }
    }

    private fun navigate(direction: NavDirections) {
        findNavController().navigate(direction)
    }

}

