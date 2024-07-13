package voloshyn.android.redrive.presentation.onBoard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentOnBoardContainerBinding
@AndroidEntryPoint
class OnBoardFragmentContainer : Fragment(R.layout.fragment_on_board_container){
    private lateinit var binding: FragmentOnBoardContainerBinding
    private lateinit var pager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnBoardContainerBinding.bind(view)

        pager = binding.pager
        val pagerAdapter = OnBoardPagerAdapter(requireActivity())
        pager.adapter = pagerAdapter
    }

    companion object{
        object OnBoardScreens{
            const val FIRST=0
            const val SECOND=1
            const val THIRD=2
        }
    }
}