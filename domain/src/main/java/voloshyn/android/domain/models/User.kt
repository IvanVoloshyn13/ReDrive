package voloshyn.android.domain.models

data class User(
    val fullName: String,
    val email: String,
    val password: String
)

data class UserTuple(
    val fullName: String,
    val email: String ,
) {
    companion object {
        val EMPTY_USER = UserTuple(
            fullName = "",
            email = "",
        )
    }
}
