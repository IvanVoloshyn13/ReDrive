package voloshyn.android.data

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.datastore.core.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import voloshyn.android.data.mappers.toDataError
import voloshyn.android.domain.DataStoreException
import voloshyn.android.domain.LocalStorageException
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError

private const val TAG="DATASTORE_EXCEPTIONS"

suspend fun safeUpdateData(
    block: suspend () -> Unit,
) {
    try {
        block()
    } catch (e: IOException) {
        Log.e(TAG, e.toString())
        val appException = DataStoreException()
        throw appException
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
        val appException = DataStoreException()
        throw appException
    }
}


suspend fun <D> safeGetData(
    defaultValue: D? = null,
    call: suspend () -> D,
): AppResult<D, DataError.Locale> {
        try {
            return AppResult.Success(data = call())
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
            return AppResult.Error(defaultValue, error = e.toDataError())
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            return AppResult.Error(defaultValue, error = e.toDataError())
        }
    }

suspend fun <T> safeDbCallWithReturn(
    dispatcher: CoroutineDispatcher,
    call: suspend CoroutineScope.() -> T
): AppResult<T, DataError.Locale> {
    return try {
        withContext(dispatcher) {
            AppResult.Success(data = call())
        }
    } catch (e: SQLiteException) {
        e.printStackTrace()
        AppResult.Error(error = DataError.Locale.STORAGE_ERROR)
    } catch (e: NullPointerException) {
        e.printStackTrace()
        AppResult.Error(error = DataError.Locale.DATA_NOT_FOUND)
    } catch (e: Exception) {
        e.printStackTrace()
        AppResult.Error(error = DataError.Locale.UNKNOWN_ERROR)
    }
}

suspend fun  safeDbCall(
    dispatcher: CoroutineDispatcher,
    call: suspend CoroutineScope.() -> Unit
) {
    try {
        withContext(dispatcher) {
            call()
        }
    } catch (e: SQLiteException) {
        e.printStackTrace()
        throw LocalStorageException()
    } catch (e: Exception) {
        e.printStackTrace()
        throw LocalStorageException()
    }
}




