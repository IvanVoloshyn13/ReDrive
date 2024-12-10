package voloshyn.android.data.localeStorage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.localeStorage.room.entities.UserEntity


@Dao
interface UsersDao {

    @Insert
    suspend fun addUser(user: UserEntity)

    @Query("SELECT * from users WHERE id=:userId")
     fun currentUser(userId: String): Flow<UserEntity?>

}