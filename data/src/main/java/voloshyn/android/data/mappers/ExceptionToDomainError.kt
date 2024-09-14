package voloshyn.android.data.mappers

import android.database.sqlite.SQLiteException
import androidx.datastore.core.IOException
import voloshyn.android.domain.AppException
import voloshyn.android.domain.DataStoreException
import voloshyn.android.domain.FileNotFoundException
import voloshyn.android.domain.LocalStorageException
import voloshyn.android.domain.UnknownException
import voloshyn.android.domain.appResult.DataError

fun Exception.toDataError(): DataError.Locale {
    return when (this) {
        is IOException -> DataError.Locale.STORAGE_ERROR
        is NullPointerException -> DataError.Locale.DATA_NOT_FOUND
        else -> DataError.Locale.UNKNOWN_ERROR
    }
}

@Throws(AppException::class)
fun Throwable.throwAppException() {
    this.printStackTrace()
    val appException = when (this) {
        is SQLiteException -> LocalStorageException()
        is IOException -> DataStoreException()
        is NullPointerException -> FileNotFoundException()
        else -> UnknownException()
    }
    throw appException
}