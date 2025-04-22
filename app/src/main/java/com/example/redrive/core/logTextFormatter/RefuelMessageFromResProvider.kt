package com.example.redrive.core.logTextFormatter

interface RefuelMessageFromResProvider {
    fun getMessage(odometer: String, date: String): String
}