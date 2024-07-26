package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.data.safeCall
import voloshyn.android.domain.repository.OnBoardRepository
import javax.inject.Inject

class OnBoardRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnBoardRepository {

    override suspend fun finish(isFinished: Boolean) {
        safeCall {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ON_BOARD_IS_FINISHED] = isFinished
            }
        }
    }


}







