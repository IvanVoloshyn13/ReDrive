package com.example.redrive.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.SignInStatus
import com.example.redrive.R
import com.example.redrive.databinding.FragmentProfileBinding
import com.example.redrive.findTopNavController
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
        binding.tvSignIn.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_auth_flow)
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                updateUi(it)
            }
        }
    }

    private fun updateUi(state: FragmentProfileState) {
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
}

