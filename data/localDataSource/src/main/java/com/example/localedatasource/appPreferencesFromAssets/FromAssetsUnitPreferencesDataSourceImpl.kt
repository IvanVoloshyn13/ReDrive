package com.example.localedatasource.appPreferencesFromAssets

import android.content.Context
import com.example.localedatasource.appPreferencesFromAssets.models.PreferencesResponse
import com.example.localedatasource.appPreferencesFromAssets.models.DefaultPreferencesResponse
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FromAssetsUnitPreferencesDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : FromAssetsUnitPreferencesDataSource {

    override fun getPreferences(language: String): PreferencesResponse {
        val assetFileName = when (language) {
            "pl" -> "pl_asset_app_unit_preferences.json"
            "en" -> "en_asset_app_unit_preferences.json"
            else -> "en_asset_app_unit_preferences.json"
        }
        val json = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        return gson.fromJson(json, PreferencesResponse::class.java)
    }

    override fun getDefaultPreferences(language: String): DefaultPreferencesResponse {
        val assetFileName = when (language) {
            "pl" -> "pl_default_unit_preferences.json"
            "en" -> "en_default_unit_preferences.json"
            else -> "en_default_unit_preferences.json"
        }
        val json = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        return gson.fromJson(json, DefaultPreferencesResponse::class.java)
    }
}