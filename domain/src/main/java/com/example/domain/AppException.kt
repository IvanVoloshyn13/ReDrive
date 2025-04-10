package com.example.domain

open class AppException : RuntimeException()

sealed class VehicleException() : AppException() {
    class IsCurrentVehicleException() : VehicleException()
    class NoCurrentVehicleException() : VehicleException()
    class InvalidOdometerValueException(val initialValue:Int):VehicleException()

}

sealed class UserException() : AppException() {
    class NoUserDetectedException() : UserException()
}

sealed class AuthException : AppException() {
    class UserNotFoundException : AuthException()
    class InvalidPasswordException : AuthException()
    class UnknownException : AuthException()
    class UserAlreadyExistsException : AuthException()
    class AuthenticationFailed : AuthException()
}



