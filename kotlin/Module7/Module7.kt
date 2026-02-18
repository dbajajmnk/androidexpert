/**
 * Techlambda Training — Kotlin Foundations (BASIC)
 * Module 7: Collections Basics
 *
 * WHAT this module covers:
 * 1) List, Set, Map
 * 2) Mutable vs Immutable collections
 * 3) Iteration patterns
 * 4) Collection operations (basic): filter, map, find, count, sorted
 *
 * WHY:
 * - Real apps are all about data: users, products, orders, messages
 * - Collections are how we store and transform that data
 *
 * WHEN:
 * - After functions & lambdas (Module 6) because many collection ops use lambdas
 *
 * HOW:
 * - Teach using a simple "students & courses" example
 */

fun main() {
    println("=== Module 7: Collections Basics (Techlambda Training) ===\n")

    listBasics()
    setBasics()
    mapBasics()
    iterationPatterns()
    basicCollectionOperations()
    commonMistakes()
    practiceTasks()

    println("\n=== End of Module 7 ===")
}

/**
 * WHAT:
 * List = ordered collection, allows duplicates
 *
 * WHY:
 * - Use when order matters (timeline, cart items, playlist)
 *
 * WHEN:
 * - Storing items in a sequence
 *
 * HOW:
 * Show immutable vs mutable list.
 */
fun listBasics() {
    println("1) List Basics")

    // Immutable list (read-only view)
    val topics = listOf("Kotlin", "Android", "Backend", "Kotlin")
    println("- listOf topics = $topics (ordered + duplicates allowed)")

    // Mutable list (can add/remove)
    val mutableTopics = mutableListOf("Kotlin", "Android")
    mutableTopics.add("Backend")
    mutableTopics.remove("Android")
    println("- mutableListOf after add/remove = $mutableTopics\n")
}

/**
 * WHAT:
 * Set = unique collection (no duplicates)
 *
 * WHY:
 * - Use when uniqueness matters (unique user IDs, tags)
 *
 * WHEN:
 * - You want to remove duplicates automatically
 *
 * HOW:
 * Show duplicate removal.
 */
fun setBasics() {
    println("2) Set Basics")

    val tags = setOf("kotlin", "android", "backend", "kotlin")
    println("- setOf tags = $tags (duplicates removed automatically)")

    val mutableTags = mutableSetOf("kotlin", "android")
    mutableTags.add("backend")
    mutableTags.add("kotlin") // no effect (already exists)
    println("- mutableSetOf after add = $mutableTags\n")
}

/**
 * WHAT:
 * Map = key-value pairs (dictionary)
 *
 * WHY:
 * - Fast lookup by key (userId -> user, productId -> product)
 *
 * WHEN:
 * - You want to find data using a unique identifier
 *
 * HOW:
 * Show immutable vs mutable map and safe lookups.
 */
fun mapBasics() {
    println("3) Map Basics")

    val courseFee = mapOf(
        "Kotlin" to 1999,
        "Android" to 2999,
        "Backend" to 3999
    )
    println("- mapOf courseFee = $courseFee")

    val kotlinFee = courseFee["Kotlin"] // may be null if key missing
    println("- courseFee['Kotlin'] = $kotlinFee")

    val unknownFee = courseFee["AI"] ?: 0
    println("- courseFee['AI'] ?: 0 => $unknownFee (safe default)\n")

    val mutableCourseFee = mutableMapOf("Kotlin" to 1999)
    mutableCourseFee["Android"] = 2999 // add/update
    mutableCourseFee["Kotlin"] = 1499  // update
    println("- mutableMap after updates = $mutableCourseFee\n")
}

/**
 * WHAT:
 * Ways to iterate collections
 *
 * WHY:
 * - Data processing is daily work in Android/backend
 *
 * WHEN:
 * - Rendering UI list, building response payload, summarizing data
 *
 * HOW:
 * Show for loop, forEach, withIndex.
 */
fun iterationPatterns() {
    println("4) Iteration Patterns")

    val students = listOf("Deepak", "Aman", "Ravi")

    print("- for loop: ")
    for (s in students) print("$s ")
    println()

    print("- forEach: ")
    students.forEach { print("$it ") }
    println()

    println("- withIndex:")
    for ((index, value) in students.withIndex()) {
        println("  index=$index, student=$value")
    }
    println()
}

/**
 * WHAT:
 * Basic collection operations (functional style)
 *
 * WHY:
 * - Kotlin encourages clean transformations:
 *   filter -> map -> sort -> compute
 *
 * WHEN:
 * - Anytime you need to manipulate data for UI/API
 *
 * HOW:
 * Use a real-ish dataset.
 */
fun basicCollectionOperations() {
    println("5) Basic Collection Operations")

    data class Student(val name: String, val marks: Int)

    val list = listOf(
        Student("Deepak", 78),
        Student("Aman", 92),
        Student("Ravi", 55),
        Student("Neha", 88)
    )

    // filter: keep only students with marks >= 80
    val top = list.filter { it.marks >= 80 }
    println("- filter marks>=80 => $top")

    // map: convert to names only
    val names = list.map { it.name }
    println("- map to names => $names")

    // find: first match or null
    val firstBelow60 = list.find { it.marks < 60 }
    println("- find marks<60 => $firstBelow60")

    // count: how many satisfy condition
    val passCount = list.count { it.marks >= 60 }
    println("- count marks>=60 => $passCount")

    // sortedBy / sortedByDescending
    val sortedAsc = list.sortedBy { it.marks }
    val sortedDesc = list.sortedByDescending { it.marks }
    println("- sortedBy marks asc => $sortedAsc")
    println("- sortedByDescending marks desc => $sortedDesc\n")
}

/**
 * Common mistakes beginners make.
 */
fun commonMistakes() {
    println("6) Common Mistakes (and fixes)")

    println("- Mistake: assuming listOf is editable")
    println("  Fix: use mutableListOf if you need add/remove.")

    println("- Mistake: forgetting that map[key] can return null")
    println("  Fix: use ?: default or check null.")

    println("- Mistake: using Set when you need ordering")
    println("  Fix: prefer List if order matters.")

    println("- Mistake: doing heavy loops inside UI render")
    println("  Fix: prepare data before rendering (architecture topic later).\n")
}

/**
 * Practice tasks for students.
 */
fun practiceTasks() {
    println("7) Practice Tasks (Student)")

    println("Task 1: Create listOf(1,2,3,4,5,6) and filter even numbers")
    println("Task 2: Convert listOf('a','bb','ccc') to lengths using map")
    println("Task 3: Create setOf with duplicates and observe duplicates removed")
    println("Task 4: Create a map of productName->price and safely read a missing key with ?:")
    println("Task 5: Use withIndex on a list of 3 cities and print index + city\n")
}
