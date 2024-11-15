package voloshyn.android.domain.appResult

typealias RootError = AppError

/** Use it only for network  */
sealed interface AppResult<out D, out E : RootError> {
    class Success<out D>(val data: D) : AppResult<D, Nothing>
    class Error<out D, out E : RootError>(val data: D? = null, val error: E) : AppResult<D, E>
}
