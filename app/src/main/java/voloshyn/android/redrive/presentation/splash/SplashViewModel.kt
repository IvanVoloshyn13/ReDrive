package voloshyn.android.redrive.presentation.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.UserTuple
import voloshyn.android.domain.useCase.init.IsSignedInUseCase
import voloshyn.android.domain.useCase.init.OnBoardIsFinishedUseCase
import voloshyn.android.redrive.presentation.onBoard.model.PresentationError
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoard: OnBoardIsFinishedUseCase,
    private val signIn: IsSignedInUseCase
) : ViewModel() {
    private val scope = viewModelScope()

    private val _onBoardStatus = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val onBoardStatus = _onBoardStatus.asSharedFlow()

    private val _isSignedIn = MutableSharedFlow<UserTuple?>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val isSignedIn = _isSignedIn.asSharedFlow()

    private val _isError = MutableSharedFlow<PresentationError>()
    val isError = _isError.asSharedFlow()

    init {
        scope.launch {
            val result = onBoard.invoke()
            _onBoardStatus.emit(onBoardStatus(result))

            isSignedIn()
        }
    }

    private fun isSignedIn() {
        val result = signIn.invoke()
        when (result) {
            is AppResult.Success -> {
                _isSignedIn.tryEmit(result.data!!)
            }

            is AppResult.Error -> {
                _isSignedIn.tryEmit(null)
            }
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

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

