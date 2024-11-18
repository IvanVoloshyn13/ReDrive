package voloshyn.android.data.localeStorage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import voloshyn.android.data.localeStorage.room.entities.AccountEntity


@Dao
interface AccountsDao {

    @Insert
    suspend fun addAccount(account: AccountEntity)

    @Query("SELECT * from accounts WHERE email=:email")
    suspend fun currentAccount(email: String): AccountEntity

}