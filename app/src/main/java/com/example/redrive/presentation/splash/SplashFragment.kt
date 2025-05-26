package com.example.redrive.presentation.splash

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.navigate
import com.example.redrive.databinding.FragmentSplashBinding
import com.example.redrive.presentation.tabs.TabsFragment
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val ANIMATION_START_DELAY = 200L
private const val ANIMATION_DURATION = 1500L

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding<FragmentSplashBinding>()
    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderLogoAnimation()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(ANIMATION_DURATION)
            launch {
                viewModel.isLoading.collectLatest {
                    if (it) renderProgressBarAnimation() else binding.progressBar.visibility =
                        View.GONE
                }
            }
            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        Router.SplashDirections.ToProfile -> {
                            navigate(getNavigationAction(TabsFragment.Companion.Destinations.PROFILE))
                        }

                        Router.SplashDirections.ToApp -> {
                            navigate(getNavigationAction(TabsFragment.Companion.Destinations.REDRIVE))
                        }
                    }
                }
            }

        }
    }

    private fun renderProgressBarAnimation() {
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.apply {
            alpha = 0.2f
            scaleX = 0.7f
            scaleY = 0.7f
        }

        binding.progressBar.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(ANIMATION_START_DELAY)
            .setDuration(ANIMATION_DURATION)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()
    }

    private fun renderLogoAnimation() {
        binding.ivLogo.apply {
            alpha = 0.2f
            scaleX = 0.4f
            scaleY = 0.4f
        }

        binding.ivLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(ANIMATION_START_DELAY)
            .setDuration(ANIMATION_DURATION)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()
    }

    private fun getNavigationAction(destination: String): NavDirections {
        return SplashFragmentDirections.actionSplashFragmentToTabsFragment(
            destination
        )
    }

}