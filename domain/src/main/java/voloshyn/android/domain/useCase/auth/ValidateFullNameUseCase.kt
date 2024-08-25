package voloshyn.android.domain.useCase.auth

class ValidateFullNameUseCase {

    fun isValidFullName(fullName: String): Boolean {
        val names = fullName.split(" ").filter { it.isNotEmpty() }
        return names.size > 1
    }
}