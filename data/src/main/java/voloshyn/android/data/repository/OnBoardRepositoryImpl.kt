package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.data.safeGetData
import voloshyn.android.data.safeUpdateData
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.repository.OnBoardRepository
import javax.inject.Inject

class OnBoardRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnBoardRepository {

    override suspend fun onFinish(isFinished: Boolean) {
        safeUpdateData {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ON_BOARD_IS_FINISHED] = isFinished
            }
        }
    }

    override suspend fun isFinished(): AppResult<Boolean, DataError.Locale> {
        val isFinished = safeGetData(defaultValue = false) {
            dataStore.data.map { preferences ->
                preferences[PreferencesKeys.ON_BOARD_IS_FINISHED] ?: false
            }.first()
        }
        return isFinished
    }

}







