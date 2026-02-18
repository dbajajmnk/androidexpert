/**
 * Techlambda Training — Kotlin (INTERMEDIATE)
 * Module 16: REST APIs with Kotlin
 *
 * Audience: Working developers
 * Goal: Build JSON REST APIs with correct HTTP + production patterns
 *
 * WHAT:
 * - HTTP basics: method, path, headers, body
 * - REST principles: resource-based URIs, stateless, proper status codes
 * - API handlers: routing + request parsing + response shaping
 * - JSON APIs: ContentNegotiation + kotlinx.serialization
 * - Status codes: 200/201/204/400/404/409/500
 * - Intro Ktor: lightweight Kotlin server framework
 *
 * WHY:
 * - Every backend/mobile system talks via APIs
 * - Correct status codes + error shapes = production readiness
 *
 * WHEN:
 * - Building backend for Android app, microservices, admin panels, integrations
 *
 * HOW:
 * - Implement a clean CRUD API for a resource: Student
 * - In-memory repository (so students can run immediately)
 * - Add proper validation + consistent error response
 */

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

// ---------------------------
// Entry point: start server
// ---------------------------
fun main() {
    // WHAT: start embedded HTTP server
    // WHY: easiest local dev startup, no external container required
    // WHEN: local dev, training, small services
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

/**
 * Ktor convention: define module() for install plugins + routing.
 */
fun Application.module() {

    // JSON plugin: converts Kotlin objects <-> JSON automatically
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
        )
    }

    // Centralized error handling: consistent JSON errors
    install(StatusPages) {
        exception<ApiException> { call, e ->
            val status = e.status
            call.respond(status, ApiError(code = e.code, message = e.message ?: "error"))
        }
        exception<Throwable> { call, _ ->
            // production: log exception + traceId; return safe message
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiError(code = "internal_error", message = "Something went wrong")
            )
        }
    }

    val repo: StudentRepository = InMemoryStudentRepository()

    routing {
        // Health check (DevOps / readiness)
        get("/health") {
            call.respond(mapOf("ok" to true))
        }

        /**
         * REST resource: /students
         *
         * GET    /students         -> list
         * POST   /students         -> create
         * GET    /students/{id}    -> detail
         * PUT    /students/{id}    -> update
         * DELETE /students/{id}    -> delete
         */
        route("/students") {

            // GET /students?minMarks=80
            get {
                val minMarks = call.request.queryParameters["minMarks"]?.toIntOrNull()
                val list = repo.list(minMarks)
                call.respond(HttpStatusCode.OK, list)
            }

            // POST /students  (create)
            post {
                val req = call.receive<CreateStudentRequest>()

                validateName(req.name)
                validateMarks(req.marks)

                val created = repo.create(req)
                call.respond(HttpStatusCode.Created, created) // 201
            }

            // /students/{id}
            route("/{id}") {

                // GET /students/{id}
                get {
                    val id = parseId(call.parameters["id"])
                    val row = repo.get(id) ?: throw ApiException(
                        status = HttpStatusCode.NotFound,
                        code = "student_not_found",
                        message = "Student id=$id not found"
                    )
                    call.respond(HttpStatusCode.OK, row)
                }

                // PUT /students/{id} (replace/update)
                put {
                    val id = parseId(call.parameters["id"])
                    val req = call.receive<UpdateStudentRequest>()

                    req.name?.let(::validateName)
                    req.marks?.let(::validateMarks)

                    val updated = repo.update(id, req) ?: throw ApiException(
                        status = HttpStatusCode.NotFound,
                        code = "student_not_found",
                        message = "Student id=$id not found"
                    )

                    call.respond(HttpStatusCode.OK, updated)
                }

                // DELETE /students/{id}
                delete {
                    val id = parseId(call.parameters["id"])
                    val ok = repo.delete(id)
                    if (!ok) throw ApiException(
                        status = HttpStatusCode.NotFound,
                        code = "student_not_found",
                        message = "Student id=$id not found"
                    )

                    // 204 = no content (common REST convention)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}

/* ---------------------------------
   Models (Serializable for JSON)
---------------------------------- */

@Serializable
data class StudentDto(
    val id: Long,
    val name: String,
    val marks: Int
)

@Serializable
data class CreateStudentRequest(
    val name: String,
    val marks: Int
)

@Serializable
data class UpdateStudentRequest(
    val name: String? = null,
    val marks: Int? = null
)

@Serializable
data class ApiError(
    val code: String,
    val message: String
)

/* ---------------------------------
   Repository (Interface + Implementation)
---------------------------------- */

interface StudentRepository {
    fun list(minMarks: Int? = null): List<StudentDto>
    fun create(req: CreateStudentRequest): StudentDto
    fun get(id: Long): StudentDto?
    fun update(id: Long, req: UpdateStudentRequest): StudentDto?
    fun delete(id: Long): Boolean
}

class InMemoryStudentRepository : StudentRepository {
    private val seq = AtomicLong(1000)
    private val store = ConcurrentHashMap<Long, StudentDto>()

    override fun list(minMarks: Int?): List<StudentDto> {
        val all = store.values.sortedBy { it.id }
        return if (minMarks == null) all else all.filter { it.marks >= minMarks }
    }

    override fun create(req: CreateStudentRequest): StudentDto {
        val id = seq.incrementAndGet()
        val dto = StudentDto(id = id, name = req.name.trim(), marks = req.marks)
        store[id] = dto
        return dto
    }

    override fun get(id: Long): StudentDto? = store[id]

    override fun update(id: Long, req: UpdateStudentRequest): StudentDto? {
        val current = store[id] ?: return null
        val next = current.copy(
            name = req.name?.trim() ?: current.name,
            marks = req.marks ?: current.marks
        )
        store[id] = next
        return next
    }

    override fun delete(id: Long): Boolean = store.remove(id) != null
}

/* ---------------------------------
   Validation + Errors
---------------------------------- */

class ApiException(
    val status: HttpStatusCode,
    val code: String,
    override val message: String
) : RuntimeException(message)

fun parseId(raw: String?): Long {
    val id = raw?.toLongOrNull()
    if (id == null || id <= 0) {
        throw ApiException(
            status = HttpStatusCode.BadRequest,
            code = "invalid_id",
            message = "Invalid id: '$raw'"
        )
    }
    return id
}

fun validateName(name: String) {
    if (name.isBlank() || name.length < 2) {
        throw ApiException(
            status = HttpStatusCode.BadRequest,
            code = "invalid_name",
            message = "Name must be at least 2 characters"
        )
    }
}

fun validateMarks(marks: Int) {
    if (marks !in 0..100) {
        throw ApiException(
            status = HttpStatusCode.BadRequest,
            code = "invalid_marks",
            message = "Marks must be between 0 and 100"
        )
    }
}
