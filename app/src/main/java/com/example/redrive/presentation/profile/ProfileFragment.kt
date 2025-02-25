package com.example.redrive.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.redrive.R
import com.example.redrive.databinding.FragmentProfileBinding
import com.example.redrive.findTopNavController
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment:Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding<FragmentProfileBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSignIn.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_auth_flow)
        }
    }
}

