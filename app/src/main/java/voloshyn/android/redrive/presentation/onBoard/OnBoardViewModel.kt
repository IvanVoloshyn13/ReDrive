package voloshyn.android.redrive.presentation.onBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import voloshyn.android.domain.useCase.onBoard.OnBoardFinishUseCase
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val onBoard: OnBoardFinishUseCase
) : ViewModel() {

    fun finishOnBoard() {
        viewModelScope.launch {
           onBoard.invoke(true)
        }
    }

    companion object {
        private const val TAG = "exception"
    }
}