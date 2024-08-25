package voloshyn.android.redrive.presentation.onBoard

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.app.R
import voloshyn.android.redrive.utils.navigateToTabsFragment

@AndroidEntryPoint
class OnBoardFragment2:Fragment(R.layout.fragment_on_board_2) {
    private val viewModel: OnBoardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btt_get_started).setOnClickListener {
            viewModel.finishOnBoard()
            navigateToTabsFragment()
        }
    }


}