package voloshyn.android.redrive.utils

import voloshyn.android.app.R
import voloshyn.android.domain.DataStoreException
import voloshyn.android.domain.FileNotFoundException
import voloshyn.android.domain.LocalStorageException
import voloshyn.android.domain.UnknownException
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.appResult.DataError

fun DataError.Locale.toStringResource(): Int {
    return when (this) {
        DataError.Locale.STORAGE_ERROR -> R.string.storage_error
        DataError.Locale.DATA_NOT_FOUND -> R.string.data_not_found
        DataError.Locale.UNKNOWN_ERROR -> R.string.unknown_error_datastore
        else -> TODO()
    }
}

fun Throwable.toStringResource(): Int {
    return when (this) {
        is DataStoreException -> {
            R.string.storage_error
        }

        is LocalStorageException -> {
            R.string.storage_error
        }

        is FileNotFoundException -> {
            R.string.data_not_found
        }

        is UnknownException -> {
            R.string.unknown_error_datastore
        }

        else -> {
            R.string.unknown_error_datastore
        }
    }
}

fun AuthenticationError.AuthError.toStringResource(): Int {
    return when (this) {
        AuthenticationError.AuthError.AUTHENTICATION_FAILED -> R.string.firebase_auth_error
        AuthenticationError.AuthError.INVALID_CREDENTIALS -> R.string.invalid_credentials
        AuthenticationError.AuthError.USER_ALREADY_EXISTS -> R.string.user_collision
        AuthenticationError.AuthError.USER_NOT_FOUND -> R.string.no_user_detected
        AuthenticationError.AuthError.UNKNOWN_ERROR -> R.string.unknown_error_firebase
    }
}