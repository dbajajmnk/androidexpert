/**
 * Techlambda Training — Kotlin Foundations (BASIC)
 * Module 6: Functions & Lambdas
 *
 * WHAT this module covers:
 * 1) Function declaration
 * 2) Default parameters + Named parameters
 * 3) Expression functions (single-line)
 * 4) Lambda expressions
 * 5) Higher-order functions (intro) — functions that take functions
 *
 * WHY:
 * - Functions are the building blocks of reusable code
 * - Lambdas unlock modern Kotlin style (collections, callbacks, async)
 *
 * WHEN:
 * - After control flow (Module 5)
 *
 * HOW:
 * - Explain function patterns with real-life analogies:
 *   - A function is a "machine": input -> processing -> output
 */

fun main() {
    println("=== Module 6: Functions & Lambdas (Techlambda Training) ===\n")

    basicFunctionDemo()
    defaultAndNamedParamsDemo()
    expressionFunctionDemo()
    lambdaBasicsDemo()
    higherOrderFunctionIntro()

    commonMistakes()
    practiceMiniTasks()

    println("\n=== End of Module 6 ===")
}

/**
 * WHAT:
 * Basic function declaration with parameters and return type.
 *
 * WHY:
 * - Reuse logic
 * - Keep main() clean
 *
 * WHEN:
 * - Anytime code repeats
 *
 * HOW:
 * - Use simple math + string examples.
 */
fun basicFunctionDemo() {
    println("1) Function Declaration")

    val sum = add(10, 20)
    println("- add(10,20) = $sum")

    val message = greet("Deepak")
    println("- greet('Deepak') = $message\n")
}

fun add(a: Int, b: Int): Int {
    return a + b
}

fun greet(name: String): String {
    return "Hello, $name!"
}

/**
 * WHAT:
 * Default parameters + Named parameters
 *
 * WHY:
 * - Avoid function overloads (multiple same-name functions in Java)
 * - Make calls readable
 *
 * WHEN:
 * - When some inputs are optional (country, prefix, discount, pageSize)
 *
 * HOW:
 * - Demonstrate both default and named usage.
 */
fun defaultAndNamedParamsDemo() {
    println("2) Default & Named Parameters")

    // Default param used for country
    val a = formatUser(name = "Deepak")
    val b = formatUser(name = "Deepak", country = "India")

    println("- formatUser(name='Deepak') => $a")
    println("- formatUser(name='Deepak', country='India') => $b")

    // Named arguments improve clarity
    val c = buildUrl(
        host = "techlambda.com",
        path = "/blogs",
        secure = true,
        port = 443
    )
    println("- buildUrl(named params) => $c\n")
}

fun formatUser(name: String, country: String = "Unknown"): String {
    return "User(name=$name, country=$country)"
}

fun buildUrl(host: String, path: String, secure: Boolean = true, port: Int = 443): String {
    val scheme = if (secure) "https" else "http"
    return "$scheme://$host:$port$path"
}

/**
 * WHAT:
 * Expression functions (single-line functions)
 *
 * WHY:
 * - Cleaner for simple logic
 *
 * WHEN:
 * - When the function is small and readable
 *
 * HOW:
 * - Show examples.
 */
fun expressionFunctionDemo() {
    println("3) Expression Functions")

    println("- square(5) = ${square(5)}")
    println("- isEven(10) = ${isEven(10)}\n")
}

fun square(x: Int): Int = x * x
fun isEven(x: Int): Boolean = x % 2 == 0

/**
 * WHAT:
 * Lambda = function without a name (anonymous function).
 *
 * WHY:
 * - Used heavily in Kotlin: collections, callbacks, event handlers
 *
 * WHEN:
 * - When you pass behavior as an argument
 *
 * HOW:
 * - Show lambda creation and calling.
 */
fun lambdaBasicsDemo() {
    println("4) Lambda Basics")

    val multiply: (Int, Int) -> Int = { x, y -> x * y }
    println("- lambda multiply(3,4) = ${multiply(3, 4)}")

    val shout: (String) -> String = { s -> s.uppercase() + "!" }
    println("- lambda shout('kotlin') = ${shout("kotlin")}")

    // Lambdas with collections
    val nums = listOf(1, 2, 3, 4, 5)
    val evens = nums.filter { it % 2 == 0 } // it refers to each element
    println("- nums.filter { it%2==0 } => $evens\n")
}

/**
 * WHAT:
 * Higher-order function = a function that takes another function as parameter
 * or returns a function.
 *
 * WHY:
 * - Enables reusable behavior: logging, retry, validation, mapping rules
 *
 * WHEN:
 * - When you want customizable steps in a reusable pipeline
 *
 * HOW:
 * - We'll build a tiny "processor" that accepts an operation.
 */
fun higherOrderFunctionIntro() {
    println("5) Higher-Order Functions (Intro)")

    val result1 = operate(10, 5) { a, b -> a + b } // addition lambda
    val result2 = operate(10, 5) { a, b -> a - b } // subtraction lambda

    println("- operate(10,5,{+}) = $result1")
    println("- operate(10,5,{-}) = $result2")

    // Example: reusable wrapper for timing/logging
    val output = withLogging("uppercase conversion") {
        "techlambda".uppercase()
    }
    println("- withLogging => $output\n")
}

fun operate(a: Int, b: Int, op: (Int, Int) -> Int): Int {
    // op is a function parameter
    return op(a, b)
}

fun <T> withLogging(taskName: String, task: () -> T): T {
    println("[LOG] Starting: $taskName")
    val result = task()
    println("[LOG] Finished: $taskName")
    return result
}

/**
 * Common beginner mistakes + quick fixes.
 */
fun commonMistakes() {
    println("6) Common Mistakes (and fixes)")

    println("- Mistake: confusion between default params vs named params")
    println("  Fix: defaults define optional values; named params improve readability.")

    println("- Mistake: writing huge lambdas inside one line")
    println("  Fix: extract lambda into a named function or use multi-line lambda.")

    println("- Mistake: forgetting return type for complex function")
    println("  Fix: explicitly add ': Type' if compiler inference becomes unclear.")

    println("- Mistake: mixing Unit-returning lambdas and value-returning lambdas")
    println("  Fix: know whether your lambda returns a value or just performs action.\n")
}

/**
 * Practice section printed for students.
 */
fun practiceMiniTasks() {
    println("7) Practice Tasks (Student)")

    println("Task 1: Create function fullName(first:String, last:String='Kumar') -> String")
    println("Task 2: Call it using named args: fullName(last='Bajaj', first='Deepak')")

    println("Task 3: Create lambda: val triple: (Int) -> Int = { it * 3 }")
    println("Task 4: Use listOf(1,2,3).map(triple)")

    println("Task 5: Write higher-order function retry(times:Int, block:()->Boolean)")
    println("        It should stop early if block returns true.\n")
}
