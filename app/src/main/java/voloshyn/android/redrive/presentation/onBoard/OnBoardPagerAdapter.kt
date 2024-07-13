package voloshyn.android.redrive.presentation.onBoard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_PAGES = 3

class OnBoardPagerAdapter(act: FragmentActivity) : FragmentStateAdapter(act) {
    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardFragment()
            1 -> OnBoardFragment1()
            else -> OnBoardFragment2()

        }
    }
}