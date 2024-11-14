package voloshyn.android.redrive.presentation.onBoard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentOnBoard1Binding
import voloshyn.android.redrive.utils.navigateToSignInFragment
import voloshyn.android.redrive.utils.viewBinding
@AndroidEntryPoint
class OnBoardFragment1 : Fragment(R.layout.fragment_on_board_1) {
    private val viewModel: OnBoardViewModel by viewModels()
    private val binding by viewBinding<FragmentOnBoard1Binding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pager = requireActivity().findViewById<ViewPager2>(R.id.pager)
        binding.bttNext.setOnClickListener {
            pager.currentItem = OnBoardFragmentContainer.Companion.OnBoardScreens.THIRD
        }

        binding.bttSkip.setOnClickListener {
            viewModel.finishOnBoard()
            navigateToSignInFragment()
        }

    }


}