package com.example.localedatasource.inMemoryAppSettings

import com.example.localedatasource.inMemoryAppSettings.models.AppSettingsResponse
import com.example.localedatasource.inMemoryAppSettings.models.DefaultAppSettingsResponse

interface InMemoryAppSettingsRepository {

    fun getSettings(language:String): AppSettingsResponse

    fun getDefaultSettings(language: String):DefaultAppSettingsResponse
}