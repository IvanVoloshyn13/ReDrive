package com.example.redrive.presentation.profile

import androidx.lifecycle.viewModelScope
import com.example.domain.model.SignInStatus
import com.example.domain.useCase.userSession.GetUserInitialsUseCase
import com.example.domain.useCase.userSession.IsUserSignedInUseCase
import com.example.domain.useCase.userSession.SignOutUseCase
import com.example.redrive.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private typealias SignedInStatusAndUserInitials = Pair<SignInStatus, String>

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val getUserInitialsUseCase: GetUserInitialsUseCase,
    private val signOutUseCase: SignOutUseCase,
) : BaseViewModel() {

    private val _state: MutableStateFlow<FragmentProfileState> = MutableStateFlow(
        FragmentProfileState()
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            combine(
                isUserSignedInUseCase.invoke(),
                getUserInitialsUseCase.invoke()
            ) { signedInStatus, initials ->
                SignedInStatusAndUserInitials(signedInStatus, initials)
            }.collect { combineResult ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        signedInStatus = combineResult.first,
                        userInitials = combineResult.second
                    )
                }
            }
        }
    }

    fun onSignOutClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            delay(500)
            signOutUseCase()
            _state.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

}