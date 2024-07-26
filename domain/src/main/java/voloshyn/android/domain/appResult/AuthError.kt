package voloshyn.android.domain.appResult

interface AuthError : AppError {
    enum class Password : AuthError {
        NO_UPPER_CASE, NO_LOWER_CASE, NO_DIGIT, TO_SHORT
    }

    enum class Auth : AuthError {
        FIREBASE_AUTH_ERROR,INVALID_CREDENTIALS,USER_COLLISION,NO_USER_DETECTED,UNKNOWN_ERROR
    }

}