package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.data.mappers.toDataError
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.repository.OnBoardRepository
import javax.inject.Inject

class OnBoardRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnBoardRepository {

    override suspend fun finish(isFinished: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ON_BOARD_IS_FINISHED] = isFinished
            }
        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun isFinished(): AppResult<Boolean, DataError.Locale> {
        return try {
            val flow = dataStore.data.map { preferences ->
                preferences[PreferencesKeys.ON_BOARD_IS_FINISHED] ?: false
            }
            AppResult.Success(data = flow.first())
        } catch (e: IOException) {
            return AppResult.Error(data = false, e.toDataError())
        } catch (e: Exception) {
            throw e
        }
    }

}



