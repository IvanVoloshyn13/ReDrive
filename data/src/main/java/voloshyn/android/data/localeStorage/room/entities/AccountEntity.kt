package voloshyn.android.data.localeStorage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseUser

@Entity(
    tableName = "accounts",
    indices = [
        Index("email", unique = true)
    ]
)
data class AccountEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "email", collate = ColumnInfo.NOCASE) val email: String,
    @ColumnInfo(name = "full_name") val fullName: String
) {

    companion object {
        fun toEntity(user: FirebaseUser): AccountEntity {
            return AccountEntity(
                id = user.uid,
                email = user.email ?: "null",
                fullName = user.displayName ?: "null"
            )
        }
    }
}