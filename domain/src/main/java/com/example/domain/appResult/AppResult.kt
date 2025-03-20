package com.example.domain.appResult

typealias RootException = AppError

/** Use it only for network  */
interface AppResult<out R, out E : RootException> {
    class Success<out R>(val result: R) : AppResult<R, Nothing>
    class Error<out E : RootException>(val exception: E) : AppResult<Nothing, E>
}