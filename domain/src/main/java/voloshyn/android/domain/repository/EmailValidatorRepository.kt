package voloshyn.android.domain.repository

interface EmailValidatorRepository {

     fun isValidEmail(email: String):Boolean
}