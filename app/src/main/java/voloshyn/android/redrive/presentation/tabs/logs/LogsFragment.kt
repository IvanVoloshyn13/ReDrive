package voloshyn.android.redrive.presentation.tabs.logs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentLogsBinding
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class LogsFragment : Fragment(R.layout.fragment_logs) {

    private val binding by viewBinding<FragmentLogsBinding>()
    private val viewModel by viewModels<LogsViewModel>()
    private lateinit var adapter:RefuelLogsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= RefuelLogsAdapter()
        initLogsAdapter()
        binding.ivDropDownMenu.setOnClickListener {
          //  findTopNavController().navigate(R.id.action_tabsFragment_to_vehiclesFragment)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                binding.tvCurrentVehicleName.text =
                    it.currentVehicle.name.ifEmpty { getString(R.string.add_new_vehicle) }
                adapter.submitList(it.refuelLogs)
            }
        }
    }

    private fun  initLogsAdapter(){
        val rv=binding.rvLogs
        rv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        rv.adapter=adapter
    }

}