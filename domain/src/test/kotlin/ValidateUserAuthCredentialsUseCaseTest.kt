import com.example.domain.useCase.InputType
import com.example.domain.useCase.PasswordValidationState
import com.example.domain.useCase.ValidateUserAuthCredentialsUseCase
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class ValidateUserAuthCredentialsUseCaseTest {
    @Test
    fun `validatePassword returns valid result for a valid password`() {
        // Arrange: valid password meets all criteria
        val validPassword = "Abcdef1!"
        val result = ValidateUserAuthCredentialsUseCase(InputType.Password(validPassword))

        // Assert: the result should be valid
        assertTrue(result.isValid)

        // Assert: the detailed validation state should indicate all criteria are met
        val state = result.error as? PasswordValidationState
        assertNotNull(state)
        state?.let {
            assertTrue(it.hasValidLength)
            assertTrue(it.hasUpperCase)
            assertTrue(it.hasLowerCase)
            assertTrue(it.hasDigit)
            assertTrue(it.hasSpecialChar)
        }
    }

    @Test
    fun `validatePassword returns invalid result for password missing uppercase`() {
        // Arrange: password missing an uppercase letter
        val missingUpperCasePassword = "abcdef1!"
        val result = ValidateUserAuthCredentialsUseCase(InputType.Password(missingUpperCasePassword))

        // Assert: result should be invalid
        assertFalse(result.isValid)

        // Assert: validation state should indicate missing uppercase
        val state = result.error as? PasswordValidationState
        assertNotNull(state)
        state?.let {
            assertTrue(it.hasValidLength)
            assertFalse(it.hasUpperCase)
            assertTrue(it.hasLowerCase)
            assertTrue(it.hasDigit)
            assertTrue(it.hasSpecialChar)
        }
    }
}