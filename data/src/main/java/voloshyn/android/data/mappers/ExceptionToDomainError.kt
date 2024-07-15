package voloshyn.android.data.mappers

import androidx.datastore.core.IOException
import voloshyn.android.domain.appResult.DataError

fun Exception.toDataError(): DataError.Locale {
    return when (this) {
        is IOException -> DataError.Locale.STORAGE_ERROR
        is NullPointerException -> DataError.Locale.DATA_NOT_FOUND
        else -> DataError.Locale.UNKNOWN_ERROR
    }
}