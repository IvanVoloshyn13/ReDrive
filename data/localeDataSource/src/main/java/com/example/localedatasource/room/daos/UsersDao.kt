package com.example.localedatasource.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.localedatasource.room.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewUser(user: UserEntity)

    @Query("SELECT * from users WHERE id=:uUid")
    fun observeCurrentUser(uUid: String): Flow<UserEntity?>
}