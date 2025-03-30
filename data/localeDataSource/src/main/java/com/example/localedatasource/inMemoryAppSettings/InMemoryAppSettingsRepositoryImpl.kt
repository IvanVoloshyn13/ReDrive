package com.example.localedatasource.inMemoryAppSettings

import android.content.Context
import com.example.localedatasource.inMemoryAppSettings.models.AppSettingsResponse
import com.example.localedatasource.inMemoryAppSettings.models.DefaultAppSettingsResponse
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InMemoryAppSettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : InMemoryAppSettingsRepository {

    override fun getSettings(language: String): AppSettingsResponse {
        val assetFileName = when (language) {
            "pl" -> "pl_asset_app_settings.json"
            "en" -> "en_asset_app_settings.json"
            else -> "en_asset_app_settings.json"
        }
        val json = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        return gson.fromJson(json, AppSettingsResponse::class.java)
    }

    override fun getDefaultSettings(language: String): DefaultAppSettingsResponse {
        val assetFileName = when (language) {
            "pl" -> "pl_default_settings.json"
            "en" -> "en_default_settings.json"
            else -> "en_default_settings.json"
        }
        val json = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        return gson.fromJson(json, DefaultAppSettingsResponse::class.java)
    }
}