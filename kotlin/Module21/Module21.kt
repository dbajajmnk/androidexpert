/**
 * Techlambda Training — Kotlin (ADVANCED)
 * Module 21: Testing & Quality
 *
 * Audience: Senior engineers / Architects
 *
 * WHAT:
 * - Unit testing with JUnit
 * - Mocking with MockK
 * - Parameterized tests
 * - Quality mindset
 *
 * WHY:
 * - Untested code is legacy code
 * - Testing enforces clean architecture
 */

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/* ============================================================
   Example Domain Logic
   ============================================================ */

data class User(val id: Int, val age: Int)

interface UserRepository {
    fun findUser(id: Int): User?
}

class UserService(private val repo: UserRepository) {

    fun isAdult(userId: Int): Boolean {
        val user = repo.findUser(userId)
            ?: throw IllegalArgumentException("User not found")

        return user.age >= 18
    }
}

/* ============================================================
   1) Unit Test with MockK
   ============================================================ */

class UserServiceTest {

    @Test
    fun `should return true when user is adult`() {
        val repo = mockk<UserRepository>()
        every { repo.findUser(1) } returns User(1, 25)

        val service = UserService(repo)

        val result = service.isAdult(1)

        assertTrue(result)
    }

    @Test
    fun `should return false when user is minor`() {
        val repo = mockk<UserRepository>()
        every { repo.findUser(2) } returns User(2, 15)

        val service = UserService(repo)

        val result = service.isAdult(2)

        assertFalse(result)
    }

    @Test
    fun `should throw exception when user not found`() {
        val repo = mockk<UserRepository>()
        every { repo.findUser(3) } returns null

        val service = UserService(repo)

        assertThrows(IllegalArgumentException::class.java) {
            service.isAdult(3)
        }
    }
}

/* ============================================================
   2) Parameterized Test
   ============================================================ */

class AgeValidationTest {

    @ParameterizedTest
    @CsvSource(
        "18, true",
        "25, true",
        "17, false",
        "10, false"
    )
    fun `age validation works correctly`(age: Int, expected: Boolean) {
        val isAdult = age >= 18
        assertEquals(expected, isAdult)
    }
}

/* ============================================================
   3) Quality Engineering Mindset
   ============================================================ */

/*
QUALITY PRINCIPLES:

1) Test behavior, not implementation
   ❌ Don't test private methods directly
   ✅ Test public contract

2) Use Dependency Injection
   - Makes mocking possible
   - Enables isolation

3) Avoid:
   - Sleeping in tests
   - Real DB calls
   - Real network calls

4) Code coverage:
   - Use IntelliJ coverage tool
   - Aim for meaningful coverage, not 100% blindly

5) Naming convention:
   should_return_true_when_condition_met()

6) Keep tests:
   - Fast
   - Deterministic
   - Independent
*/

/*
ADVANCED TESTING STRATEGIES:

- Test containers for integration testing
- Contract testing for APIs
- Mutation testing for robustness
- Property-based testing
*/

