package voloshyn.android.redrive.presentation.onBoard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentOnBoard1Binding
import voloshyn.android.app.databinding.FragmentOnBoardBinding
import voloshyn.android.redrive.utils.viewBinding


class OnBoardFragment : Fragment(R.layout.fragment_on_board) {

    private val binding by viewBinding<FragmentOnBoardBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pager = requireActivity().findViewById<ViewPager2>(R.id.pager)
        binding.bttNext.setOnClickListener {
            pager.currentItem = OnBoardFragmentContainer.Companion.OnBoardScreens.SECOND
        }
    }


}