package voloshyn.android.redrive.presentation.onBoard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import voloshyn.android.domain.models.OnBoardStatus
import voloshyn.android.domain.useCase.onBoard.FinishOnBoardUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val finishOnBoard: FinishOnBoardUseCase
) : ViewModel() {
    private val scope = viewModelScope()

    fun finishOnBoard() {
        scope.launch {
            finishOnBoard.invoke(OnBoardStatus.FINISHED)
        }
    }

    companion object {
        private const val TAG = "exception"
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}