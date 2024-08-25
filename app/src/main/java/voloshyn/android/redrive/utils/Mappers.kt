package voloshyn.android.redrive.utils

import voloshyn.android.app.R
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.appResult.DataError

fun DataError.Locale.toStringResource(): Int {
    return when (this) {
        DataError.Locale.STORAGE_ERROR -> R.string.storage_error
        DataError.Locale.DATA_NOT_FOUND -> R.string.data_not_found
        DataError.Locale.UNKNOWN_ERROR -> R.string.unknown_error_datastore
        else -> TODO()
    }
}

fun AuthError.Auth.toStringResource():Int{
    return when(this){
        AuthError.Auth.FIREBASE_AUTH_ERROR -> R.string.firebase_auth_error
        AuthError.Auth.INVALID_CREDENTIALS -> R.string.invalid_credentials
        AuthError.Auth.USER_COLLISION -> R.string.user_collision
        AuthError.Auth.NO_USER_DETECTED -> R.string.no_user_detected
        AuthError.Auth.UNKNOWN_ERROR -> R.string.unknown_error_firebase
    }
}