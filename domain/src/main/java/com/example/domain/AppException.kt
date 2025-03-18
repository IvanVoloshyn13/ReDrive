package com.example.domain

open class AppException : RuntimeException()

class IsCurrentVehicleException() : AppException()

class NoCurrentVehicleException() : AppException()