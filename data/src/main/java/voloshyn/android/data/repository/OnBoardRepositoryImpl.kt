package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.data.localeStorage.datastorePreferences.PreferencesKeys
import voloshyn.android.domain.models.OnBoardStatus
import voloshyn.android.domain.repository.OnBoardRepository
import javax.inject.Inject

class OnBoardRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnBoardRepository {

    override suspend fun onFinish(status: OnBoardStatus) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ON_BOARD_STATUS] = status.name
        }
    }

    override suspend fun isFinished(): OnBoardStatus {
        val result = dataStore.data.map { preferences ->
            val status = preferences[PreferencesKeys.ON_BOARD_STATUS]
            if (status != null) enumValueOf<OnBoardStatus>(status) else OnBoardStatus.IN_PROGRESS
        }.first()
        return result
    }

}







