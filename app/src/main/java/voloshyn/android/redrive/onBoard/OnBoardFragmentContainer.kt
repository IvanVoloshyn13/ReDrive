package voloshyn.android.redrive.onBoard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentOnBoardContainerBinding

class OnBoardFragmentContainer : Fragment(R.layout.fragment_on_board_container) {
    private lateinit var binding: FragmentOnBoardContainerBinding
    private lateinit var pager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnBoardContainerBinding.bind(view)

        pager = binding.pager
        val pagerAdapter = OnBoardPagerAdapter(requireActivity())
        pager.adapter = pagerAdapter
    }
}