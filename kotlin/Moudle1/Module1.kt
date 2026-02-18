/**
 * Techlambda Training — Kotlin Foundations (BASIC)
 * Module 1: Why Kotlin?
 *
 * How to run:
 * 1) IntelliJ IDEA -> New Kotlin Project -> paste this file
 * 2) Run main()
 *
 * Goal of this file:
 * - Help student understand WHY Kotlin exists (history + philosophy)
 * - Show Kotlin vs Java difference with tiny examples
 * - Show where Kotlin fits (Android, Backend, Multiplatform)
 */

fun main() {
    println("=== Module 1: Why Kotlin? (Techlambda Training) ===\n")

    historyAndEvolution()
    kotlinPhilosophy()
    kotlinVsJavaEngineeringView()
    whereKotlinFits()

    println("\n=== End of Module 1 ===")
}

/**
 * WHAT:
 * Kotlin history in a very simple timeline.
 *
 * WHY:
 * Students must understand Kotlin is not “random new language”,
 * it was created to solve practical Java problems.
 *
 * WHEN:
 * When onboarding beginners, before syntax-heavy topics.
 *
 * HOW:
 * We show a small narrative + key milestones.
 */
fun historyAndEvolution() {
    println("1) History & Evolution")
    println("- Java dominated JVM + Android for years.")
    println("- Developers faced: boilerplate, null pointer crashes, slow iteration.")
    println("- JetBrains created Kotlin to improve productivity on JVM.")
    println("- Kotlin became official / primary language for Android development.\n")
}

/**
 * WHAT:
 * Kotlin philosophy = core design principles.
 *
 * WHY:
 * Philosophy helps students understand “why syntax looks like this”.
 *
 * WHEN:
 * Right after history, before writing real programs.
 *
 * HOW:
 * Use simple examples: null safety, conciseness, interoperability.
 */
fun kotlinPhilosophy() {
    println("2) Kotlin Philosophy (Safety, Conciseness, Interoperability)")

    // SAFETY (Null Safety)
    val name: String = "Techlambda"
    // val maybeName: String = null // ❌ Not allowed (compile-time safety)

    val maybeName: String? = null // ✅ explicitly nullable
    val safeLength = maybeName?.length ?: 0 // safe-call + Elvis operator (preview)
    println("- Safety: nullable types prevent many runtime crashes. safeLength=$safeLength")

    // CONCISENESS (Less boilerplate)
    val sum = add(10, 20) // short function usage
    println("- Conciseness: fewer lines, same meaning. add(10,20)=$sum")

    // INTEROPERABILITY (Kotlin works with Java ecosystem)
    println("- Interoperability: Kotlin runs on JVM and can use Java libraries.\n")
}

fun add(a: Int, b: Int): Int = a + b

/**
 * WHAT:
 * Kotlin vs Java from engineering view (not “fanboy” view).
 *
 * WHY:
 * Students often compare with Java; we show practical benefits.
 *
 * WHEN:
 * When student asks: “Why not just Java?”
 *
 * HOW:
 * Show 4 realistic differences with tiny code examples.
 */
fun kotlinVsJavaEngineeringView() {
    println("3) Kotlin vs Java (Engineering View)")

    // (A) Data / model classes:
    // Java: need constructor, getters, equals/hashCode, toString
    // Kotlin: data class auto-generates these
    data class Student(val id: Int, val name: String)

    val s1 = Student(1, "Deepak")
    val s2 = Student(1, "Deepak")
    println("- Data classes: s1 == s2 => ${s1 == s2} (easy equality + toString)")
    println("  Student toString => $s1")

    // (B) Immutability default style:
    val course = "Kotlin Foundations" // immutable
    var batchCount = 1                 // mutable only when needed
    batchCount += 1
    println("- Immutability-friendly: val for stable data, var only when needed. batchCount=$batchCount")

    // (C) Null safety (compile-time)
    val email: String? = null
    // println(email.length) // ❌ compile error in Kotlin (prevents crash)
    println("- Null safety: Kotlin forces safe handling of nullable values.")

    // (D) Extension functions (adds behavior without changing original class)
    val title = "techlambda training"
    println("- Extensions: title.capitalizeWords() => '${title.capitalizeWords()}'\n")
}

fun String.capitalizeWords(): String =
    this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { ch -> ch.uppercaseChar() }
    }

/**
 * WHAT:
 * Where Kotlin fits (Android, backend, multiplatform).
 *
 * WHY:
 * Students should see career path: Kotlin is not only Android.
 *
 * WHEN:
 * At course start to motivate learners.
 *
 * HOW:
 * Show simple “mental mapping” of usage, not frameworks yet.
 */
fun whereKotlinFits() {
    println("4) Where Kotlin Fits")

    println("- Android: UI, business logic, networking, local DB, app architecture")
    println("- Backend: APIs, microservices (Spring/Ktor), clean domain models, coroutines for async")
    println("- Multiplatform: share business logic across Android + iOS + Desktop + Web")
    println("- Scripting/Tools: Kotlin scripts for automation (build/dev tasks)\n")

    println("Mini reality check:")
    println("If your goal is JOB:")
    println("  Path A: Kotlin -> Android -> Clean Architecture -> APIs -> Production apps")
    println("  Path B: Kotlin -> Backend -> REST/GraphQL -> Databases -> Microservices")
    println("  Path C: Kotlin -> Multiplatform -> Shared modules -> Mobile + Desktop\n")
}
