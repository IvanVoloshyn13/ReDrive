package voloshyn.android.redrive.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentSplashBinding
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val binding by viewBinding<FragmentSplashBinding>()
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderAnimations()
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                delay(1500)
                viewModel.navigation.collectLatest {
                    launchMainFragment(it)
                }
            }
        }

    }

    private fun launchMainFragment(destination: NavigationPath) {
        when (destination) {
            is NavigationPath.ToTabs -> findNavController().navigate(R.id.action_splashFragment_to_tabsFragment)
            else -> navigateToInitialGraph(R.id.action_splashFragment_to_initial, destination)
        }
    }


    private fun navigateToInitialGraph(action: Int, destination: NavigationPath) {
        val navController = findNavController()
        val navGraph = navController.navInflater.inflate(R.navigation.initial)
        when (destination) {
            NavigationPath.ToAddNewVehicle -> navGraph.setStartDestination(R.id.newVehicleFragment)
            NavigationPath.ToOnBoard -> navGraph.setStartDestination(R.id.onBoardFragmentContainer)
            NavigationPath.ToSignIn -> navGraph.setStartDestination(R.id.signInFragment)
            NavigationPath.ToTabs -> findNavController().navigate(action)
        }
        navController.graph = navGraph

    }


    private fun renderAnimations() {
        binding.progressBar.alpha = 0f
        binding.progressBar.animate()
            .alpha(0.7f)
            .setDuration(1500)
            .start()

        binding.ivLogo.alpha = 0f
        binding.ivLogo.rotationX = 0f
        binding.ivLogo.animate()
            .alpha(1f)
            .setStartDelay(500)
            .setDuration(2000)
            .start()
    }
}