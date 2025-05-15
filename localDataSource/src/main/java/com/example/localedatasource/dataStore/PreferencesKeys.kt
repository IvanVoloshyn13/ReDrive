package com.example.localedatasource.dataStore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val CURRENT_VEHICLE: Preferences.Key<Long> = longPreferencesKey("current_vehicle")
    val CURRENT_USER = stringPreferencesKey("current_user")
    val LAST_PULL_VEHICLES = longPreferencesKey("last_pull_vehicles")
    val LAST_PULL_REFUELS = longPreferencesKey("last_pull_refuels")
}