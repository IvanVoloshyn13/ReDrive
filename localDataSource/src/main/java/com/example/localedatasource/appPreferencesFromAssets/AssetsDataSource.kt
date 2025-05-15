package com.example.localedatasource.appPreferencesFromAssets

import com.example.localedatasource.appPreferencesFromAssets.models.PreferencesResponse
import com.example.localedatasource.appPreferencesFromAssets.models.DefaultPreferencesResponse

interface AssetsDataSource {

    fun getPreferences(language:String): PreferencesResponse

    fun getDefaultPreferences(language: String):DefaultPreferencesResponse
}