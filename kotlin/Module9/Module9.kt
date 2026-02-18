/**
 * Techlambda Training — Kotlin (INTERMEDIATE)
 * Module 9: Classes, Data Classes & Objects
 *
 * Audience: Working developers
 * Goal: Idiomatic Kotlin + production readiness
 *
 * WHAT:
 * - Classes & constructors (primary + secondary)
 * - data class (value modeling)
 * - object & singleton (one instance)
 * - companion object (factory/static-like)
 * - Equality & immutability (== vs ===, val design)
 *
 * WHY:
 * - Production code is mostly modeling + correctness + maintainability
 * - Kotlin encourages immutability + value objects (data class)
 * - Singleton/factory patterns are common in Android + backend
 *
 * WHEN:
 * - Right before interfaces/abstraction and module boundaries
 *
 * HOW:
 * - Use small, realistic domain: User + Session + Config + Factories
 */

fun main() {
    println("=== Module 9: Classes, Data Classes & Objects (Techlambda) ===\n")

    classesAndConstructors()
    dataClassAndEquality()
    objectSingletonDemo()
    companionObjectDemo()
    immutabilityMindset()

    println("\n=== End of Module 9 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Classes & Constructors                                                  */
/* -------------------------------------------------------------------------- */

fun classesAndConstructors() {
    println("1) Classes & Constructors")

    // Primary constructor + properties
    val u1 = User(id = 101, name = "Deepak")
    println("- User primary ctor => $u1")

    // Secondary constructor usage
    val u2 = User(name = "Aman") // uses secondary ctor
    println("- User secondary ctor => $u2")

    // Class with validation in init block
    val p1 = Product(sku = "SKU-001", price = 1999)
    println("- Product validated in init => $p1")

    // Uncomment to see validation failure
    // val bad = Product(sku = "", price = -10)

    println()
}

/**
 * WHAT: A normal Kotlin class with a primary constructor.
 * WHY: Model entities for app/backend.
 * WHEN: Anytime you represent a "thing".
 * HOW: Prefer val for immutability; expose minimal mutable state.
 */
class User(val id: Int, val name: String) {
    // Secondary constructor: convenience overload
    constructor(name: String) : this(id = IdGenerator.nextId(), name = name)

    override fun toString(): String = "User(id=$id, name=$name)"
}

/**
 * WHAT: init block runs right after primary constructor.
 * WHY: enforce invariants early (defensive programming).
 * WHEN: validate inputs for production correctness.
 */
class Product(val sku: String, val price: Int) {
    init {
        require(sku.isNotBlank()) { "SKU cannot be blank" }
        require(price >= 0) { "Price cannot be negative" }
    }

    override fun toString(): String = "Product(sku='$sku', price=$price)"
}

/* -------------------------------------------------------------------------- */
/* 2) data class + Equality                                                   */
/* -------------------------------------------------------------------------- */

fun dataClassAndEquality() {
    println("2) data class + Equality (== vs ===)")

    // data class auto-generates:
    // - equals(), hashCode()
    // - toString()
    // - copy()
    // - componentN() for destructuring

    val a = Student(id = 1, name = "Neha")
    val b = Student(id = 1, name = "Neha")

    println("- data class toString => $a")
    println("- a == b (structural equality) => ${a == b}")   // true
    println("- a === b (reference equality) => ${a === b}")  // false (different objects)

    // copy() creates a new object with changes
    val c = a.copy(name = "Neha Sharma")
    println("- copy(): a=$a")
    println("         c=$c")

    // Destructuring (component1/component2)
    val (id, name) = c
    println("- destructuring: id=$id, name=$name")

    println()
}

/**
 * WHY data class:
 * - Perfect for "value objects" / DTOs / API models / DB row models
 * - Makes equality correct by default (based on properties)
 */
data class Student(val id: Int, val name: String)

/* -------------------------------------------------------------------------- */
/* 3) object & Singleton                                                      */
/* -------------------------------------------------------------------------- */

fun objectSingletonDemo() {
    println("3) object & Singleton")

    // object => exactly one instance, created lazily on first access (generally)
    AppConfig.set("env", "prod")
    AppConfig.set("region", "ME")

    println("- AppConfig env => ${AppConfig.get("env")}")
    println("- AppConfig region => ${AppConfig.get("region")}")

    // Common production usage:
    // - Config store
    // - Logger
    // - Metrics collector
    // - Feature flags

    println()
}

/**
 * WHAT: Singleton object
 * WHY: one shared instance, globally accessible (use carefully)
 * WHEN: truly global shared state (config/logging) — but avoid business state
 * HOW: keep API small and state controlled
 */
object AppConfig {
    private val map = mutableMapOf<String, String>()

    fun set(key: String, value: String) {
        map[key] = value
    }

    fun get(key: String): String? = map[key]
}

/* -------------------------------------------------------------------------- */
/* 4) Companion Object (static-like) + Factory                                */
/* -------------------------------------------------------------------------- */

fun companionObjectDemo() {
    println("4) Companion Object + Factory")

    // Factory methods give control over creation
    val session = Session.createForUser(userId = 101)
    println("- Session factory => $session")

    // Access companion values like static constants
    println("- Session TTL => ${Session.DEFAULT_TTL_MINUTES} minutes")

    println()
}

class Session private constructor(
    val token: String,
    val userId: Int,
    val ttlMinutes: Int
) {
    override fun toString(): String =
        "Session(token='$token', userId=$userId, ttlMinutes=$ttlMinutes)"

    companion object {
        // constant-like config
        const val DEFAULT_TTL_MINUTES = 30

        /**
         * WHAT: Factory method
         * WHY: hide complex creation/validation/token generation
         * WHEN: session creation, DB connection builder, API client builder, etc.
         */
        fun createForUser(userId: Int, ttlMinutes: Int = DEFAULT_TTL_MINUTES): Session {
            require(userId > 0) { "userId must be positive" }
            val token = "tok_${userId}_${System.currentTimeMillis()}"
            return Session(token = token, userId = userId, ttlMinutes = ttlMinutes)
        }
    }
}

/* -------------------------------------------------------------------------- */
/* 5) Immutability + Production Mindset                                       */
/* -------------------------------------------------------------------------- */

fun immutabilityMindset() {
    println("5) Equality & Immutability mindset (production-ready)")

    // Prefer val properties in constructors for safety
    val order1 = Order(orderId = "ORD-1", items = listOf("Laptop", "Mouse"))
    println("- order1 => $order1")

    // If you need a modification, prefer copy() with data class
    val order2 = order1.copy(items = order1.items + "Keyboard")
    println("- order2 (copy) => $order2")

    // Avoid mutable shared state in singletons for business logic
    println("\nRule of thumb:")
    println("- Use data class for models/value objects")
    println("- Keep state immutable; create new values via copy()")
    println("- Use object singletons for cross-cutting concerns (config/logging), not business state")
    println("- Use companion object for factories/constants\n")
}

data class Order(
    val orderId: String,
    val items: List<String>
)

/* -------------------------------------------------------------------------- */
/* Utilities                                                                  */
/* -------------------------------------------------------------------------- */

/**
 * Example utility singleton: ID generator.
 * In real systems, IDs usually come from DB/UUID/service.
 */
object IdGenerator {
    private var last = 1000
    fun nextId(): Int = ++last
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Create data class Employee(id:Int, name:String, dept:String)
 *    - Create two equal employees and compare == and ===
 *
 * 2) Create class BankAccount(val id:Int, var balance:Int)
 *    - Add init { require(balance >= 0) }
 *    - Add method deposit(amount:Int)
 *
 * 3) Create object Logger with function log(msg:String)
 *
 * 4) Add companion object factory:
 *    - class ApiClient private constructor(baseUrl:String)
 *    - companion object { fun createProd(): ApiClient }
 */
