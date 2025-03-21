import com.example.domain.appResult.AppResult
import com.example.domain.appResult.AuthException
import com.example.domain.model.SignInStatus
import com.example.domain.repository.EmailAuthRepository
import com.example.domain.useCase.SignInWithEmailUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SignInWithEmailUseCaseTest {

    private lateinit var authRepository: EmailAuthRepository
    private lateinit var signInWithEmailUseCase: SignInWithEmailUseCase

    @Before
    fun setUp() {
        authRepository = mockk<EmailAuthRepository>()
        signInWithEmailUseCase = SignInWithEmailUseCase(authRepository)
    }

    @Test
    fun `should return success if email and password are correct`()= runTest {
        //Arrange
        val email = "test@example.com"
        val password = "123456"
        val expectedResult = AppResult.Success(SignInStatus.SignedIn)

        coEvery {
            authRepository.signInWithEmailAndPassword(
                email,
                password
            )
            expectedResult
        }

        // Act
        val result =signInWithEmailUseCase.invoke(email, password)

        // Assert
        assertEquals(expectedResult, result)

    }


    @Test
    fun `should return error when email or password are wrong`()= runTest {
        //Arrange
        val email = "wrong@example.com"
        val password = "wrongPassword"
        val expectedResult = AppResult.Error(AuthException.INVALID_PASSWORD)

        coEvery {
            authRepository.signInWithEmailAndPassword(
                email,
                password
            )
            expectedResult
        }

        // Act
        val result =signInWithEmailUseCase.invoke(email, password)

        // Assert
        assertEquals(expectedResult, result)

    }

    @Test
    fun `should return UserNotFound when email is wrong`()= runTest {
        //Arrange
        val email = "notFound@example.com"
        val password = "justPassword"
        val expectedResult = AppResult.Error(AuthException.USER_NOT_FOUND)

        coEvery {
            authRepository.signInWithEmailAndPassword(
                email,
                password
            )
            expectedResult
        }

        // Act
        val result =signInWithEmailUseCase.invoke(email, password)

        // Assert
        assertEquals(expectedResult, result)

    }



}