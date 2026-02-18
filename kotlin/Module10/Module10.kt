/**
 * Techlambda Training — Kotlin (INTERMEDIATE)
 * Module 10: Interfaces & Abstract Classes
 *
 * Audience: Working developers
 * Goal: Idiomatic Kotlin + production readiness
 *
 * WHAT:
 * - Interfaces in Kotlin (contracts)
 * - Abstract classes (shared base behavior + state)
 * - Default implementations in interfaces
 * - Interface vs abstract class (choosing correctly)
 * - Dependency Inversion (DIP) with practical design
 *
 * WHY:
 * - Production code needs flexibility + testability
 * - Interfaces enable swapping implementations (real vs mock)
 * - DIP prevents tight coupling to frameworks/databases/network
 *
 * WHEN:
 * - When building features that interact with IO (API/DB/file)
 * - When writing unit tests and clean architecture
 *
 * HOW:
 * - Build a small "Notification" feature:
 *   - NotificationSender (interface)
 *   - EmailSender, SmsSender (implementations)
 *   - NotificationService depends on interface (DIP)
 *   - Add abstract base for shared behavior (template-like)
 */

fun main() {
    println("=== Module 10: Interfaces & Abstract Classes (Techlambda) ===\n")

    interfacesBasics()
    defaultImplementationDemo()
    abstractClassDemo()
    interfaceVsAbstractDecision()
    dependencyInversionDemo()

    println("\n=== End of Module 10 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Interfaces Basics                                                       */
/* -------------------------------------------------------------------------- */

/**
 * WHAT: Interface = contract (what can be done), not how.
 * WHY: Helps swapping implementations easily (prod vs test).
 */
interface NotificationSender {
    fun send(to: String, message: String): SendResult
}

/**
 * Data class as value object for outcomes (idiomatic Kotlin).
 */
data class SendResult(val ok: Boolean, val provider: String, val error: String? = null)

class EmailSender : NotificationSender {
    override fun send(to: String, message: String): SendResult {
        // Simulating sending email
        return if (to.contains("@")) {
            SendResult(ok = true, provider = "EMAIL")
        } else {
            SendResult(ok = false, provider = "EMAIL", error = "Invalid email address")
        }
    }
}

class SmsSender : NotificationSender {
    override fun send(to: String, message: String): SendResult {
        // Simulating SMS - accept only digits length >= 10
        val ok = to.all { it.isDigit() } && to.length >= 10
        return if (ok) SendResult(true, "SMS") else SendResult(false, "SMS", "Invalid phone number")
    }
}

fun interfacesBasics() {
    println("1) Interfaces Basics")

    val email: NotificationSender = EmailSender()
    val sms: NotificationSender = SmsSender()

    println("- Email send => ${email.send("deepak@techlambda.com", "Welcome!")}")
    println("- Email send (bad) => ${email.send("not-an-email", "Welcome!")}")

    println("- SMS send => ${sms.send("9876543210", "OTP 1234")}")
    println("- SMS send (bad) => ${sms.send("98ABC", "OTP 1234")}\n")
}

/* -------------------------------------------------------------------------- */
/* 2) Default Implementations in Interfaces                                   */
/* -------------------------------------------------------------------------- */

/**
 * Kotlin interfaces can have method bodies (default implementations).
 * This is powerful for providing common helpers without forcing inheritance.
 */
interface Auditable {
    fun auditLabel(): String = "AUDIT" // default implementation

    fun audit(msg: String) {
        println("[${auditLabel()}] $msg")
    }
}

class EmailSenderWithAudit : NotificationSender, Auditable {
    override fun send(to: String, message: String): SendResult {
        audit("Sending EMAIL to=$to, message='$message'")
        return if (to.contains("@")) SendResult(true, "EMAIL") else SendResult(false, "EMAIL", "Invalid email")
    }
}

fun defaultImplementationDemo() {
    println("2) Default implementations in interfaces")

    val sender = EmailSenderWithAudit()
    val res = sender.send("deepak@techlambda.com", "Course starts Monday")
    println("- Result => $res\n")
}

/* -------------------------------------------------------------------------- */
/* 3) Abstract Classes (shared behavior/state)                                */
/* -------------------------------------------------------------------------- */

/**
 * WHAT: Abstract class can hold state + concrete methods + abstract methods.
 * WHY: Use when multiple implementations share core steps/state.
 *
 * This is a "template method" style base:
 * - validate -> deliver -> format result
 */
abstract class BaseSender(private val providerName: String) : NotificationSender {

    // shared state example (could be config / provider name)
    protected val provider: String = providerName

    final override fun send(to: String, message: String): SendResult {
        // shared algorithm (template)
        val validationError = validate(to, message)
        if (validationError != null) {
            return SendResult(ok = false, provider = provider, error = validationError)
        }

        // delegate the actual delivery to subclasses
        val ok = deliver(to, message)
        return if (ok) SendResult(true, provider) else SendResult(false, provider, "Delivery failed")
    }

    protected open fun validate(to: String, message: String): String? {
        if (to.isBlank()) return "Destination is blank"
        if (message.isBlank()) return "Message is blank"
        return null
    }

    protected abstract fun deliver(to: String, message: String): Boolean
}

class PushSender : BaseSender("PUSH") {
    override fun deliver(to: String, message: String): Boolean {
        // pretend "to" is deviceToken
        return to.startsWith("dev_") // very fake check
    }
}

fun abstractClassDemo() {
    println("3) Abstract class demo")

    val push: NotificationSender = PushSender()
    println("- Push send => ${push.send("dev_abc123", "Hi!")}")
    println("- Push send (bad token) => ${push.send("abc123", "Hi!")}\n")
}

/* -------------------------------------------------------------------------- */
/* 4) Interface vs Abstract Class                                             */
/* -------------------------------------------------------------------------- */

fun interfaceVsAbstractDecision() {
    println("4) Interface vs Abstract Class (decision guide)")

    println("Use INTERFACE when:")
    println("- You want a contract with multiple unrelated implementations")
    println("- You want composition (a class can implement many interfaces)")
    println("- You want testability via mocking/fakes\n")

    println("Use ABSTRACT CLASS when:")
    println("- You want shared state + shared algorithm steps")
    println("- You want to enforce a template flow (final send() calling deliver())")
    println("- You want partial implementation with protected helpers\n")
}

/* -------------------------------------------------------------------------- */
/* 5) Dependency Inversion (DIP) Demo                                         */
/* -------------------------------------------------------------------------- */

/**
 * BAD DESIGN (tight coupling):
 * class NotificationService {
 *   private val sender = EmailSender()  // hard-coded dependency
 * }
 *
 * PROBLEM:
 * - Can't swap SMS/Push easily
 * - Hard to unit-test (can't inject mock)
 *
 * GOOD DESIGN (DIP):
 * High-level module depends on abstraction (NotificationSender), not concrete classes.
 */
class NotificationService(private val sender: NotificationSender) {

    fun notifyUser(userId: Int, destination: String, msg: String): SendResult {
        // high-level logic: could add retries, logging, etc.
        if (userId <= 0) return SendResult(false, "SERVICE", "Invalid userId")
        return sender.send(destination, msg)
    }
}

/**
 * Fake sender for tests (no network/IO).
 */
class FakeSender(private val alwaysOk: Boolean) : NotificationSender {
    override fun send(to: String, message: String): SendResult {
        return if (alwaysOk) SendResult(true, "FAKE") else SendResult(false, "FAKE", "Simulated failure")
    }
}

fun dependencyInversionDemo() {
    println("5) Dependency Inversion (DIP) demo")

    // Production wiring (choose implementation)
    val serviceEmail = NotificationService(EmailSender())
    val serviceSms = NotificationService(SmsSender())

    println("- serviceEmail => ${serviceEmail.notifyUser(1, "deepak@techlambda.com", "Welcome")}")
    println("- serviceSms   => ${serviceSms.notifyUser(1, "9876543210", "OTP 4321")}")

    // Testing wiring (inject fake)
    val serviceTest = NotificationService(FakeSender(alwaysOk = true))
    println("- serviceTest(fake) => ${serviceTest.notifyUser(1, "anything", "test message")}\n")
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Create interface PaymentGateway { fun charge(amount:Int): Boolean }
 *    - Implement StripeGateway, MockGateway
 *    - Create PaymentService(gateway: PaymentGateway) (DIP)
 *
 * 2) Create abstract class BaseRepository<T>
 *    - final fun save(entity:T): Boolean { validate(); persist(); }
 *    - abstract fun persist(entity:T): Boolean
 *
 * 3) Explain:
 *    - Why interface helps unit testing?
 *    - Why abstract class helps enforcing common workflow?
 */
