package com.example.redrive.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.redrive.R
import com.example.redrive.databinding.FragmentLogsBinding
import com.example.redrive.viewBinding

class LogsFragment : Fragment(R.layout.fragment_logs) {
    private val binding by viewBinding<FragmentLogsBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivDropDownMenu.setOnClickListener {
            findNavController().navigate(R.id.action_logsFragment_to_vehicle_flow)
        }
    }
}