package voloshyn.android.redrive.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.redrive.presentation.onBoard.model.PresentationError
import voloshyn.android.redrive.utils.toStringResource
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoard: OnBoardIsFinishedUseCase
) : ViewModel() {

    private val _onBoardStatus = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val onBoardStatus = _onBoardStatus.asSharedFlow()

    private val _isError = MutableSharedFlow<PresentationError>()
    val isError = _isError.asSharedFlow()

    init {
        viewModelScope.launch {
            val result = onBoard.isFinished()
            _onBoardStatus.emit(onBoardStatus(result))
        }
    }

    private suspend fun onBoardStatus(result: AppResult<Flow<Boolean>, DataError.Locale>): Boolean {
        return when (result) {
            is AppResult.Error -> {
                _isError.tryEmit(PresentationError(true, result.error.toStringResource()))
                if (result.data != null) {
                    result.data!!.first()
                } else false
            }

            is AppResult.Success -> result.data.first()
        }
    }
}

