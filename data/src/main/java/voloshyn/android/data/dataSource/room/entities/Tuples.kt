package voloshyn.android.data.dataSource.room.entities

import androidx.room.ColumnInfo

data class AccountTupleEntity(
    val id: String,
    val email: String,
  @ColumnInfo(name = "full_name")  val fullName: String
)