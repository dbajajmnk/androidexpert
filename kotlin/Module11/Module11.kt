/**
 * Techlambda Training — Kotlin (INTERMEDIATE)
 * Module 11: Packages, Modules & Visibility
 *
 * Audience: Working developers
 * Goal: Idiomatic Kotlin + production readiness
 *
 * WHAT:
 * - Package design (feature-based organization)
 * - Visibility modifiers: public, internal, protected, private
 * - Module boundaries (Gradle modules / library boundaries)
 * - API exposure rules (what to expose vs hide)
 * - Naming conventions (clean, scalable)
 *
 * WHY:
 * - Bad packaging leads to "god packages", tangled dependencies, and slow development
 * - Visibility is a tool for architecture enforcement
 * - Module boundaries matter for real Android/backend codebases
 *
 * WHEN:
 * - As soon as you start building multi-feature apps/APIs
 *
 * HOW:
 * - We'll simulate:
 *   - A small "Auth" feature with public API
 *   - Internal implementation hidden behind interfaces
 *   - Private helpers not visible outside file/class
 *
 * NOTE:
 * This is a single file demo, so we cannot physically create real packages/modules here.
 * But we teach the rules + show code patterns that you apply across folders/modules.
 */

fun main() {
    println("=== Module 11: Packages, Modules & Visibility (Techlambda) ===\n")

    packageDesignGuide()
    visibilityModifiersDemo()
    moduleBoundaryMindset()
    apiExposureRules()
    namingConventions()

    println("\n=== End of Module 11 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Package Design (Concept + Recommended structure)                         */
/* -------------------------------------------------------------------------- */

fun packageDesignGuide() {
    println("1) Package design (feature-first)")

    println("Recommended package style (feature-based):")
    println("- com.techlambda.app.auth")
    println("  - AuthService (public API)")
    println("  - AuthRepository (interface)")
    println("  - internal: TokenGenerator, AuthRepositoryImpl\n")

    println("Avoid (layer-only packages):")
    println("- com.techlambda.controller")
    println("- com.techlambda.service")
    println("- com.techlambda.repository")
    println("Why avoid? Features get scattered across layers, harder to refactor.\n")

    println("Rule of thumb:")
    println("- Organize by feature first (auth/payments/orders)")
    println("- Inside each feature, organize by layers if needed\n")
}

/* -------------------------------------------------------------------------- */
/* 2) Visibility Modifiers Demo                                                */
/* -------------------------------------------------------------------------- */

fun visibilityModifiersDemo() {
    println("2) Visibility modifiers (public/internal/protected/private)")

    // public is default in Kotlin (top-level)
    val service = AuthService()

    println("- login success => ${service.login(username = "deepak", password = "pass123")}")
    println("- login failure => ${service.login(username = "deepak", password = "wrong")}")

    println("\nVisibility meanings:")
    println("- public    : visible everywhere (default)")
    println("- internal  : visible within the same module (Gradle module)")
    println("- protected : visible in subclasses (class-level only)")
    println("- private   : visible only in the same file/class\n")

    // Demonstrate protected via inheritance
    val admin = AdminUser("Deepak", role = "ADMIN")
    println("- AdminUser.describe() => ${admin.describe()}\n")
}

/**
 * PUBLIC API: this is the class you want other features/modules to use.
 * Everything else should be hidden as internal/private when possible.
 */
class AuthService(
    // Dependency is an abstraction (DIP)
    private val repo: AuthRepository = DefaultAuthRepository()
) {
    fun login(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) return false

        // Use internal helper - hidden from other modules (concept)
        val hashed = internalHash(password)

        // Delegate to repository abstraction
        return repo.validate(username, hashed)
    }

    // private = visible only inside this class
    private fun internalHash(raw: String): String {
        // not a real hash; just teaching visibility
        return "hash_${raw.reversed()}"
    }
}

/**
 * Interface is typically part of public API if you want external implementations.
 * If it's only inside the module, keep it internal.
 */
interface AuthRepository {
    fun validate(username: String, hashedPassword: String): Boolean
}

/**
 * internal: visible only within the same module.
 * In real Gradle setup, this class would be hidden from other modules.
 */
internal class DefaultAuthRepository : AuthRepository {
    override fun validate(username: String, hashedPassword: String): Boolean {
        // Fake user store
        val expected = "hash_${"pass123".reversed()}"
        return (username == "deepak" && hashedPassword == expected)
    }
}

/**
 * protected demo: protected properties are visible to subclasses
 */
open class BaseUser(val name: String) {
    protected fun roleLabel(role: String): String = "Role=$role"
    open fun describe(): String = "User(name=$name)"
}

class AdminUser(name: String, private val role: String) : BaseUser(name) {
    override fun describe(): String = "Admin(name=$name, ${roleLabel(role)})"
}

/* -------------------------------------------------------------------------- */
/* 3) Module boundaries (Gradle modules)                                       */
/* -------------------------------------------------------------------------- */

fun moduleBoundaryMindset() {
    println("3) Module boundaries mindset (concept)")

    println("In real projects you create modules, e.g.:")
    println("- :core")
    println("- :auth")
    println("- :payments")
    println("- :app (android) or :api (backend)\n")

    println("Why modules matter:")
    println("- Faster builds (only rebuild changed module)")
    println("- Clear ownership of code")
    println("- Enforced boundaries (use internal to hide stuff)\n")

    println("Key rule:")
    println("- If you don't want other modules to use it -> mark it internal or keep it in implementation module.\n")
}

/* -------------------------------------------------------------------------- */
/* 4) API exposure rules                                                       */
/* -------------------------------------------------------------------------- */

fun apiExposureRules() {
    println("4) API exposure rules (production discipline)")

    println("Expose only what is needed:")
    println("- public: stable surface (services/interfaces/models that others must use)")
    println("- internal: implementation details inside module")
    println("- private: helper functions, utilities, constants\n")

    println("Practical rules:")
    println("- Public APIs should be small + stable")
    println("- Avoid exposing mutable state publicly")
    println("- Prefer interfaces for boundaries, data classes for models\n")
}

/* -------------------------------------------------------------------------- */
/* 5) Naming conventions                                                       */
/* -------------------------------------------------------------------------- */

fun namingConventions() {
    println("5) Naming conventions (clean code)")

    println("Packages:")
    println("- lower case, feature-based: com.techlambda.app.auth\n")

    println("Classes/Interfaces:")
    println("- PascalCase: AuthService, UserRepository")
    println("- Interface names should be roles: Logger, PaymentGateway\n")

    println("Functions/variables:")
    println("- camelCase: fetchUser(), isValid, retryCount\n")

    println("Constants:")
    println("- const val MAX_RETRY = 3 (UPPER_SNAKE_CASE)\n")

    println("Avoid:")
    println("- Utils, Helper, Common (be specific)")
    println("- Very long names (but clarity > shortness)\n")
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Create a feature package plan for:
 *    - orders, inventory, shipping
 *
 * 2) Create:
 *    - public class OrderService
 *    - internal class OrderRepositoryImpl
 *    - private function inside OrderService called normalizeId()
 *
 * 3) Explain:
 *    - Why "internal" is useful in multi-module projects?
 *    - Why keeping public API small helps maintainability?
 */
