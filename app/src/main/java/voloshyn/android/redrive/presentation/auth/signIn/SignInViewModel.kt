package voloshyn.android.redrive.presentation.auth.signIn

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.useCase.auth.SignInWithEmailUseCase
import voloshyn.android.redrive.utils.toStringResource
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signIn: SignInWithEmailUseCase
) : ViewModel() {
    private val viewModelScope = viewModelScope()

    private val _state: MutableStateFlow<FragmentSignInState> =
        MutableStateFlow(FragmentSignInState())
    val state = _state.asStateFlow()

    suspend fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    loading = true, signInStatus = SignInStatus.SignOut
                )
            }
            val result = signIn.invoke(email, password)
            when (result) {
                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            isError = true,
                            errorMessage = result.error.toStringResource(),
                            signInStatus = SignInStatus.Failure
                        )
                    }
                }

                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            signInStatus = SignInStatus.SignIn(user = result.data),
                            user = (User(
                                id = result.data.id,
                                fullName = result.data.fullName,
                                email = result.data.email
                            ))
                        )
                    }
                }
            }
        }
    }

}

