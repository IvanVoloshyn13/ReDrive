package voloshyn.android.data.localeStorage.room.entities

import androidx.room.ColumnInfo

data class AccountTupleEntity(
    val id: String,
    val email: String,
  @ColumnInfo(name = "full_name")  val fullName: String
)