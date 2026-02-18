/**
 * Techlambda Training — Kotlin Foundations (BASIC)
 * Module 8: Null Safety & Basic Error Handling
 *
 * WHAT this module covers:
 * 1) Nullable types (String?)
 * 2) Safe calls (?.)
 * 3) Elvis operator (?:)
 * 4) try-catch (basic error handling)
 * 5) Why Kotlin avoids many NullPointerExceptions
 *
 * WHY:
 * - Null crashes are the #1 beginner and production issue in many apps.
 * - Kotlin’s type system forces you to handle null at compile-time.
 *
 * WHEN:
 * - Every time you deal with external data:
 *   user input, API response, DB values, JSON parsing, intent extras, etc.
 *
 * HOW:
 * - Show safe patterns first (?. and ?:)
 * - Show dangerous pattern (!!) and explain why to avoid
 * - Show try-catch for runtime errors (like parsing failures)
 */

fun main() {
    println("=== Module 8: Null Safety & Basic Error Handling (Techlambda Training) ===\n")

    nullableTypesDemo()
    safeCallDemo()
    elvisOperatorDemo()
    notNullAssertionWarning()
    tryCatchBasics()
    whyKotlinAvoidsNpe()
    commonMistakes()
    practiceTasks()

    println("\n=== End of Module 8 ===")
}

/**
 * WHAT:
 * Nullable types:
 * - String  (cannot be null)
 * - String? (can be null)
 *
 * WHY:
 * Kotlin forces clarity: can this value be missing or not?
 *
 * WHEN:
 * - API fields: middleName, phone, addressLine2
 * - Optional UI fields
 *
 * HOW:
 * Use ? only when null is possible/meaningful.
 */
fun nullableTypesDemo() {
    println("1) Nullable Types")

    val name: String = "Techlambda"
    // val wrong: String = null // ❌ compile error
    println("- name='$name' (non-null)")

    val phone: String? = null
    println("- phone=$phone (nullable)\n")
}

/**
 * WHAT:
 * Safe call operator: ?.  (if null, stops and returns null)
 *
 * WHY:
 * Avoids crash. No need for manual if-check every time.
 *
 * WHEN:
 * - Reading length, chaining calls: user?.address?.city
 *
 * HOW:
 * Demonstrate safe access.
 */
fun safeCallDemo() {
    println("2) Safe Calls (?.)")

    val email: String? = null
    val length = email?.length // null-safe access
    println("- email?.length => $length (no crash)\n")

    val actualEmail: String? = "hello@techlambda.com"
    val length2 = actualEmail?.length
    println("- actualEmail?.length => $length2\n")
}

/**
 * WHAT:
 * Elvis operator: ?:  (provide default when left side is null)
 *
 * WHY:
 * Most real code needs a fallback value.
 *
 * WHEN:
 * - UI display: name ?: "Guest"
 * - Missing map key: map[key] ?: 0
 *
 * HOW:
 * Show fallback patterns.
 */
fun elvisOperatorDemo() {
    println("3) Elvis Operator (?:)")

    val middleName: String? = null
    val displayName = middleName ?: "N/A"
    println("- middleName ?: 'N/A' => $displayName")

    val city: String? = "Amritsar"
    val displayCity = city ?: "Unknown"
    println("- city ?: 'Unknown' => $displayCity\n")
}

/**
 * WHAT:
 * Not-null assertion: !!
 *
 * WHY:
 * It can crash at runtime if value is null.
 * Kotlin allows it for "I am 100% sure" situations, but it’s risky.
 *
 * WHEN:
 * - Rarely. Use only when you want fail-fast and accept crash.
 *
 * HOW:
 * Show warning (we do NOT execute crash in training).
 */
fun notNullAssertionWarning() {
    println("4) '!!' Warning (Not-null assertion)")

    val token: String? = null
    println("- token is null. If we do token!! it will crash.")
    // val crash = token!!.length // ❌ would throw NullPointerException
    println("- Best practice: avoid !! in beginner + production code.\n")
}

/**
 * WHAT:
 * try-catch: handle runtime exceptions
 *
 * WHY:
 * Some errors cannot be prevented at compile-time (like parsing invalid input).
 *
 * WHEN:
 * - Parsing numbers, reading files, network operations, DB operations (later)
 *
 * HOW:
 * Demonstrate safe parsing with try-catch and better alternative.
 */
fun tryCatchBasics() {
    println("5) try-catch (Basic Error Handling)")

    val input1 = "123"
    val input2 = "12a" // invalid number

    println("- Parsing '$input1' => ${parseIntWithTryCatch(input1)}")
    println("- Parsing '$input2' => ${parseIntWithTryCatch(input2)}")

    // Better Kotlin alternative: toIntOrNull()
    println("\nBetter approach (no exception):")
    println("- '$input1'.toIntOrNull() => ${input1.toIntOrNull()}")
    println("- '$input2'.toIntOrNull() => ${input2.toIntOrNull()}\n")
}

fun parseIntWithTryCatch(text: String): Int {
    return try {
        text.toInt()
    } catch (e: NumberFormatException) {
        // fallback logic
        0
    }
}

/**
 * WHAT:
 * Why Kotlin avoids many NullPointerExceptions
 *
 * WHY:
 * In Java, every reference can be null.
 * In Kotlin, null must be explicit (String?).
 *
 * WHEN:
 * This understanding matters most in Android + backend data models.
 *
 * HOW:
 * Explain with a practical story.
 */
fun whyKotlinAvoidsNpe() {
    println("6) Why Kotlin avoids Null Pointer issues")

    println("Java mindset (danger):")
    println("- Any String can be null -> calling length can crash at runtime.")

    println("\nKotlin mindset (safe):")
    println("- Non-null types (String) cannot hold null => compiler protects you.")
    println("- Nullable types (String?) force you to handle missing values using:")
    println("  ?.  (safe call)")
    println("  ?:  (default/fallback)")
    println("  if (x != null) { } (smart cast)")
    println("  let { } (scope function, later)\n")
}

/**
 * Common mistakes and fixes.
 */
fun commonMistakes() {
    println("7) Common Mistakes (and fixes)")

    println("- Mistake: Using !! everywhere to silence compiler")
    println("  Fix: Use ?. and ?: instead.")

    println("- Mistake: Catching generic Exception always")
    println("  Fix: Catch specific exceptions when possible (NumberFormatException).")

    println("- Mistake: Using try-catch for normal flow")
    println("  Fix: Prefer safe conversions like toIntOrNull().")

    println("- Mistake: Treating null as 'bad' always")
    println("  Fix: Null can mean 'missing/optional' — handle it intentionally.\n")
}

/**
 * Practice tasks for students.
 */
fun practiceTasks() {
    println("8) Practice Tasks (Student)")

    println("Task 1: Create nullable var name:String? = null and print name ?: 'Guest'")
    println("Task 2: Create email:String? and print email?.length ?: 0")
    println("Task 3: Write function safeDivide(a:Int, b:Int): Int")
    println("        If b==0 return 0 else return a/b")
    println("Task 4: Parse listOf('10','x','30') to Ints using toIntOrNull() and replace null with 0\n")
}
