package voloshyn.android.data

import android.util.Log
import androidx.datastore.core.IOException
import kotlinx.coroutines.delay
import voloshyn.android.data.mappers.toDataError
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError



suspend fun safeLocalCall(
    maxAttempts: Int = 3,
    defaultDelay: Long = 250,
    call: suspend () -> Unit,
) {
    var delayTime = defaultDelay
    repeat(maxAttempts) {
        try {
            call()
            return
        } catch (e: IOException) {
            Log.e("CALL", e.toString())
            delay(it * delayTime)
            delayTime *= 2
            if (maxAttempts - 1 == it) throw e
        }
        catch (e: Exception) {
            Log.e("CALL", e.toString())
            delay(it * delayTime)
            delayTime *= 2
            if (maxAttempts - 1 == it) throw e
        }
    }
    throw IllegalStateException("Unknown exception from safeCallWithReturn.")
    TODO("refactor this functions for using Room later ")
}



suspend fun <D> safeLocaleCallWithReturn(
    defaultValue: D? = null,
    maxAttempts: Int = 3,
    defaultDelay: Long = 250,
    call: suspend () -> D,
): AppResult<D, DataError.Locale> {
    var delayTime = defaultDelay
    repeat(maxAttempts) {
        try {
            return AppResult.Success(data = call())
        } catch (e: IOException) {
            Log.e("CALL", e.toString())
            delay(it * delayTime)
            delayTime *= 2
            if (maxAttempts - 1 == it) return AppResult.Error(defaultValue, error = e.toDataError())
        } catch (e: Exception) {
            Log.e("CALL", e.toString())
            delay(it * delayTime)
            delayTime *= 2
            if (maxAttempts - 1 == it) return AppResult.Error(defaultValue, error = e.toDataError())
        }
    }
    throw IllegalStateException("Unknown exception from safeCallWithReturn.")
}
