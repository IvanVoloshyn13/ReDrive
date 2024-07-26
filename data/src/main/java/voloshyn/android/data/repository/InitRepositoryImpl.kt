package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.data.safeCallWithReturn
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.UserTuple
import voloshyn.android.domain.repository.InitRepository
import javax.inject.Inject

class InitRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>
) : InitRepository {

    override  fun isSignedIn(): AppResult<UserTuple?, AuthError.Auth> {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            AppResult.Success(
                data = UserTuple(
                    fullName = firebaseUser.displayName!!,
                    email = firebaseUser.email!!
                )
            )
        } else AppResult.Error(null, AuthError.Auth.NO_USER_DETECTED)

    }

    override suspend fun isFinished(): AppResult<Flow<Boolean>, DataError.Locale> {
        return safeCallWithReturn(defaultValue = flowOf(false)) {
            dataStore.data.map { preferences ->
                preferences[PreferencesKeys.ON_BOARD_IS_FINISHED] ?: false
            }
        }
    }
}