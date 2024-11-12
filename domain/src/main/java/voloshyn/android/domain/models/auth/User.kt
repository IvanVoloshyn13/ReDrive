package voloshyn.android.domain.models.auth

data class User(
    val id:String="",
    val fullName: String,
    val email: String,

){
    companion object {
        val EMPTY_USER = User(
            id = "",
            fullName = "",
            email = "",
        )
    }
}


