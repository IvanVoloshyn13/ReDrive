package voloshyn.android.domain.models.tabs.profile

data class User(
    val fullName: String,
    val email: String,
    val password: String
)

data class UserTuple(
    val id: String,
    val fullName: String,
    val email: String,
) {
    companion object {
        val EMPTY_USER = UserTuple(
            id = "",
            fullName = "",
            email = "",
        )
    }
}
