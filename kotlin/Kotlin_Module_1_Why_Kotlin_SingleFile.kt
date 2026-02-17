/**
 * Kotlin_Module_1_Why_Kotlin_SingleFile.kt
 *
 * MODULE 1: WHY KOTLIN? (Kotlin-specific)
 *
 * This single file is designed for teaching:
 * - Kotlin history & philosophy (as engineering decisions)
 * - Safety: nullability & compiler-enforced contracts
 * - Conciseness: data classes, default/named params
 * - Interoperability: Kotlin + Java ecosystem mindset
 * - Mind-map in code (Real-life → Engineering → Kotlin feature)
 * - Mini labs at bottom
 *
 * Run (IntelliJ): Right click -> Run
 * Run (CLI):
 *   kotlinc Kotlin_Module_1_Why_Kotlin_SingleFile.kt -include-runtime -d app.jar
 *   java -jar app.jar
 */

// ------------------------------
// 0) "WHY KOTLIN" — IN COMMENTS
// ------------------------------
/*
Kotlin milestones (teaching timeline):
- 2011: JetBrains announces Kotlin (solve Java pain in large codebases)
- 2016: Kotlin 1.0 production-ready + stability guarantees
- 2017: Google announces Kotlin for Android
- 2018–2021: Coroutines stabilized; Multiplatform momentum
- 2022–till date: tooling/compiler improvements (K2 effort), enterprise maturity

Kotlin philosophy (engineering outcomes):
1) Safety by default (null safety, smart casts)
2) Conciseness without ambiguity (less boilerplate, still readable)
3) Interoperability (works with Java + JVM ecosystem)
4) Productivity for teams (easier review, fewer runtime bugs)
*/

// ------------------------------------------
// 1) REAL-LIFE → ENGINEERING → KOTLIN MIND MAP
// ------------------------------------------
/*
Real-life analogy:
- A form has mandatory fields (Name) and optional fields (Middle name).
Engineering abstraction:
- APIs have required vs optional fields. Ambiguity causes runtime crashes.
Kotlin feature:
- Non-null type = mandatory contract
- Nullable type (?) = optional contract
*/

data class CustomerProfile(
    val name: String,            // mandatory (non-null)
    val middleName: String?,      // optional (nullable)
    val email: String = "NA"      // default value reduces boilerplate
)

// ------------------------------------------
// 2) SAFETY BY DEFAULT (NULL SAFETY)
// ------------------------------------------
fun formatDisplayName(profile: CustomerProfile): String {
    // Safe call: ?.  (only runs if middleName != null)
    val middleInitial = profile.middleName?.firstOrNull()?.uppercaseChar()

    // Elvis operator ?:  (fallback when middleName is null)
    val middlePart = middleInitial?.let { " $it." } ?: ""

    // Compiler ensures profile.name is always non-null
    return profile.name + middlePart
}

// ------------------------------------------
// 3) SMART CASTS (LESS DEFENSIVE CODE)
// ------------------------------------------
sealed class PaymentMethod {
    data class Card(val last4: String) : PaymentMethod()
    data class UPI(val handle: String) : PaymentMethod()
    object Cash : PaymentMethod()
}

fun paymentLabel(method: PaymentMethod): String {
    // when + smart cast: inside each branch, method is automatically treated as the correct type
    return when (method) {
        is PaymentMethod.Card -> "Card ending ${method.last4}"
        is PaymentMethod.UPI -> "UPI ${method.handle}"
        PaymentMethod.Cash -> "Cash"
    }
}

// ------------------------------------------
// 4) CONCISENESS WITHOUT LOSS OF CLARITY
// ------------------------------------------
fun createProfile(
    name: String,
    middleName: String? = null,   // optional with default
    email: String = "NA"
): CustomerProfile {
    // Named args at call sites become self-documenting
    return CustomerProfile(name = name, middleName = middleName, email = email)
}

// ------------------------------------------
// 5) INTEROPERABILITY MINDSET (JAVA ECOSYSTEM)
// ------------------------------------------
/*
Kotlin is designed to coexist with Java:
- Calls Java libraries directly
- Generates JVM bytecode
- Runs anywhere Java runs

Interoperability habits:
- Keep public APIs explicit and stable
- Avoid exposing overly-clever Kotlin features in public APIs unless agreed
- Use clear naming & nullability contracts
*/
class EmailService {
    fun sendWelcomeEmail(toEmail: String): Boolean {
        if (toEmail.isBlank()) return false
        // Simulate sending
        return true
    }
}

// ------------------------------------------
// 6) "KOTLIN VS JAVA" — ENGINEERING COMPARISON IN CODE
// ------------------------------------------
/*
Java typical risk:
- null is allowed everywhere -> runtime NullPointerException.

Kotlin equivalent:
- non-null by default
- nullable must be explicitly marked with ?
This forces engineers to acknowledge risk early.
*/
fun safeEmailDomain(email: String?): String {
    // email can be null -> we must handle it explicitly
    val normalized = email?.trim()?.lowercase() ?: return "NO_EMAIL"
    return normalized.substringAfter("@", missingDelimiterValue = "NO_DOMAIN")
}

// ------------------------------------------
// 7) MINI LAB: "BAD VS GOOD" (TEACHING SNIPPETS)
// ------------------------------------------
fun badNullHandling(email: String?): String {
    // BAD: !! forces a crash if null (throws away Kotlin safety)
    // Use only if you're 100% sure (rare in production).
    // return email!!.lowercase()

    // Safe fallback:
    return email?.lowercase() ?: "unknown"
}

fun goodNullHandling(email: String?): String {
    // GOOD: safe, explicit, readable
    return email
        ?.trim()
        ?.lowercase()
        ?.takeIf { it.contains("@") }
        ?: "invalid_or_missing_email"
}

// ------------------------------------------
// 8) MAIN: Demonstrate Everything in One Run
// ------------------------------------------
fun main() {
    println("=== Kotlin Module 1: Why Kotlin (Single File Lab) ===")

    // 1) Mind-map demo: required vs optional fields
    val p1 = createProfile(name = "Deepak", middleName = "K", email = "deepak@example.com")
    val p2 = createProfile(name = "Aman", email = "aman@example.com") // middleName omitted

    println("DisplayName p1: ${formatDisplayName(p1)}")
    println("DisplayName p2: ${formatDisplayName(p2)}")

    // 2) Smart cast + when demo
    val method1: PaymentMethod = PaymentMethod.Card("1234")
    val method2: PaymentMethod = PaymentMethod.UPI("name@upi")
    println("Payment1: ${paymentLabel(method1)}")
    println("Payment2: ${paymentLabel(method2)}")

    // 3) Interoperability-friendly service API
    val emailService = EmailService()
    println("Welcome email sent: ${emailService.sendWelcomeEmail(p1.email)}")

    // 4) Null safety demo
    println("Email domain (null): ${safeEmailDomain(null)}")
    println("Email domain (valid): ${safeEmailDomain("Deepak@Example.com")}")

    // 5) Bad vs good handling
    println("Bad handling: ${badNullHandling(null)}")
    println("Good handling: ${goodNullHandling("  USER@DOMAIN.COM  ")}")

    println("\n=== End Module 1 Lab ===")
}

/*
-------------------------------------------
PRACTICE TASKS 
-------------------------------------------
1) Add a new optional field: phoneNumber: String?
   - Update createProfile and formatDisplayName accordingly.

2) Add a new PaymentMethod type: NetBanking(bankName: String)
   - Update paymentLabel() using smart casts.

3) Write a function:
   fun requireEmail(profile: CustomerProfile): String
   - If email is "NA" or invalid, return a clear message.

4) Create a small "before vs after" example:
   - Show how Kotlin null safety prevents runtime crashes.

5) Discuss in class:
   - Why Kotlin chose to avoid checked exceptions?
   - Why explicit nullability is better for large teams?
*/
