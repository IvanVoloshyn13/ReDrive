package voloshyn.android.data.repository

import android.util.Patterns
import voloshyn.android.domain.repository.EmailValidatorRepository
import javax.inject.Inject

class AndroidEmailValidatorImpl @Inject constructor(

) : EmailValidatorRepository {
    override fun isValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}