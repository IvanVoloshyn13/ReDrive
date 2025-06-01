package com.example.localedatasource.dataStore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val CURRENT_VEHICLE: Preferences.Key<String> = stringPreferencesKey("current_vehicle")
    val CURRENT_USER = stringPreferencesKey("current_user")

    //1-Fetched, -1-Failed ,0-Pending
    val VEHICLE_FETCH_STATUS = intPreferencesKey("vehicle_fetch_status")

    val PREFS_FETCH_CHECKPOINT = longPreferencesKey("last_prefs_fetch")
    val REFUELS_FETCH_CHECKPOINT = longPreferencesKey("last_vehicle_fetch")

}

