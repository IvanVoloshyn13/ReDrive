package voloshyn.android.redrive.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentSplashBinding
import voloshyn.android.redrive.presentation.main.MainActivity
import voloshyn.android.redrive.presentation.onBoard.OnBoardActivity
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val binding by viewBinding<FragmentSplashBinding>()
    private val viewModel:SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderAnimations()
        lifecycleScope.launch {
            delay(2500)
            viewModel.onBoardStatus.collectLatest {
                launchMainScreen(it)
            }

        }

    }

    private fun launchMainScreen(onBoardFinished: Boolean) {
        val intent = Intent(
            requireContext(),
            destination(onBoardFinished)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        val args = MainActivityArgs(completed)
//        intent.putExtras(args.toBundle())
        startActivity(intent)

    }

    private fun destination(onBoardFinished: Boolean): Class<*> {
        return if (onBoardFinished) MainActivity::class.java else OnBoardActivity::class.java
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