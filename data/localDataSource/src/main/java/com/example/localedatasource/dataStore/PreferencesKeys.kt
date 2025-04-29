package com.example.localedatasource.dataStore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val CURRENT_VEHICLE: Preferences.Key<Long> = longPreferencesKey("current_vehicle")
    val CURRENT_USER = stringPreferencesKey("current_user")
}