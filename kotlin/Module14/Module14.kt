/**
 * Techlambda Training — Kotlin (INTERMEDIATE)
 * Module 14: Coroutines – Basics
 *
 * Audience: Working developers
 * Goal: Understand coroutines for production async work
 *
 * WHAT:
 * - Why coroutines (lightweight async)
 * - suspend functions
 * - Coroutine scopes
 * - Dispatchers (Default / IO)
 * - Structured concurrency (intro)
 *
 * WHY:
 * - Production apps/backend do network + DB + file IO
 * - Threads are expensive; callbacks become messy
 * - Coroutines give clean async code with structured lifecycle
 *
 * WHEN:
 * - Any non-blocking work: API calls, DB queries, parallel tasks, retries
 *
 * HOW:
 * - Demo:
 *   1) Blocking vs coroutine delay
 *   2) suspend function example
 *   3) launch vs async
 *   4) Dispatcher usage
 *   5) Structured concurrency with coroutineScope
 */

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    println("=== Module 14: Coroutines Basics (Techlambda) ===\n")

    whyCoroutines()
    suspendFunctionDemo()
    launchVsAsyncDemo()
    dispatchersDemo()
    structuredConcurrencyIntro()

    commonMistakes()
    practiceTasks()

    println("\n=== End of Module 14 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Why Coroutines                                                          */
/* -------------------------------------------------------------------------- */

suspend fun whyCoroutines() {
    println("1) Why Coroutines?")

    println("Problem with blocking:")
    val t1 = measureTimeMillis {
        Thread.sleep(300) // blocks thread
        Thread.sleep(300)
    }
    println("- Blocking sleep total ~${t1}ms (thread is stuck)\n")

    println("Coroutine delay (non-blocking):")
    val t2 = measureTimeMillis {
        delay(300) // does NOT block underlying thread
        delay(300)
    }
    println("- delay total ~${t2}ms (coroutine suspends, thread can do other work)\n")

    println("Takeaway:")
    println("- Coroutines are lightweight tasks")
    println("- delay() suspends without blocking (Thread.sleep blocks)\n")
}

/* -------------------------------------------------------------------------- */
/* 2) suspend functions                                                       */
/* -------------------------------------------------------------------------- */

/**
 * WHAT: suspend fun can pause (suspend) without blocking the thread.
 * WHY: enables clean async without callbacks.
 * WHEN: network/db/file operations.
 */
suspend fun fetchUserFromApi(userId: Int): String {
    delay(200) // simulate network call
    return "user_$userId"
}

suspend fun suspendFunctionDemo() {
    println("2) suspend functions")

    val user = fetchUserFromApi(101)
    println("- fetchUserFromApi(101) => $user\n")
}

/* -------------------------------------------------------------------------- */
/* 3) launch vs async                                                         */
/* -------------------------------------------------------------------------- */

suspend fun launchVsAsyncDemo() {
    println("3) launch vs async")

    // launch: fire-and-forget (returns Job)
    val job = launch {
        delay(150)
        println("- launch: background work done")
    }

    // async: returns Deferred<T> for a result
    val deferred = async {
        delay(150)
        10 + 20
    }

    job.join()
    val result = deferred.await()
    println("- async result => $result\n")
}

/* -------------------------------------------------------------------------- */
/* 4) Dispatchers                                                             */
/* -------------------------------------------------------------------------- */

suspend fun dispatchersDemo() {
    println("4) Dispatchers (Default vs IO)")

    // Default: CPU-bound work (computations)
    val cpuResult = withContext(Dispatchers.Default) {
        heavyCpuWork()
    }
    println("- CPU work result => $cpuResult")

    // IO: blocking IO (files/db/network clients that block threads)
    val ioResult = withContext(Dispatchers.IO) {
        // simulate IO blocking code
        Thread.sleep(200) // in real code: file read / JDBC / blocking HTTP
        "io_done"
    }
    println("- IO work result => $ioResult\n")

    println("Rule of thumb:")
    println("- Dispatchers.Default -> CPU heavy work")
    println("- Dispatchers.IO      -> blocking IO work\n")
}

fun heavyCpuWork(): Long {
    // Fake CPU work
    var sum = 0L
    for (i in 1..2_000_00) sum += i
    return sum
}

/* -------------------------------------------------------------------------- */
/* 5) Structured Concurrency (intro)                                          */
/* -------------------------------------------------------------------------- */

suspend fun structuredConcurrencyIntro() {
    println("5) Structured concurrency (intro)")

    // coroutineScope ensures child coroutines complete before it returns.
    // If one fails, it can cancel siblings (depending on scope/supervisor usage).
    val totalTime = measureTimeMillis {
        coroutineScope {
            val a = async { slowTask("A", 250) }
            val b = async { slowTask("B", 350) }

            val resA = a.await()
            val resB = b.await()

            println("- Results: $resA, $resB")
        }
    }
    println("- Total time ~${totalTime}ms (ran concurrently)\n")

    println("Why structured concurrency matters:")
    println("- Prevents 'leaked' background jobs")
    println("- Parent scope controls child lifecycle")
    println("- Makes cancellation and error handling predictable\n")
}

suspend fun slowTask(name: String, ms: Long): String {
    delay(ms)
    return "task_$name_done"
}

/* -------------------------------------------------------------------------- */
/* Common mistakes + best practices                                           */
/* -------------------------------------------------------------------------- */

fun commonMistakes() {
    println("6) Common Mistakes (and fixes)")

    println("- Mistake: using Thread.sleep inside coroutines")
    println("  Fix: use delay() (or use Dispatchers.IO for blocking libraries).")

    println("- Mistake: GlobalScope.launch everywhere (leaks lifecycle)")
    println("  Fix: use structured scopes (runBlocking/coroutineScope/viewModelScope).")

    println("- Mistake: doing CPU heavy work on Dispatchers.IO")
    println("  Fix: use Dispatchers.Default for CPU tasks.")

    println("- Mistake: forgetting to await async() result")
    println("  Fix: call await() or use launch if you don’t need a result.\n")
}

/* -------------------------------------------------------------------------- */
/* Practice                                                                   */
/* -------------------------------------------------------------------------- */

fun practiceTasks() {
    println("7) Practice Tasks (Student)")

    println("Task 1: Write suspend fun fetchProduct(id:Int): String with delay(200)")
    println("Task 2: In coroutineScope, fetch 3 products in parallel using async, then print list")
    println("Task 3: Use withContext(Dispatchers.Default) to run a loop sum 1..1_000_000")
    println("Task 4: Explain: Why is GlobalScope dangerous in Android apps?\n")
}
