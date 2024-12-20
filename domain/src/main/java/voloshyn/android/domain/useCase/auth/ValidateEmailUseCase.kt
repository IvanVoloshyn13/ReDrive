package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.repository.EmailValidatorRepository

class ValidateEmailUseCase(private val repository: EmailValidatorRepository) {
    fun invoke(email: String): Boolean {
        return repository.isValid(email)
    }

}