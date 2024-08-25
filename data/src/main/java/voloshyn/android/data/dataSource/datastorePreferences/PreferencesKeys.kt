package voloshyn.android.data.dataSource.datastorePreferences

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val ON_BOARD_IS_FINISHED= booleanPreferencesKey("on_board_finished")
    val REMEMBER_ME= booleanPreferencesKey("remember_me")
}