package voloshyn.android.domain.repository

interface EmailValidatorRepository {

     fun isValid(email: String):Boolean
}