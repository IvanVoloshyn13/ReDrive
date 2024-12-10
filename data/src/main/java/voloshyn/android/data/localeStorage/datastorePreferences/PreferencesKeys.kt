package voloshyn.android.data.localeStorage.datastorePreferences

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val ON_BOARD_STATUS= stringPreferencesKey("on_board_finished")
    val CURRENT_USER= stringPreferencesKey("current_user")
    val REMEMBER_VEHICLE= longPreferencesKey("remember_vehicle")
}