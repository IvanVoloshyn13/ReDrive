package com.example.redrive.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.databinding.FragmentSplashBinding
import com.example.redrive.presentation.tabs.TabsFragment
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding<FragmentSplashBinding>()
    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderAnimations()
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                delay(1500)
                viewModel.navigation.collectLatest {
                    when (it) {
                        Router.SplashDirections.ToProfile -> {
                            navigate(TabsFragment.Companion.Destinations.PROFILE)
                        }

                        Router.SplashDirections.ToRedrive -> {
                            navigate(TabsFragment.Companion.Destinations.REDRIVE)
                        }
                    }

                }
            }
        }
    }

    private fun renderAnimations() {
        binding.progressBar.apply {
            alpha = 0.2f
            scaleX = 0.7f
            scaleY = 0.7f
        }

        binding.ivLogo.apply {
            alpha = 0.2f
            scaleX = 0.4f
            scaleY = 0.4f
        }

        binding.progressBar.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(200)
            .setDuration(1500)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()


        binding.ivLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(200)
            .setDuration(1500)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()
    }

    private fun navigate(destination: String) {
        val action = SplashFragmentDirections.actionSplashFragmentToTabsFragment(destination)
        findNavController().navigate(action)
    }

}