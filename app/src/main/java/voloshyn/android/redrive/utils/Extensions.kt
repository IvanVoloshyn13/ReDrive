package voloshyn.android.redrive.utils

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import voloshyn.android.app.R
import voloshyn.android.domain.useCase.auth.ValidatePasswordState

fun Fragment.navigateToSignInFragment() {
    requireActivity().findNavController(R.id.main_fragment_container).navigate(R.id.action_onBoardFragmentContainer_to_sign_in_graph)
}

fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun ValidatePasswordState.message(): Int {
    return when {
        !hasUpperCase -> {
            R.string.password_error_uppercase
        }

        !hasDigit -> {
            R.string.password_error_digit
        }

        !hasValidLength -> {
            R.string.password_error_length
        }

        !hasSpecialChar -> {
            R.string.password_error_special
        }

        !hasLowerCase -> {
            R.string.password_error_lowercase
        }

        else -> {
            R.string.password_error_general
        }
    }
}

fun viewModelScope(errorHandler: (cause: Throwable) -> Unit): CoroutineScope {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorHandler(throwable)
        Log.e("EXCEPTION_HANDLER", "$throwable,  ${throwable.message}")
    }
    return CoroutineScope(SupervisorJob() + exceptionHandler)
}


fun viewModelScope(): CoroutineScope {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("EXCEPTION_HANDLER", "$throwable,  ${throwable.message}")
    }
    return CoroutineScope(SupervisorJob() + exceptionHandler)
}


