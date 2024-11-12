package voloshyn.android.domain.appResult

interface AuthenticationError : AppError {
    enum class AuthError : AuthenticationError {
        AUTHENTICATION_FAILED,INVALID_CREDENTIALS,USER_ALREADY_EXISTS,USER_NOT_FOUND,UNKNOWN_ERROR
    }

}

enum class Password : AuthenticationError {
    NO_UPPER_CASE, NO_LOWER_CASE, NO_DIGIT, TO_SHORT
}