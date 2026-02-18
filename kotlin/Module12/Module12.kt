/**
 * Techlambda Training — Kotlin (INTERMEDIATE)
 * Module 12: Advanced Null Safety & Exceptions
 *
 * Audience: Working developers
 * Goal: Production-ready defensive programming
 *
 * WHAT:
 * - Custom exceptions (domain-specific)
 * - Checked vs Unchecked (JVM view)
 * - Defensive programming (require, check, validate)
 * - Error propagation patterns (wrap, rethrow, map)
 *
 * WHY:
 * - Production systems fail at boundaries (API/DB/File/Network)
 * - Clear error modeling improves reliability + debuggability
 * - Kotlin doesn’t enforce checked exceptions — so design discipline matters
 *
 * WHEN:
 * - Building services, repositories, parsers, API handlers
 *
 * HOW:
 * - Build a small PaymentService
 * - Add validation + custom exceptions
 * - Show wrapping + propagation patterns
 */

fun main() {
    println("=== Module 12: Advanced Null Safety & Exceptions (Techlambda) ===\n")

    defensiveProgrammingDemo()
    customExceptionDemo()
    checkedVsUncheckedJvmView()
    errorPropagationPatterns()

    println("\n=== End of Module 12 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Defensive Programming                                                    */
/* -------------------------------------------------------------------------- */

fun defensiveProgrammingDemo() {
    println("1) Defensive Programming")

    try {
        val account = BankAccount(id = 1, balance = 500)
        account.withdraw(100)
        println("- Withdraw success, balance=${account.balance}")

        account.withdraw(1000) // should fail
    } catch (e: Exception) {
        println("- Caught exception: ${e.message}")
    }

    println("\nDefensive tools in Kotlin:")
    println("- require(condition) -> IllegalArgumentException")
    println("- check(condition)   -> IllegalStateException")
    println("- requireNotNull(value)")
    println("- checkNotNull(value)\n")
}

/**
 * Defensive example class
 */
class BankAccount(val id: Int, initialBalance: Int) {

    var balance: Int = initialBalance
        private set

    init {
        require(id > 0) { "Account id must be positive" }
        require(initialBalance >= 0) { "Initial balance cannot be negative" }
    }

    fun withdraw(amount: Int) {
        require(amount > 0) { "Withdraw amount must be positive" }

        if (amount > balance) {
            throw InsufficientBalanceException(balance, amount)
        }

        balance -= amount
    }
}

/* -------------------------------------------------------------------------- */
/* 2) Custom Exceptions                                                        */
/* -------------------------------------------------------------------------- */

/**
 * WHAT: Domain-specific exception
 * WHY: Clearer meaning than generic IllegalArgumentException
 * WHEN: Business rule violation
 */
class InsufficientBalanceException(
    val currentBalance: Int,
    val requestedAmount: Int
) : RuntimeException(
    "Insufficient balance. Current=$currentBalance, Requested=$requestedAmount"
)

fun customExceptionDemo() {
    println("2) Custom Exceptions")

    try {
        val account = BankAccount(id = 2, initialBalance = 100)
        account.withdraw(200)
    } catch (e: InsufficientBalanceException) {
        println("- Custom exception caught")
        println("  balance=${e.currentBalance}, requested=${e.requestedAmount}")
    }

    println()
}

/* -------------------------------------------------------------------------- */
/* 3) Checked vs Unchecked (JVM view)                                          */
/* -------------------------------------------------------------------------- */

fun checkedVsUncheckedJvmView() {
    println("3) Checked vs Unchecked (JVM view)")

    println("Java:")
    println("- Checked exceptions must be declared or caught")
    println("- Unchecked (RuntimeException) do not need explicit handling\n")

    println("Kotlin:")
    println("- No checked exception enforcement at language level")
    println("- All exceptions are effectively unchecked")
    println("- Design discipline is developer responsibility\n")

    println("Best practice:")
    println("- Throw RuntimeException for business rule violations")
    println("- Convert low-level exceptions to domain exceptions at boundaries\n")
}

/* -------------------------------------------------------------------------- */
/* 4) Error Propagation Patterns                                               */
/* -------------------------------------------------------------------------- */

fun errorPropagationPatterns() {
    println("4) Error Propagation Patterns")

    val service = PaymentService()

    try {
        service.processPayment(userId = 1, amount = 100)
        service.processPayment(userId = -1, amount = 100)
    } catch (e: PaymentException) {
        println("- PaymentException caught: ${e.message}")
    }

    println()
}

/**
 * High-level domain exception
 */
class PaymentException(message: String, cause: Throwable? = null)
    : RuntimeException(message, cause)

/**
 * Simulated low-level exception
 */
class GatewayException(message: String) : RuntimeException(message)

class PaymentService {

    fun processPayment(userId: Int, amount: Int) {
        require(userId > 0) { "Invalid userId" }
        require(amount > 0) { "Amount must be positive" }

        try {
            callPaymentGateway(userId, amount)
            println("- Payment successful for user=$userId")
        } catch (e: GatewayException) {
            // Wrap low-level exception into domain-level exception
            throw PaymentException("Payment failed for user=$userId", e)
        }
    }

    private fun callPaymentGateway(userId: Int, amount: Int) {
        // simulate gateway failure for demo
        if (userId < 0) {
            throw GatewayException("Gateway rejected request")
        }
    }
}

/* -------------------------------------------------------------------------- */
/* Additional Advanced Patterns (Conceptual)                                   */
/* -------------------------------------------------------------------------- */

/**
 * Error modeling alternative:
 * Instead of throwing exceptions, return sealed class Result
 */

sealed class PaymentResult {
    data class Success(val transactionId: String) : PaymentResult()
    data class Failure(val reason: String) : PaymentResult()
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Create custom exception:
 *    class InvalidOrderException(orderId:String)
 *
 * 2) Create OrderService.placeOrder(amount:Int)
 *    - require amount > 0
 *    - if amount > 5000 throw InvalidOrderException
 *
 * 3) Wrap low-level IOException into DomainException
 *
 * 4) Implement sealed class Result<T> with Success and Error
 */
