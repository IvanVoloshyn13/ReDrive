package com.example.redrive.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.redrive.R
import com.example.redrive.databinding.FragmentSplashBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding<FragmentSplashBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderAnimations()
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                delay(1500)
                findNavController().navigate(R.id.action_splashFragment_to_tabsFragment)
            }
        }
    }

    private fun renderAnimations() {
        binding.progressBar.apply {
            alpha=0.2f
            scaleX=0.7f
            scaleY=0.7f
        }

        binding.ivLogo.apply {
            alpha=0.2f
            scaleX=0.4f
            scaleY=0.4f
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
}