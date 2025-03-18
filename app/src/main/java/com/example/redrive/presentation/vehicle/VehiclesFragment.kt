package com.example.redrive.presentation.vehicle

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.redrive.R
import com.example.redrive.databinding.FragmentVehiclesBinding
import com.example.redrive.viewBinding

class VehiclesFragment : Fragment(R.layout.fragment_vehicles) {
    private val binding by viewBinding<FragmentVehiclesBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bttAddNewVehicle.setOnClickListener {
            findNavController().navigate(R.id.action_vehiclesFragment_to_newVehicleFragment)
        }
    }
}