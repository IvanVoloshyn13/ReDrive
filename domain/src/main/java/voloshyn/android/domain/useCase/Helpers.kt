package voloshyn.android.domain.useCase

import voloshyn.android.domain.appResult.AppError
import voloshyn.android.domain.appResult.AppResult

suspend fun <D, E : AppError, R> toResult(
    call: suspend () -> AppResult<D, E>,
    error: (error: E) -> AppResult.Error<R, E>,
    success: (data: D) -> AppResult.Success<R, E>

): AppResult<R, E> {
    return when (val result = call()) {
        is AppResult.Success -> success(result.data)
        is AppResult.Error -> error(result.error)
    }
}

