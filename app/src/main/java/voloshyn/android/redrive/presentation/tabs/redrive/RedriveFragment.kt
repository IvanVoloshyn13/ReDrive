package voloshyn.android.redrive.presentation.tabs.redrive

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentRedriveBinding
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.redrive.presentation.tabs.vehicles.VehiclesViewModel
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class RedriveFragment : Fragment(R.layout.fragment_redrive) {

    private val binding by viewBinding<FragmentRedriveBinding>()
    private val vehicleViewModel by viewModels<VehiclesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("FRAGMENT_REDRIVE",host.toString())
        super.onViewCreated(view, savedInstanceState)
        binding.bttAddRefuel.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_newRefuelFragment)
        }

        binding.ivDropDownMenu.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_vehiclesFragment)
        }

    }


}