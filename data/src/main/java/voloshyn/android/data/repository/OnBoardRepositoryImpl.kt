package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.domain.repository.OnBoardRepository
import javax.inject.Inject

class OnBoardRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnBoardRepository {

    override suspend fun onFinish(isFinished: Boolean) {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ON_BOARD_IS_FINISHED] = isFinished
        }
    }

    override suspend fun isFinished(): Boolean {
        val isFinished = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ON_BOARD_IS_FINISHED] ?: false
        }.first()
        return isFinished
    }

}







