package com.example.redrive.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.account.SignInStatus
import com.example.redrive.R
import com.example.redrive.core.RedriveDirection
import com.example.redrive.core.Router
import com.example.redrive.core.navigate
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
        viewModel.addAuthStateListener()

        observeViewModel()
        setViewsOnClickListeners()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.state.collectLatest {
                    renderUi(it)
                }
            }

            launch {
                viewModel.navigation.collectLatest {
                    if (it != null) {
                        navigateByDirection(it)
                    }
                }
            }
        }
    }

    private fun navigateByDirection(direction: RedriveDirection) {
        when (direction) {
            Router.ProfileDirections.ToSettings -> {
                navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
            }

            Router.ProfileDirections.ToSignIn -> {
                navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
            }

            Router.ProfileDirections.ToEditVehicles -> {
                navigate(ProfileFragmentDirections.actionProfileFragmentToVehicleFlow())
            }

        }
    }

    private fun renderUi(state: FragmentProfileState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.tvSignIn.visibility =
            if (state.signedInStatus == SignInStatus.SignOut) View.VISIBLE else View.GONE
        binding.tvSignOut.visibility =
            if (state.signedInStatus == SignInStatus.SignOut) View.GONE else View.VISIBLE
        binding.tvUserInitial.text = state.userInitials
    }

    private fun setViewsOnClickListeners() {
        binding.tvSignIn.setOnClickListener {
            viewModel.navigate(Router.ProfileDirections.ToSignIn)
        }
        binding.tvEditVehicles.setOnClickListener {
            viewModel.navigate(Router.ProfileDirections.ToEditVehicles)
        }
        binding.tvSettings.setOnClickListener {
            viewModel.navigate(Router.ProfileDirections.ToSettings)
        }
        binding.tvSignOut.setOnClickListener {
            viewModel.onSignOutBtnClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeAuthStateListener()
    }

}

