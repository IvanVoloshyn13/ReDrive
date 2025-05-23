package com.example.redrive.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.userSession.GetUserInitialsUseCase
import com.example.domain.useCase.userSession.IsUserSignedInUseCase
import com.example.domain.useCase.userSession.SignOutUseCase
import com.example.redrive.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val getUserInitialsUseCase: GetUserInitialsUseCase,
    private val signOutUseCase: SignOutUseCase,
) : BaseViewModel() {

    private var authStateJob: Job? = null

    private val _state: MutableStateFlow<FragmentProfileState> = MutableStateFlow(
        FragmentProfileState()
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            showProgressBar()
            getUserInitialsUseCase().collectLatest { result ->
                hideProgressBar()
                _state.update {
                    it.copy(
                        userInitials = result
                    )
                }
            }
        }
    }

    fun addAuthStateListener() {
        authStateJob = viewModelScope.launch {
            isUserSignedInUseCase.invoke().collectLatest { status ->
                _state.update {
                    it.copy(
                        signedInStatus = status
                    )
                }
            }
        }
    }

    fun removeAuthStateListener() {
        authStateJob?.cancel()
    }

    fun onSignOutBtnClick() {
        viewModelScope.launch {
            showProgressBar()
            delay(1000)
            signOutUseCase()
            hideProgressBar()
        }
    }

    private fun showProgressBar() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun hideProgressBar() {
        _state.update {
            it.copy(
                isLoading = false
            )
        }
    }

}