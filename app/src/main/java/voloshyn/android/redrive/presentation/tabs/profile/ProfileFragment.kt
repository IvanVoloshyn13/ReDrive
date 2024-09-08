package voloshyn.android.redrive.presentation.tabs.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentProfileBinding
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding<FragmentProfileBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btt.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_signInFragment)
        }
    }
}

