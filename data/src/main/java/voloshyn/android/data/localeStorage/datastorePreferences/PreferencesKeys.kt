package voloshyn.android.data.localeStorage.datastorePreferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object PreferencesKeys {
    val ON_BOARD_STATUS= stringPreferencesKey("on_board_finished")
    val REMEMBER_ME= booleanPreferencesKey("remember_me")
    val REMEMBER_VEHICLE= longPreferencesKey("remember_vehicle")
}