package com.example.redrive

import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.domain.appResult.AuthException

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

fun AuthException.getStringResource(): Int {
    return when (this) {
        // AuthException.AUTHENTICATION_FAILED -> R.string.firebase_auth_error
        AuthException.INVALID_PASSWORD -> R.string.invalid_credentials
        //AuthException.USER_ALREADY_EXISTS -> R.string.user_collision
        AuthException.USER_NOT_FOUND -> R.string.no_user_detected
        AuthException.UNKNOWN_ERROR -> R.string.unknown_error_firebase
    }
}