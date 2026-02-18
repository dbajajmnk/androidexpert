/**
 * Techlambda Training — Kotlin (ADVANCED)
 * Module 19: Design Patterns in Kotlin
 *
 * Audience: Senior engineers / Architects
 *
 * WHAT:
 * - Singleton (Kotlin way)
 * - Factory
 * - Strategy (lambda-based)
 * - Observer (Flow-based)
 * - Avoiding Java-pattern misuse
 *
 * WHY:
 * - Patterns are tools, not religion
 * - Kotlin reduces boilerplate patterns
 *
 * WHEN:
 * - Designing scalable systems
 */

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    println("=== Module 19: Design Patterns in Kotlin ===\n")

    singletonDemo()
    factoryDemo()
    strategyDemo()
    observerDemo()

    println("\n=== End of Module 19 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Singleton (Kotlin way)                                                  */
/* -------------------------------------------------------------------------- */

object AppLogger {
    fun log(msg: String) {
        println("[LOG] $msg")
    }
}

fun singletonDemo() {
    println("1) Singleton")

    AppLogger.log("Application started")

    println("Why Kotlin object is better than Java Singleton:")
    println("- Thread-safe by default")
    println("- No double-checked locking")
    println("- No static boilerplate\n")
}

/* -------------------------------------------------------------------------- */
/* 2) Factory Pattern                                                         */
/* -------------------------------------------------------------------------- */

sealed class PaymentMethod {
    data class Card(val number: String) : PaymentMethod()
    data class UPI(val id: String) : PaymentMethod()
    object Cash : PaymentMethod()
}

object PaymentFactory {

    fun create(type: String): PaymentMethod =
        when (type.lowercase()) {
            "card" -> PaymentMethod.Card("0000-0000")
            "upi" -> PaymentMethod.UPI("user@upi")
            "cash" -> PaymentMethod.Cash
            else -> throw IllegalArgumentException("Unknown payment type")
        }
}

fun factoryDemo() {
    println("2) Factory")

    val method = PaymentFactory.create("card")
    println("- Created payment method: $method")

    println("Why sealed + when is powerful:")
    println("- Compiler forces exhaustive handling")
    println("- No need for complex factory hierarchies\n")
}

/* -------------------------------------------------------------------------- */
/* 3) Strategy Pattern (Kotlin way)                                           */
/* -------------------------------------------------------------------------- */

/*
Classic Java Strategy:
- Interface
- Multiple implementations
- Context class

Kotlin simplification:
- Use lambda
*/

class DiscountCalculator(private val strategy: (Double) -> Double) {
    fun apply(amount: Double): Double = strategy(amount)
}

fun strategyDemo() {
    println("3) Strategy")

    val festiveDiscount = DiscountCalculator { amount -> amount * 0.9 }
    val noDiscount = DiscountCalculator { amount -> amount }

    println("- Festive price: ${festiveDiscount.apply(100.0)}")
    println("- Normal price: ${noDiscount.apply(100.0)}")

    println("Lambda replaced full strategy class hierarchy\n")
}

/* -------------------------------------------------------------------------- */
/* 4) Observer Pattern (Modern Kotlin way)                                    */
/* -------------------------------------------------------------------------- */

fun observerDemo() = runBlocking {
    println("4) Observer (Flow-based)")

    val flow = MutableSharedFlow<String>()

    val job = launch {
        flow.collect {
            println("Observer received: $it")
        }
    }

    flow.emit("Event 1")
    flow.emit("Event 2")

    delay(200)
    job.cancel()

    println("Flow replaces classic observer interfaces\n")
}

/* -------------------------------------------------------------------------- */
/* Avoiding Java-pattern misuse                                               */
/* -------------------------------------------------------------------------- */

/*
ANTI-PATTERNS in Kotlin:

❌ Creating unnecessary Builder classes
→ Use default parameters

❌ Creating Singleton via companion object + lazy
→ Use object keyword

❌ Using inheritance-heavy design
→ Prefer composition + delegation

❌ Using Java-style Observer interface
→ Use Flow or Channel

❌ Using verbose Strategy classes
→ Use lambdas

Kotlin gives you:
- Higher-order functions
- Sealed classes
- Data classes
- Delegation
- Extension functions
*/

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Replace Strategy class hierarchy with lambda-based approach
 *
 * 2) Implement RateLimiter singleton
 *
 * 3) Create sealed class Result (Success/Error)
 *    and handle via exhaustive when
 *
 * 4) Convert classic observer to Flow-based solution
 */
