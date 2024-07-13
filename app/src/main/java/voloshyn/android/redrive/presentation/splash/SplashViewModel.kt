package voloshyn.android.redrive.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoard: OnBoardIsFinishedUseCase
) : ViewModel() {

    private val _onBoardStatus = MutableSharedFlow<Boolean>()
    val onBoardStatus = _onBoardStatus.asSharedFlow()

    init {
        viewModelScope.launch {
            val result = onBoard.isFinished()
            _onBoardStatus.emit(onBoardStatus(result))
        }
    }

    private fun onBoardStatus(result: AppResult<Boolean, DataError.Locale>): Boolean {
        return when (result) {
            is AppResult.Error -> false
            is AppResult.Success -> result.data
        }
    }
}