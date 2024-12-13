package voloshyn.android.data.localeStorage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.localeStorage.room.entities.UserEntity


@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: UserEntity)

    @Query("SELECT * from users WHERE id=:userId")
     fun currentUser(userId: String): Flow<UserEntity?>

    @Query("SELECT * from users WHERE email=:email")
    fun getUserByEmail(email: String): Flow<UserEntity?>

}