package com.example.redrive

import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.domain.AppException
import com.example.domain.appResult.AuthError
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun Fragment.hideSoftInputAndClearViewsFocus(root: ViewGroup) {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    root.setOnClickListener { view ->
        imm.hideSoftInputFromWindow(
            view.applicationWindowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    root.children.forEach {
        it.clearFocus()
    }
}

fun ViewModel.wrapLocaleDataSourceRequests(
    action:suspend () -> Unit,
    onError: suspend (e: AppException) -> Unit
) {
    viewModelScope.launch {
        try {
            action()
            delay(500)
        } catch (e: AppException) {
            onError(e)
        }
    }
}

fun AuthError.getStringResource(): Int {
    return when (this) {
        AuthError.AUTHENTICATION_FAILED -> R.string.firebase_auth_error
        AuthError.INVALID_PASSWORD -> R.string.invalid_credentials
        AuthError.USER_ALREADY_EXISTS -> R.string.user_collision
        AuthError.USER_NOT_FOUND -> R.string.no_user_detected
        AuthError.UNKNOWN_ERROR -> R.string.unknown_error_firebase
    }
}