package voloshyn.android.redrive.presentation.tabs.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import voloshyn.android.domain.useCase.user.GetCurrentUserUseCase
import voloshyn.android.redrive.utils.viewModelScope
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val user: GetCurrentUserUseCase
) : ViewModel() {

    private val scope = viewModelScope()

    private val _state = MutableStateFlow<String>("")
    val state = _state.asStateFlow()

    init {
        scope.launch {
          val user = user.invoke()
            user.let {
                val userInitial = it.let {
                    val nameParts = it.fullName.split(" ")
                    val firstNameInitial =
                        nameParts.getOrNull(0)?.firstOrNull()?.uppercaseChar() ?: ""
                    val lastNameInitial =
                        nameParts.getOrNull(1)?.firstOrNull()?.uppercaseChar() ?: ""
                    "$firstNameInitial$lastNameInitial"
                }
                _state.value = userInitial
            }
        }
    }

}