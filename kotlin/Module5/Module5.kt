/**
 * Techlambda Training — Kotlin Foundations (BASIC)
 * Module 5: Control Flow
 *
 * WHAT this module covers:
 * 1) if as expression (not just statement)
 * 2) when expression (switch upgrade)
 * 3) for, while, do-while loops
 * 4) Ranges (1..10, until, downTo, step)
 * 5) Common mistakes beginners make
 *
 * WHY:
 * - Control flow = decision + repetition => core of programming
 * - Kotlin adds safer and cleaner forms (if/when as expressions)
 *
 * WHEN:
 * - After variables/types (Module 4)
 *
 * HOW:
 * - Teach as "real-life decision making":
 *   - choose a path (if/when)
 *   - repeat tasks (loops)
 *   - iterate over a defined range (ranges)
 */

fun main() {
    println("=== Module 5: Control Flow (Techlambda Training) ===\n")

    ifAsExpressionDemo()
    whenExpressionDemo()
    loopsDemo()
    rangesDemo()
    commonMistakes()

    println("\n=== End of Module 5 ===")
}

/**
 * WHAT:
 * Kotlin's if can RETURN a value (expression).
 *
 * WHY:
 * In Java, developers often do:
 * int result; if (...) result=... else result=...
 * Kotlin can do it in one clean expression.
 *
 * WHEN:
 * Whenever you want to assign a value based on a condition.
 *
 * HOW:
 * Show both old-style and Kotlin expression-style.
 */
fun ifAsExpressionDemo() {
    println("1) if as expression")

    val marks = 72

    // Kotlin expression style (recommended)
    val grade = if (marks >= 90) "A"
    else if (marks >= 75) "B"
    else if (marks >= 60) "C"
    else "D"

    println("- marks=$marks => grade='$grade'")

    // Another example: compute discount
    val cartTotal = 1200
    val discount = if (cartTotal >= 1000) 0.10 else 0.0
    println("- cartTotal=$cartTotal => discount=${discount * 100}%\n")

    // Common note:
    // if as expression must cover all branches (else required when assigning)
}

/**
 * WHAT:
 * when = Kotlin’s powerful replacement for switch.
 *
 * WHY:
 * - Cleaner than multiple if-else chains
 * - Supports ranges, multiple matches, type checks
 * - Can return a value (expression)
 *
 * WHEN:
 * - Multi-branch decisions: menus, status, role, API response mapping, etc.
 *
 * HOW:
 * Show: exact match, multiple match, range match.
 */
fun whenExpressionDemo() {
    println("2) when expression")

    val dayNumber = 3

    // when as expression (returns value)
    val dayName = when (dayNumber) {
        1 -> "Mon"
        2 -> "Tue"
        3 -> "Wed"
        4 -> "Thu"
        5 -> "Fri"
        6, 7 -> "Weekend" // multiple matches
        else -> "Invalid"
    }
    println("- dayNumber=$dayNumber => dayName='$dayName'")

    // when with ranges
    val score = 88
    val level = when (score) {
        in 0..49 -> "Beginner"
        in 50..79 -> "Intermediate"
        in 80..100 -> "Advanced"
        else -> "Invalid"
    }
    println("- score=$score => level='$level'\n")

    // when with type checks (smart casting)
    val input: Any = "Kotlin"
    val description = when (input) {
        is String -> "It's a String of length ${input.length}"
        is Int -> "It's an Int value ${input + 1}"
        else -> "Unknown type"
    }
    println("- when(type): $description\n")
}

/**
 * WHAT:
 * Loops: for, while, do-while
 *
 * WHY:
 * - Repeat tasks
 * - Iterate collections and ranges
 *
 * WHEN:
 * - Iterating students list, processing API results, retry logic, etc.
 *
 * HOW:
 * Show each loop style with small example.
 */
fun loopsDemo() {
    println("3) Loops (for, while, do-while)")

    // FOR loop over a range
    print("- for loop (1..3): ")
    for (i in 1..3) {
        print("$i ")
    }
    println()

    // FOR loop over a list
    val topics = listOf("Kotlin", "Android", "Backend")
    print("- for loop over list: ")
    for (t in topics) {
        print("$t ")
    }
    println()

    // while loop (runs 0 or more times)
    var attempts = 0
    while (attempts < 3) {
        attempts++
    }
    println("- while loop: attempts=$attempts (stopped when attempts < 3 became false)")

    // do-while loop (runs at least once)
    var count = 0
    do {
        count++
    } while (count < 1)

    println("- do-while: count=$count (ran at least once)\n")
}

/**
 * WHAT:
 * Ranges in Kotlin: 1..5, until, downTo, step
 *
 * WHY:
 * - Very common in loops
 * - Avoid off-by-one errors when used correctly
 *
 * WHEN:
 * - Iteration needs a numeric boundary
 *
 * HOW:
 * Show different range types with output.
 */
fun rangesDemo() {
    println("4) Ranges")

    print("- 1..5 includes 5: ")
    for (i in 1..5) print("$i ")
    println()

    print("- 1 until 5 excludes 5: ")
    for (i in 1 until 5) print("$i ")
    println()

    print("- 5 downTo 1: ")
    for (i in 5 downTo 1) print("$i ")
    println()

    print("- 1..10 step 3: ")
    for (i in 1..10 step 3) print("$i ")
    println("\n")
}

/**
 * Common beginner mistakes + quick fixes.
 */
fun commonMistakes() {
    println("5) Common Mistakes (and fixes)")

    println("- Mistake: forgetting else when using if/when as an expression")
    println("  Fix: always provide else branch when assigning a value.")

    println("- Mistake: off-by-one errors with ranges")
    println("  Fix: use 'until' when the end should be excluded.")

    println("- Mistake: using == for floating comparisons blindly")
    println("  Fix: use tolerance check for doubles in real projects.")

    println("- Mistake: infinite while loop")
    println("  Fix: ensure the loop condition eventually becomes false.\n")
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) if expression:
 *    - Take age = 19
 *    - Assign category = "Adult" if age >= 18 else "Minor"
 *
 * 2) when:
 *    - Input monthNumber 1..12 and map to month name
 *
 * 3) ranges:
 *    - Print even numbers from 2 to 20 using step
 *
 * 4) loops:
 *    - Use while loop to simulate OTP retries:
 *      attempts max 3, print "Retry #x"
 */
