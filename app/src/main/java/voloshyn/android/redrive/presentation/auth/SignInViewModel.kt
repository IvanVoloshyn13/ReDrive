package voloshyn.android.redrive.presentation.auth

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.useCase.auth.RememberMeUseCase
import voloshyn.android.domain.useCase.auth.SignInUseCase
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signIn: SignInUseCase,
    private val _rememberMe: RememberMeUseCase
) : ViewModel() {
    private val viewModelScope = viewModelScope()

    private val _state: MutableStateFlow<SignInState> = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    suspend fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    loading = true
                )
            }
            val result = signIn.invoke(email, password)
            when (result) {
                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            isError = true,
                            errorMessage = result.error.toStringResource()
                        )
                    }
                }

                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            isSignIn = true,
                            user = (
                                    UserTuple(
                                        id = result.data.id,
                                        fullName = result.data.fullName,
                                        email = result.data.email
                                    )
                                    )
                        )
                    }
                }
            }
        }
    }

    fun signInWithRememberMe(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            signIn(email, password)
            if (_state.value.isSignIn) {
                _rememberMe.invoke(rememberMe)
            }
        }


    }
}

data class SignInState(
    val loading: Boolean = false,
    val isSignIn: Boolean = false,
    val user: UserTuple = UserTuple.EMPTY_USER,
    val isError: Boolean = false,
    @StringRes val errorMessage: Int? = null
)