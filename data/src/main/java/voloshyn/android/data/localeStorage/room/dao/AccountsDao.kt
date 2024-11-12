package voloshyn.android.data.localeStorage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import voloshyn.android.data.localeStorage.room.entities.AccountEntity
import voloshyn.android.data.localeStorage.room.entities.AccountTupleEntity


@Dao
interface AccountsDao {

    @Insert
    suspend fun createAccount(account: AccountEntity)

    @Query("SELECT id,email,full_name from accounts WHERE email=:email")
    suspend fun currentAccount(email: String): AccountTupleEntity

}