/**
 * Techlambda Training — Kotlin (ADVANCED)
 * Module 18: Memory, JVM & Performance
 *
 * Audience: Senior engineers / Architects
 *
 * WHAT:
 * - JVM memory model
 * - Stack vs Heap
 * - Garbage Collection
 * - Kotlin performance tips
 * - Profiling basics
 *
 * WHY:
 * - Performance issues are rarely syntax problems
 * - Most production issues = memory pressure + object churn + blocking threads
 *
 * WHEN:
 * - High throughput systems
 * - Large Android apps
 * - Backend services under load
 *
 * HOW:
 * - Demonstrate memory allocation
 * - Show object churn
 * - Explain stack vs heap behavior
 * - Show performance comparison patterns
 */

import kotlin.system.measureTimeMillis

fun main() {
    println("=== Module 18: Memory, JVM & Performance ===\n")

    stackVsHeapDemo()
    objectAllocationDemo()
    garbageCollectionDemo()
    performanceTipsDemo()
    profilingBasics()

    println("\n=== End of Module 18 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Stack vs Heap                                                           */
/* -------------------------------------------------------------------------- */

fun stackVsHeapDemo() {
    println("1) Stack vs Heap")

    val a = 10          // primitive-like, likely stack allocated
    val b = "Kotlin"    // reference stored on stack, object on heap

    println("- Primitive value (a) stored efficiently")
    println("- String reference on stack, actual object on heap\n")

    explainStackVsHeap()
}

fun explainStackVsHeap() {
    println("Stack:")
    println("- Stores function calls, local variables")
    println("- Very fast allocation/deallocation")
    println("- LIFO (Last In First Out)")
    println()

    println("Heap:")
    println("- Stores objects")
    println("- Managed by Garbage Collector")
    println("- Slower than stack")
    println()
}

/* -------------------------------------------------------------------------- */
/* 2) Object Allocation & Object Churn                                        */
/* -------------------------------------------------------------------------- */

fun objectAllocationDemo() {
    println("2) Object Allocation & Churn")

    val time = measureTimeMillis {
        repeat(1_000_000) {
            val obj = Dummy(it) // many short-lived objects
        }
    }

    println("- Time creating 1M objects: ${time}ms")
    println("- High allocation rate increases GC pressure\n")
}

data class Dummy(val value: Int)

/*
High object churn:
- Frequent object creation
- Causes frequent minor GC
- Can reduce throughput
*/

/* -------------------------------------------------------------------------- */
/* 3) Garbage Collection                                                      */
/* -------------------------------------------------------------------------- */

fun garbageCollectionDemo() {
    println("3) Garbage Collection")

    val runtime = Runtime.getRuntime()

    val before = runtime.freeMemory()
    val list = mutableListOf<ByteArray>()

    repeat(50) {
        list.add(ByteArray(1_000_000)) // allocate ~1MB
    }

    val afterAlloc = runtime.freeMemory()
    println("- Memory free before allocation: $before")
    println("- Memory free after allocation:  $afterAlloc")

    list.clear()
    System.gc() // request GC (not guaranteed immediately)

    val afterGc = runtime.freeMemory()
    println("- Memory free after GC: $afterGc\n")

    println("GC Facts:")
    println("- Minor GC: cleans young generation")
    println("- Major GC: cleans old generation")
    println("- Frequent GC = performance problem\n")
}

/* -------------------------------------------------------------------------- */
/* 4) Kotlin Performance Tips                                                 */
/* -------------------------------------------------------------------------- */

fun performanceTipsDemo() {
    println("4) Kotlin Performance Tips")

    println("Tip 1: Prefer val over var")
    println("- Enables compiler optimizations\n")

    println("Tip 2: Avoid unnecessary object creation")
    println("- Use sequences for large chained operations")

    val list = (1..1_000_000).toList()

    val seqTime = measureTimeMillis {
        list.asSequence()
            .filter { it % 2 == 0 }
            .map { it * 2 }
            .toList()
    }

    val listTime = measureTimeMillis {
        list
            .filter { it % 2 == 0 }
            .map { it * 2 }
    }

    println("- Sequence time: $seqTime ms")
    println("- List eager time: $listTime ms")
    println()

    println("Tip 3: Use inline functions wisely")
    println("- Reduces lambda allocation overhead")

    println("Tip 4: Prefer primitive arrays (IntArray) over List<Int> when heavy numeric workload\n")
}

/* -------------------------------------------------------------------------- */
/* 5) Profiling Basics                                                        */
/* -------------------------------------------------------------------------- */

fun profilingBasics() {
    println("5) Profiling Basics")

    println("Tools:")
    println("- VisualVM")
    println("- JProfiler")
    println("- YourKit")
    println("- IntelliJ Profiler")
    println()

    println("What to check:")
    println("- CPU usage hotspots")
    println("- Heap usage")
    println("- GC frequency")
    println("- Thread contention")
    println()

    println("Golden rule:")
    println("- Measure before optimizing")
    println("- Premature optimization is root of all evil\n")
}
