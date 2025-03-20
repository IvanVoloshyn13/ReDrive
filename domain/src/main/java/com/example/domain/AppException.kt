package com.example.domain

open class AppException : RuntimeException()

sealed class VehicleException() : AppException() {
    class IsCurrentVehicleException() : VehicleException()

    class NoCurrentVehicleException() : VehicleException()
}

