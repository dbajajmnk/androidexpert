/**
 * Techlambda Training — Kotlin (ADVANCED)
 * Module 17: Advanced Coroutines & Flow
 *
 * Audience: Senior engineers / Architects
 * Goal: Scalable async systems
 *
 * WHAT:
 * - Coroutine lifecycle
 * - Channels vs Flow
 * - Backpressure
 * - Cancellation
 * - Concurrency patterns
 *
 * WHY:
 * - Production systems are async by default
 * - Improper coroutine usage causes memory leaks & race conditions
 * - Flow enables reactive pipelines
 *
 * WHEN:
 * - Streaming APIs
 * - Messaging systems
 * - Event processing
 * - High-throughput backend services
 */

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    println("=== Module 17: Advanced Coroutines & Flow ===\n")

    coroutineLifecycleDemo()
    channelVsFlowDemo()
    backpressureDemo()
    cancellationDemo()
    concurrencyPatternsDemo()

    println("\n=== End of Module 17 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Coroutine Lifecycle                                                     */
/* -------------------------------------------------------------------------- */

suspend fun coroutineLifecycleDemo() {
    println("1) Coroutine Lifecycle")

    val job = launch {
        repeat(3) {
            delay(200)
            println("Working... $it")
        }
    }

    println("- Job active? ${job.isActive}")
    job.join()
    println("- Job completed? ${job.isCompleted}\n")
}

/*
Lifecycle states:
New -> Active -> Completing -> Completed
            ↘ Cancelled
*/

/* -------------------------------------------------------------------------- */
/* 2) Channel vs Flow                                                         */
/* -------------------------------------------------------------------------- */

suspend fun channelVsFlowDemo() {
    println("2) Channel vs Flow")

    println("Channel example (hot stream):")

    val channel = Channel<Int>()

    launch {
        for (i in 1..3) {
            channel.send(i)
        }
        channel.close()
    }

    for (value in channel) {
        println("- Channel received: $value")
    }

    println("\nFlow example (cold stream):")

    val flow = flow {
        for (i in 1..3) {
            emit(i)
        }
    }

    flow.collect { println("- Flow emitted: $it") }

    println("\nDifference:")
    println("- Channel = push-based, good for producer-consumer systems")
    println("- Flow = cold, reactive stream, declarative pipeline\n")
}

/* -------------------------------------------------------------------------- */
/* 3) Backpressure                                                            */
/* -------------------------------------------------------------------------- */

suspend fun backpressureDemo() {
    println("3) Backpressure (Flow buffer & slow consumer)")

    val time = measureTimeMillis {
        flow {
            for (i in 1..5) {
                emit(i)
                delay(100)
            }
        }
            .buffer() // allows producer to run ahead
            .collect {
                delay(200) // slow consumer
                println("Collected $it")
            }
    }

    println("- Total time with buffer: ${time}ms\n")
}

/*
Without buffer:
Producer waits for consumer.
With buffer:
Producer can emit ahead -> better throughput.
*/

/* -------------------------------------------------------------------------- */
/* 4) Cancellation                                                            */
/* -------------------------------------------------------------------------- */

suspend fun cancellationDemo() {
    println("4) Cancellation")

    val job = launch {
        repeat(10) {
            delay(200)
            println("Processing $it")
        }
    }

    delay(600)
    println("- Cancelling job")
    job.cancelAndJoin()
    println("- Cancelled successfully\n")
}

/*
Structured concurrency ensures children are cancelled
when parent is cancelled.
*/

/* -------------------------------------------------------------------------- */
/* 5) Concurrency Patterns                                                    */
/* -------------------------------------------------------------------------- */

suspend fun concurrencyPatternsDemo() {
    println("5) Concurrency Patterns")

    println("Parallel async example:")

    val time = measureTimeMillis {
        coroutineScope {
            val a = async { heavyTask("A") }
            val b = async { heavyTask("B") }

            println("- Results: ${a.await()}, ${b.await()}")
        }
    }

    println("- Total time (parallel): ${time}ms\n")

    println("Pattern Summary:")
    println("- async/await -> parallel tasks")
    println("- supervisorScope -> isolate failure")
    println("- channel -> worker pool")
    println("- flow -> streaming pipeline")
}

suspend fun heavyTask(name: String): String {
    delay(500)
    return "Task_$name"
}
