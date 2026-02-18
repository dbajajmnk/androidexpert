/**
 * Techlambda Training — Kotlin Foundations (BASIC)
 * Module 3: Program Structure & Entry Point
 *
 * WHAT this module covers:
 * 1) main() function (entry point)
 * 2) File structure (top-level functions, classes)
 * 3) Packages & imports (why they exist)
 * 4) Execution flow (top-level init vs main execution)
 * 5) Kotlin script vs application (concept)
 *
 * WHY:
 * - Students must know "where execution starts"
 * - Must understand how files, packages, and imports work before multi-file projects
 *
 * WHEN:
 * - Right after tooling is ready (Module 2)
 *
 * HOW:
 * - Demonstrate step-by-step prints:
 *   - What runs first?
 *   - What is top-level code?
 *   - How to organize code in packages?
 */

// ------------------------------
// (A) TOP-LEVEL VALUES / INIT
// ------------------------------
// This is a top-level property initializer.
// It runs when this file is first loaded by the JVM classloader (before main()).
private val bootMessage: String = run {
    println("[BOOT] File is being loaded -> top-level initializers execute now.")
    "Boot message ready"
}

// Another top-level value
private const val APP_NAME = "Techlambda Kotlin Training"

/**
 * ENTRY POINT:
 * main() is the start of program execution for a Kotlin/JVM application.
 *
 * NOTE:
 * Kotlin supports:
 * - fun main() { }
 * - fun main(args: Array<String>) { }
 */
fun main(args: Array<String>) {
    println("\n=== Module 3: Program Structure & Entry Point ===")
    println("App: $APP_NAME")
    println("bootMessage: $bootMessage")
    println("Args count: ${args.size}")
    if (args.isNotEmpty()) println("First arg: ${args[0]}")

    executionFlowDemo()
    fileStructureDemo()
    packagesAndImportsConcept()
    scriptVsApplicationConcept()

    println("\n=== End of Module 3 ===")
}

/**
 * WHAT:
 * Execution flow in Kotlin/JVM
 *
 * WHY:
 * Many beginners assume only main() runs.
 * But top-level initializers and object init blocks can run earlier.
 *
 * WHEN:
 * When debugging "why did this print before main?"
 *
 * HOW:
 * Show order clearly with prints.
 */
fun executionFlowDemo() {
    println("\n1) Execution Flow Demo")

    println("Order summary:")
    println("1) File loads -> top-level initializers run (BOOT)")
    println("2) main() runs")
    println("3) functions called from main run in order\n")

    // Object initialization demo
    println("Creating object TrainingSession() now...")
    val session = TrainingSession(topic = "Program Structure")
    session.start()
}

class TrainingSession(private val topic: String) {

    // init block runs when object is created
    init {
        println("[INIT] TrainingSession created for topic='$topic'")
    }

    fun start() {
        println("[START] Session started on '$topic'")
    }
}

/**
 * WHAT:
 * Kotlin file structure
 *
 * WHY:
 * Kotlin allows top-level functions and properties (unlike Java which requires a class).
 *
 * WHEN:
 * Before students start organizing modules in multiple files.
 *
 * HOW:
 * Show top-level function, class, and helper functions.
 */
fun fileStructureDemo() {
    println("\n2) File Structure Demo")

    // Top-level function call
    val total = addNumbers(10, 20)
    println("Top-level function addNumbers(10,20) = $total")

    // Top-level class usage
    val user = User(id = 101, name = "Deepak")
    println("Top-level class User => $user")

    // Companion object / static-like concept
    println("User.rules() => ${User.rules()}")
}

fun addNumbers(a: Int, b: Int): Int = a + b

data class User(val id: Int, val name: String) {
    companion object {
        fun rules(): String = "Keep code clean + safe + readable."
    }
}

/**
 * WHAT:
 * Packages & imports
 *
 * WHY:
 * - Package = folder-based namespace to avoid name conflicts
 * - Import = bring a type/function into scope without full name
 *
 * WHEN:
 * As soon as project has multiple files and folders.
 *
 * HOW:
 * Explain with examples in comments + common mistakes.
 */
fun packagesAndImportsConcept() {
    println("\n3) Packages & Imports (Concept)")

    println("In real projects, you'll see at the top of file:")
    println("package com.techlambda.kotlin.basics\n")
    println("And imports like:")
    println("import kotlin.math.max")
    println("import java.time.LocalDate\n")

    println("Why packages matter:")
    println("- Avoid naming conflicts (two classes named 'User')")
    println("- Organize by feature: auth/, payments/, orders/\n")

    println("Common mistakes:")
    println("- Wrong package name not matching folder structure")
    println("- Forgetting imports (IDE usually auto-adds)")
    println("- Creating everything in one package (becomes messy)\n")

    // Small live demo using a standard library function with full qualification
    val bigger = kotlin.math.max(10, 50)
    println("Example: kotlin.math.max(10,50) = $bigger")
}

/**
 * WHAT:
 * Kotlin script vs application
 *
 * WHY:
 * Students hear: .kts vs .kt
 *
 * WHEN:
 * When introducing automation or quick experiments.
 *
 * HOW:
 * Explain differences with simple mental model.
 */
fun scriptVsApplicationConcept() {
    println("\n4) Kotlin Script vs Application (Concept)")

    println("Kotlin Application (.kt):")
    println("- Needs main() as entry point (for JVM run)")
    println("- Usually part of a Gradle project")
    println("- Best for real apps (Android/backend)\n")

    println("Kotlin Script (.kts):")
    println("- Can run top-to-bottom like a script")
    println("- Useful for automation, small tasks, quick experiments")
    println("- Example: build.gradle.kts uses Kotlin script syntax\n")

    println("Rule of thumb:")
    println("- Learn .kt first (apps). Use .kts later for tooling.\n")
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Add a second file:
 *    - Create Module3_Helper.kt
 *    - Move addNumbers() there
 *    - Call it from this file (observe imports / package usage)
 *
 * 2) Add args:
 *    - Run with arguments in IntelliJ:
 *      Run Configurations -> Program arguments: hello techlambda
 *
 * 3) Add another init print:
 *    - Create another top-level val initializer and see order.
 */
