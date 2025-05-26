package com.example.localedatasource.dataStore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val CURRENT_VEHICLE: Preferences.Key<String> = stringPreferencesKey("current_vehicle")
    val CURRENT_USER = stringPreferencesKey("current_user")

    val VEHICLES_UPLOAD_AT = longPreferencesKey("vehicles_upload_at")
    val LAST_PULL_REFUELS = longPreferencesKey("last_pull_refuels")
}