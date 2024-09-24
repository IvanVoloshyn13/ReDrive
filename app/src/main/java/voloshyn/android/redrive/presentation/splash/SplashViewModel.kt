package voloshyn.android.redrive.presentation.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.redrive.presentation.onBoard.model.PresentationError
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoard: OnBoardIsFinishedUseCase,
) : ViewModel() {
    private val scope = viewModelScope()

    private val _onBoardStatus = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val onBoardStatus = _onBoardStatus.asSharedFlow()



    init {
        scope.launch {
            val result = onBoard.invoke()
            _onBoardStatus.emit(result)
        }
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

