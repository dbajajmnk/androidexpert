/**
 * Techlambda Training — Kotlin (ADVANCED)
 * Module 22: Security Engineering
 *
 * WHAT:
 * - Secure coding practices
 * - Input validation
 * - Authentication basics
 * - Authorization patterns
 * - OWASP overview
 *
 * WHY:
 * - Security flaws destroy systems
 * - Architects must design defensively
 *
 * WHEN:
 * - Always
 */

import java.security.MessageDigest
import java.util.Base64

fun main() {
    println("=== Module 22: Security Engineering ===\n")

    inputValidationDemo()
    passwordHashingDemo()
    authenticationDemo()
    authorizationDemo()
    owaspOverview()

    println("\n=== End of Module 22 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Input Validation                                                        */
/* -------------------------------------------------------------------------- */

fun inputValidationDemo() {
    println("1) Input Validation")

    val inputEmail = "user@example.com"

    if (!isValidEmail(inputEmail)) {
        println("Invalid email")
    } else {
        println("Valid email")
    }

    println("\nBest practices:")
    println("- Validate format")
    println("- Validate length")
    println("- Reject unexpected fields")
    println("- Never trust client-side validation\n")
}

fun isValidEmail(email: String): Boolean {
    return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
}

/* -------------------------------------------------------------------------- */
/* 2) Password Hashing (Basic demo)                                           */
/* -------------------------------------------------------------------------- */

fun passwordHashingDemo() {
    println("2) Password Hashing")

    val password = "MySecurePassword123"

    val hash = hashPassword(password)

    println("Original: $password")
    println("Hashed (Base64 SHA-256): $hash\n")

    println("Production note:")
    println("- Never store raw passwords")
    println("- Use bcrypt / Argon2 (not plain SHA-256)")
    println("- Add salt\n")
}

fun hashPassword(password: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(password.toByteArray())
    return Base64.getEncoder().encodeToString(hashBytes)
}

/* -------------------------------------------------------------------------- */
/* 3) Authentication Basics                                                   */
/* -------------------------------------------------------------------------- */

data class User(val id: Int, val username: String, val role: String)

object AuthService {

    private val users = listOf(
        User(1, "admin", "ADMIN"),
        User(2, "user", "USER")
    )

    fun authenticate(username: String): User? {
        return users.find { it.username == username }
    }
}

fun authenticationDemo() {
    println("3) Authentication")

    val user = AuthService.authenticate("admin")

    if (user != null) {
        println("Authenticated user: ${user.username}")
    } else {
        println("Authentication failed")
    }

    println("\nAuthentication methods:")
    println("- Session-based")
    println("- JWT tokens")
    println("- OAuth2 / OpenID Connect")
    println("- Multi-factor authentication\n")
}

/* -------------------------------------------------------------------------- */
/* 4) Authorization Patterns                                                  */
/* -------------------------------------------------------------------------- */

fun authorizationDemo() {
    println("4) Authorization")

    val user = User(1, "admin", "ADMIN")

    if (hasAccess(user, requiredRole = "ADMIN")) {
        println("Access granted")
    } else {
        println("Access denied")
    }

    println("\nPatterns:")
    println("- Role-based access control (RBAC)")
    println("- Attribute-based access control (ABAC)")
    println("- Policy-based access control\n")
}

fun hasAccess(user: User, requiredRole: String): Boolean {
    return user.role == requiredRole
}

/* -------------------------------------------------------------------------- */
/* 5) OWASP Overview                                                          */
/* -------------------------------------------------------------------------- */

fun owaspOverview() {
    println("5) OWASP Top Risks Overview")

    println("Top Security Risks:")
    println("- Injection (SQL Injection)")
    println("- Broken Authentication")
    println("- Sensitive Data Exposure")
    println("- Broken Access Control")
    println("- Security Misconfiguration")
    println("- Cross-Site Scripting (XSS)")
    println("- Insecure Deserialization")
    println("- Using components with known vulnerabilities")
    println("- Insufficient logging & monitoring")
    println()

    println("Security Best Practices:")
    println("- Use PreparedStatement (avoid SQL injection)")
    println("- Hash + salt passwords")
    println("- Enforce HTTPS")
    println("- Validate input strictly")
    println("- Use proper logging & monitoring")
    println("- Keep dependencies updated\n")
}
