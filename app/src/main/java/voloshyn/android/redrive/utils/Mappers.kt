package voloshyn.android.redrive.utils

import voloshyn.android.app.R
import voloshyn.android.domain.appResult.AppError
import voloshyn.android.domain.appResult.DataError

fun AppError.toStringResource(): Int {
    return when (this) {
        DataError.Locale.STORAGE_ERROR -> R.string.storage_error
        DataError.Locale.DATA_NOT_FOUND -> R.string.data_not_found
        DataError.Locale.UNKNOWN_ERROR -> R.string.unknown_error
        else -> TODO()
    }
}