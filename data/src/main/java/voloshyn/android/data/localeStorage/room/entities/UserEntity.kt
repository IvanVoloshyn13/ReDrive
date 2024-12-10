package voloshyn.android.data.localeStorage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseUser
import voloshyn.android.domain.models.auth.User

@Entity(
    tableName = "users",
    indices = [
        Index("email", unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "email", collate = ColumnInfo.NOCASE) val email: String,
    @ColumnInfo(name = "full_name") val fullName: String
) {

    fun toUser(): User {
        return User(
            id = id,
            fullName = fullName,
            email = email
        )
    }


    companion object {
        fun toEntity(user: FirebaseUser): UserEntity {
            return UserEntity(
                id = user.uid,
                email = user.email ?: "null",
                fullName = user.displayName ?: "null"
            )
        }
    }
}