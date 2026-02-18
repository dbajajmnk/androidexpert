/**
 * Techlambda Training — Kotlin Foundations (BASIC)
 * Module 2: Kotlin Tooling & Environment
 *
 * Audience: Beginners / College students / Android & Backend starters
 *
 * WHAT this module covers:
 * 1) JDK & JVM basics (plain English)
 * 2) IntelliJ IDEA setup (steps inside comments)
 * 3) Kotlin compiler idea (conceptual + tiny demo)
 * 4) Gradle overview (why build tools exist)
 * 5) Running Kotlin programs (IDE + CLI concept)
 *
 * WHY this module matters:
 * - Without tooling clarity, students get stuck on "setup errors" and stop learning.
 * - Understanding JVM + build flow makes Android/backend learning easier later.
 *
 * WHEN to teach:
 * - Before writing real Kotlin programs across modules.
 *
 * HOW we teach:
 * - Keep it simple, show the "build pipeline" like a factory:
 *   Kotlin code -> Compiler -> Bytecode -> JVM -> Output
 *
 * ------------------------------------------------------------
 * IntelliJ IDEA Setup (Beginner steps)
 * ------------------------------------------------------------
 * 1) Install JDK (recommended: 17 or 21)
 * 2) Install IntelliJ IDEA Community Edition
 * 3) New Project -> Kotlin -> (Gradle Kotlin/JVM)
 * 4) Ensure Project SDK points to your installed JDK
 * 5) Run main() to confirm everything works
 *
 * NOTE (Android path):
 * - For Android development, we later use Android Studio (also IntelliJ-based).
 */

fun main() {
    println("=== Module 2: Kotlin Tooling & Environment (Techlambda Training) ===\n")

    jdkAndJvmBasics()
    kotlinCompilerBasics()
    gradleOverview()
    howToRunKotlinPrograms()

    println("\n=== End of Module 2 ===")
}

/**
 * WHAT:
 * - JDK: Java Development Kit (tools to build/run JVM programs)
 * - JVM: Java Virtual Machine (the engine that runs bytecode)
 *
 * WHY:
 * - Kotlin runs on JVM (for Kotlin/JVM), so understanding JVM is foundational.
 * - Helps students understand why Java is still relevant under the hood.
 *
 * WHEN:
 * - Before Gradle and Android topics.
 *
 * HOW:
 * - We use the "kitchen" analogy.
 */
fun jdkAndJvmBasics() {
    println("1) JDK & JVM basics")

    println("Plain English:")
    println("- JVM = the 'engine' that RUNS your program (bytecode).")
    println("- JDK = the 'full toolkit' (compiler + tools + JVM) to BUILD and RUN programs.")
    println("- JRE (older term) = mainly RUN part (JVM + libs). Today JDK is used.\n")

    println("Analogy:")
    println("- Your Kotlin code = recipe")
    println("- Compiler = chef who converts recipe into cooking steps (bytecode)")
    println("- JVM = kitchen machine that executes those steps\n")

    // Real check: show the runtime environment details
    val javaVersion = System.getProperty("java.version")
    val vendor = System.getProperty("java.vendor")
    val os = System.getProperty("os.name") + " " + System.getProperty("os.version")

    println("Quick environment check (from JVM):")
    println("- Java version  : $javaVersion")
    println("- Java vendor   : $vendor")
    println("- Operating Sys : $os\n")
}

/**
 * WHAT:
 * Kotlin compiler translates Kotlin -> bytecode (for JVM target).
 *
 * WHY:
 * Students think Kotlin is "separate from Java world".
 * Reality: Kotlin produces JVM bytecode, so it can run anywhere JVM runs.
 *
 * WHEN:
 * Before students start writing multi-file projects.
 *
 * HOW:
 * Demonstrate compilation pipeline using a small "simulation" print.
 */
fun kotlinCompilerBasics() {
    println("2) Kotlin Compiler basics (conceptual)")

    println("Build pipeline:")
    println("Kotlin (.kt) -> Kotlin Compiler -> JVM Bytecode (.class) -> JVM runs -> Output\n")

    // Tiny demonstration: your Kotlin code becomes runnable logic on JVM
    val result = compileLikeKotlin("10 + 20")
    println("Mini demo (simulated): compileLikeKotlin('10 + 20') => $result\n")

    println("Reality check:")
    println("- In Android: Kotlin -> Bytecode -> DEX -> Android Runtime (ART)")
    println("- In Backend: Kotlin -> Bytecode -> JVM (same as Java apps)\n")
}

/**
 * This is NOT a real compiler.
 * It's just a teaching trick to show:
 * input (source idea) -> processing -> output
 */
fun compileLikeKotlin(expression: String): Int {
    // WHAT: simulate compilation of "10 + 20"
    // WHY: teach beginners that code is converted and executed
    // WHEN: whenever they ask "how does code run?"
    // HOW: parse extremely simple pattern

    val parts = expression.split("+").map { it.trim() }
    if (parts.size != 2) return 0
    val a = parts[0].toIntOrNull() ?: 0
    val b = parts[1].toIntOrNull() ?: 0
    return a + b
}

/**
 * WHAT:
 * Gradle is a build tool: it downloads dependencies, compiles, runs tests, packages apps.
 *
 * WHY:
 * - Real-world apps need libraries (networking, DB, UI, logging).
 * - Manually compiling and managing dependencies becomes impossible at scale.
 *
 * WHEN:
 * - As soon as students move from "single file" to "project".
 *
 * HOW:
 * - Explain using "construction project" analogy.
 */
fun gradleOverview() {
    println("3) Gradle Overview")

    println("Plain English:")
    println("- Gradle is like a project manager for your app build.")
    println("- It knows: which libraries to download, how to compile, how to run tests, how to package.\n")

    println("Analogy:")
    println("- Building an app is like constructing a building.")
    println("- Gradle manages the workers (tasks) and materials (dependencies).\n")

    println("Typical Gradle tasks you will hear (conceptual):")
    println("- build  : compile + test + package")
    println("- run    : execute the app (for JVM projects)")
    println("- test   : run unit tests")
    println("- clean  : remove old build output\n")

    println("Important note for students:")
    println("- You don't need to master Gradle today.")
    println("- You only need to understand WHY it exists.\n")
}

/**
 * WHAT:
 * How to run Kotlin programs in practical ways.
 *
 * WHY:
 * Students often get blocked by "I can't run it" issues.
 *
 * WHEN:
 * Every time we start a new module.
 *
 * HOW:
 * Provide IDE steps + CLI concept (without forcing CLI).
 */
fun howToRunKotlinPrograms() {
    println("4) Running Kotlin programs")

    println("Option A (Recommended for beginners): IntelliJ IDEA")
    println("- Open project -> open this file -> click Run (green triangle) near main()\n")

    println("Option B (Conceptual CLI flow):")
    println("- kotlinc file.kt -include-runtime -d app.jar")
    println("- java -jar app.jar\n")

    println("Option C (Later, Android Studio):")
    println("- Android projects run via Gradle tasks + Android Emulator/Device\n")

    println("Common setup mistakes (so students don’t panic):")
    println("- Wrong JDK selected in IDE (Project SDK)")
    println("- Multiple JDK versions causing confusion")
    println("- Gradle sync failed due to internet/proxy")
    println("- Antivirus locking Gradle cache\n")
}

/**
 * PRACTICE TASKS (for students)
 *
 * 1) Print your JVM details:
 *    - java.version
 *    - java.vendor
 *    - os.name
 *
 * 2) Modify compileLikeKotlin() to support:
 *    - "50 - 20"
 *
 * 3) In IntelliJ:
 *    - Change output text and rerun
 *    - Observe how fast the compile+run cycle feels
 */
