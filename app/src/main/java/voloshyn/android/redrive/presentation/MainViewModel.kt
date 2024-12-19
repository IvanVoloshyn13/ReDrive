package voloshyn.android.redrive.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.useCase.user.ObserveCurrentUserUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userProvider: ObserveCurrentUserUseCase,
) : ViewModel() {

    fun observeUser(): Flow<User?> {
        return userProvider.invoke()
    }

}