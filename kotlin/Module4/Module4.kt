/**
 * Techlambda Training — Kotlin Foundations (BASIC)
 * Module 4: Variables & Data Types
 *
 * WHAT this module covers:
 * 1) val vs var
 * 2) Type inference
 * 3) Primitive & reference types (JVM view + Kotlin view)
 * 4) Nullable vs non-null types
 * 5) Smart casting
 *
 * WHY:
 * - 80% beginner bugs come from wrong assumptions about variables + null
 * - Kotlin’s type system is a big reason it’s safer than Java
 *
 * WHEN:
 * - After students know how to run programs (Module 2) and structure code (Module 3)
 *
 * HOW:
 * - Teach with examples, prints, and “common mistakes” inside comments
 */

fun main() {
    println("=== Module 4: Variables & Data Types (Techlambda Training) ===\n")

    valVsVar()
    typeInference()
    primitivesVsReferencesJvmView()
    nullSafetyBasics()
    smartCastingDemo()

    println("\n=== End of Module 4 ===")
}

/**
 * WHAT:
 * - val = read-only reference (preferred)
 * - var = mutable reference (use only when needed)
 *
 * WHY:
 * - Immutable style reduces bugs, improves reasoning, helps concurrency later.
 *
 * WHEN:
 * - Whenever student writes a variable, they should ask: "Should this change?"
 *
 * HOW:
 * - Show mutation behavior with examples.
 */
fun valVsVar() {
    println("1) val vs var")

    val company = "Techlambda"
    // company = "Other" // ❌ compile error (val cannot be reassigned)
    println("- val example: company='$company' (cannot be reassigned)")

    var batchCount = 1
    batchCount += 1 // ✅ allowed
    println("- var example: batchCount=$batchCount (can be reassigned)\n")

    println("Important detail:")
    println("- val does NOT mean object is immutable.")
    println("- It means the reference cannot point to another object.\n")

    val list = mutableListOf("Kotlin", "Android")
    // list = mutableListOf("Backend") // ❌ not allowed (reassign)
    list.add("Backend") // ✅ allowed (mutating the object)
    println("val list can still be modified: $list\n")
}

/**
 * WHAT:
 * Type inference = Kotlin can guess the type from the assigned value.
 *
 * WHY:
 * - Cleaner code
 * - Still type-safe
 *
 * WHEN:
 * - Most of the time.
 * - Explicit types are useful in APIs, public functions, and tricky generics.
 *
 * HOW:
 * - Show inferred types + explicit types.
 */
fun typeInference() {
    println("2) Type Inference")

    val age = 30             // inferred Int
    val price = 99.99        // inferred Double
    val isActive = true      // inferred Boolean
    val name = "Deepak"      // inferred String

    println("- age=$age, price=$price, isActive=$isActive, name=$name")

    val score: Int = 100 // explicit
    val rating: Double = 4.5
    println("- explicit types: score=$score, rating=$rating\n")

    // Common mistake:
    // val x // ❌ Kotlin requires initialization OR explicit type with later assignment.
}

/**
 * WHAT:
 * Primitive vs reference types (Kotlin hides complexity)
 *
 * WHY:
 * - Kotlin runs on JVM, so primitives exist (int, boolean) for performance.
 * - Kotlin uses "Int", "Boolean", etc. but JVM may store them as primitives.
 *
 * WHEN:
 * - When teaching performance basics and interoperability later.
 *
 * HOW:
 * - Explain conceptually + show runtime class for boxed types.
 */
fun primitivesVsReferencesJvmView() {
    println("3) Primitive & Reference Types (JVM view)")

    val a: Int = 10
    val b: Int? = 10 // nullable Int becomes a boxed reference on JVM

    println("- a (Int) is usually stored as primitive on JVM for performance.")
    println("- b (Int?) must be stored as an object reference because it can be null.\n")

    // Showing class info (boxed types show as java.lang.Integer for nullable)
    println("Runtime type info (JVM):")
    println("- a::class = ${a::class}") // Kotlin reflection view
    println("- b?.javaClass = ${b?.javaClass}\n")

    println("Takeaway:")
    println("- Non-null numeric types are optimized.")
    println("- Nullable numeric types have extra overhead (minor, but real).\n")
}

/**
 * WHAT:
 * Null safety in Kotlin:
 * - String  => cannot be null
 * - String? => can be null
 *
 * WHY:
 * - Prevents NullPointerException by forcing safe handling at compile time.
 *
 * WHEN:
 * - Always. This is Kotlin’s superpower.
 *
 * HOW:
 * - Show safe-call (?.), Elvis (?:), and !! (dangerous).
 */
fun nullSafetyBasics() {
    println("4) Nullable vs Non-null Types")

    val nonNullName: String = "Techlambda"
    // val wrong: String = null // ❌ compile error
    println("- nonNullName='$nonNullName' (cannot be null)")

    val maybeEmail: String? = null
    println("- maybeEmail=$maybeEmail (can be null)")

    // Safe call
    val len1 = maybeEmail?.length
    println("- Safe call: maybeEmail?.length => $len1")

    // Elvis operator gives default when null
    val len2 = maybeEmail?.length ?: 0
    println("- Elvis: maybeEmail?.length ?: 0 => $len2")

    println("\nDanger zone:")
    println("- '!!' forces Kotlin to treat nullable as non-null. Can crash at runtime.")
    // val crashLen = maybeEmail!!.length // ❌ would throw NullPointerException
    println("- Avoid !! unless you are 100% sure and want fail-fast.\n")
}

/**
 * WHAT:
 * Smart casting = Kotlin automatically treats a variable as a more specific type
 * after type checks (is) and null checks.
 *
 * WHY:
 * - Removes manual casting (and casting bugs)
 * - Makes code safer and cleaner
 *
 * WHEN:
 * - Handling Any / mixed types, parsing input, handling nullable data
 *
 * HOW:
 * - Show Any type and is-check
 * - Show smart cast after null check
 */
fun smartCastingDemo() {
    println("5) Smart Casting")

    val value: Any = "Kotlin"
    if (value is String) {
        // inside this block, value is smart-cast to String
        println("- value is String, length=${value.length}")
        println("- uppercase=${value.uppercase()}")
    }

    val maybeNumber: String? = "123"
    if (maybeNumber != null) {
        // smart cast: maybeNumber becomes String here
        println("- maybeNumber is not null, length=${maybeNumber.length}")
    }

    println("\nCommon mistakes:")
    println("- Smart cast may NOT work if variable can change (var) or captured in lambda.")
    println("- Prefer val for easier smart casting.\n")
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Create:
 *    val city = "Amritsar"
 *    var temperature = 30
 *    Increase temperature by 5 and print.
 *
 * 2) Nullable challenge:
 *    val phone: String? = null
 *    Print phone length safely with default 0.
 *
 * 3) Smart cast challenge:
 *    val x: Any = 99
 *    If x is Int, print x + 1.
 *
 * 4) Boxed vs primitive:
 *    Create Int? and Int and print their javaClass.
 */
