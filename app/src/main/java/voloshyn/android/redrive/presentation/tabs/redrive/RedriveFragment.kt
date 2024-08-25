package voloshyn.android.redrive.presentation.tabs.redrive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentRedriveBinding
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class RedriveFragment : Fragment(R.layout.fragment_redrive) {

    private val binding by viewBinding<FragmentRedriveBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bttAddRefuel.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_newRefuelFragment)
        }
    }
}