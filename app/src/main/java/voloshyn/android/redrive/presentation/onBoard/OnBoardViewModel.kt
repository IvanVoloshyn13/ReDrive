package voloshyn.android.redrive.presentation.onBoard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import voloshyn.android.domain.useCase.onBoard.OnBoardFinishUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val onBoard: OnBoardFinishUseCase
) : ViewModel() {
    private val scope = viewModelScope()

    fun finishOnBoard() {
        scope.launch {
            onBoard.invoke(true)
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