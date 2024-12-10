package voloshyn.android.data.repository


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.localeStorage.datastorePreferences.PreferencesKeys
import voloshyn.android.data.localeStorage.room.dao.UsersDao
import voloshyn.android.domain.models.auth.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppCurrentUserRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao,
    private val dataStore: DataStore<Preferences>,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher
) : AppCurrentUserRepository {
    private var _user: User = User.EMPTY_USER
    override val user: User
        get() = _user

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCurrentUser(): Flow<User> {
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

    override suspend fun setUserUid(uid: String) {
        withContext(dispatcherIo) {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.CURRENT_USER] = uid
            }
        }

    }

    override suspend fun clearUserUid() {
        withContext(dispatcherIo) {
            dataStore.edit { preferences ->
                preferences.remove(PreferencesKeys.CURRENT_USER)
            }
        }
    }

}