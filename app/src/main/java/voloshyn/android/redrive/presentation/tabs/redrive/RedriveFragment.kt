package voloshyn.android.redrive.presentation.tabs.redrive

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentRedriveBinding
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.showToast
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class RedriveFragment : Fragment(R.layout.fragment_redrive) {

    private val binding by viewBinding<FragmentRedriveBinding>()
    private val viewModel by viewModels<RedriveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bttAddRefuel.setOnClickListener {
            if(viewModel.state.value.currentVehicle.name.isNotEmpty()) {
                findTopNavController().navigate(R.id.action_tabsFragment_to_refuelFragment)
            } else showToast( getString(R.string.add_vehicle_first))
        }

        binding.ivDropDownMenu.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_vehiclesFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                if (!it.isLoading) {
                    binding.tvCurrentVehicleName.text = it.currentVehicle.name.ifEmpty {
                        getString(R.string.add_new_vehicle)
                    }
                }
            }
        }

    }

}