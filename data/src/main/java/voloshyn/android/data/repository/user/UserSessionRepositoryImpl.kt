package voloshyn.android.data.repository.user

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.localeStorage.datastorePreferences.PreferencesKeys
import voloshyn.android.data.localeStorage.room.dao.UsersDao
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.userAuth.UserSessionRepository
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val usersDao: UsersDao,
    private val dataStore: DataStore<Preferences>,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher
) : UserSessionRepository {

    private var _user: User? = null
    override val user: User
        get() = _user ?: User.EMPTY_USER


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCurrentUser(): Flow<User?> {
        return dataStore.data.map { value: Preferences ->
            value[PreferencesKeys.CURRENT_USER]
        }.flatMapLatest { currentUserId ->
            if (currentUserId != null) {
                usersDao.currentUser(currentUserId)
                    .map {
                        _user = it?.toUser() ?: User.EMPTY_USER
                        _user
                    }
            } else {
                _user = User.EMPTY_USER
                flowOf(_user)
            }
        }.flowOn(dispatcherIo)
    }

    //TODO move this method to AppCurrentUserRepo also save SignInMethod for signOut
    override suspend fun signOut() {
        auth.signOut()
    }

    override fun isSignedIn(): SignInStatus {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            SignInStatus.SignIn
        } else SignInStatus.SignOut
    }

}





