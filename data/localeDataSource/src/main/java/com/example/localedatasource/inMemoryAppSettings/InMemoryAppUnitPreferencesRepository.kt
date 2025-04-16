package com.example.localedatasource.inMemoryAppSettings

import com.example.localedatasource.inMemoryAppSettings.models.PreferencesResponse
import com.example.localedatasource.inMemoryAppSettings.models.DefaultPreferencesResponse

interface InMemoryAppUnitPreferencesRepository {

    fun getPreferences(language:String): PreferencesResponse

    fun getDefaultPreferences(language: String):DefaultPreferencesResponse
}