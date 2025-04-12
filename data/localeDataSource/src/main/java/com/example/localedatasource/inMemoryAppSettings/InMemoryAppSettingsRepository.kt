package com.example.localedatasource.inMemoryAppSettings

import com.example.localedatasource.inMemoryAppSettings.models.SettingsResponse
import com.example.localedatasource.inMemoryAppSettings.models.DefaultSettingsResponse

interface InMemoryAppSettingsRepository {

    fun getSettings(language:String): SettingsResponse

    fun getDefaultSettings(language: String):DefaultSettingsResponse
}