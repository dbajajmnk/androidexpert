/**
 * Techlambda Training — Kotlin (INTERMEDIATE)
 * Module 15: Database Basics
 *
 * Audience: Working developers
 * Goal: Production readiness with DB fundamentals
 *
 * WHAT:
 * - JDBC basics (Connection, Statement, PreparedStatement, ResultSet)
 * - CRUD operations
 * - Transactions (commit/rollback)
 * - Error handling patterns
 * - ORM overview (JPA/Exposed) concept
 *
 * WHY:
 * - Backend services and many Android apps need persistence
 * - Understanding JDBC teaches the true fundamentals underneath ORMs
 *
 * WHEN:
 * - Building APIs, microservices, reporting, enterprise apps
 *
 * HOW:
 * - Use H2 in-memory database (fast, no install needed)
 * - Create table, insert, read, update, delete
 * - Demonstrate transaction with forced failure and rollback
 */

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

fun main() {
    println("=== Module 15: Database Basics (Techlambda) ===\n")

    println("DB choice for training: H2 (in-memory) => no installation needed.\n")

    val jdbcUrl = "jdbc:h2:mem:techlambda;DB_CLOSE_DELAY=-1"
    val user = "sa"
    val pass = ""

    Database.connect(jdbcUrl, user, pass).use { conn ->
        println("Connected: ${conn.metaData.databaseProductName} ${conn.metaData.databaseProductVersion}\n")

        createSchema(conn)

        val repo = StudentRepository(conn)

        val id1 = repo.createStudent(name = "Deepak", marks = 78)
        val id2 = repo.createStudent(name = "Neha", marks = 92)
        println("Inserted students: id1=$id1, id2=$id2\n")

        println("Read by id:")
        println("- $id1 => ${repo.getStudentById(id1)}")
        println("- $id2 => ${repo.getStudentById(id2)}\n")

        println("Read all:")
        repo.getAllStudents().forEach { println("- $it") }
        println()

        println("Update:")
        repo.updateMarks(id2, 95)
        println("- After update id2 => ${repo.getStudentById(id2)}\n")

        println("Delete:")
        repo.deleteStudent(id1)
        println("- After delete, all students:")
        repo.getAllStudents().forEach { println("- $it") }
        println()

        transactionDemo(conn)

        ormOverview()
    }

    println("\n=== End of Module 15 ===")
}

/* -------------------------------------------------------------------------- */
/* JDBC Connection Helper                                                     */
/* -------------------------------------------------------------------------- */

object Database {
    /**
     * WHAT: Open JDBC connection
     * WHY: This is the entry to DB world
     * WHEN: every DB operation needs a connection
     */
    fun connect(url: String, user: String, pass: String): Connection {
        return DriverManager.getConnection(url, user, pass)
    }
}

/* -------------------------------------------------------------------------- */
/* Schema                                                                     */
/* -------------------------------------------------------------------------- */

fun createSchema(conn: Connection) {
    println("Creating schema...")

    val sql = """
        CREATE TABLE IF NOT EXISTS students (
          id IDENTITY PRIMARY KEY,
          name VARCHAR(100) NOT NULL,
          marks INT NOT NULL
        );
    """.trimIndent()

    conn.createStatement().use { st ->
        st.execute(sql)
    }

    println("Schema ready.\n")
}

/* -------------------------------------------------------------------------- */
/* Data Model                                                                 */
/* -------------------------------------------------------------------------- */

data class StudentRow(val id: Long, val name: String, val marks: Int)

/* -------------------------------------------------------------------------- */
/* Repository: CRUD using PreparedStatement                                   */
/* -------------------------------------------------------------------------- */

class StudentRepository(private val conn: Connection) {

    /**
     * CREATE (Insert)
     *
     * WHY PreparedStatement:
     * - Prevents SQL injection
     * - Handles parameters safely
     */
    fun createStudent(name: String, marks: Int): Long {
        require(name.isNotBlank()) { "name cannot be blank" }
        require(marks in 0..100) { "marks must be 0..100" }

        val sql = "INSERT INTO students(name, marks) VALUES (?, ?)"
        conn.prepareStatement(sql, arrayOf("id")).use { ps ->
            ps.setString(1, name)
            ps.setInt(2, marks)
            ps.executeUpdate()

            ps.generatedKeys.use { rs ->
                if (rs.next()) return rs.getLong(1)
            }
        }
        throw SQLException("Failed to insert student (no generated id)")
    }

    /**
     * READ by id
     */
    fun getStudentById(id: Long): StudentRow? {
        val sql = "SELECT id, name, marks FROM students WHERE id = ?"
        conn.prepareStatement(sql).use { ps ->
            ps.setLong(1, id)
            ps.executeQuery().use { rs ->
                return if (rs.next()) rs.toStudentRow() else null
            }
        }
    }

    /**
     * READ all
     */
    fun getAllStudents(): List<StudentRow> {
        val sql = "SELECT id, name, marks FROM students ORDER BY id"
        conn.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                val out = mutableListOf<StudentRow>()
                while (rs.next()) out.add(rs.toStudentRow())
                return out
            }
        }
    }

    /**
     * UPDATE
     */
    fun updateMarks(id: Long, newMarks: Int): Boolean {
        require(newMarks in 0..100) { "marks must be 0..100" }

        val sql = "UPDATE students SET marks = ? WHERE id = ?"
        conn.prepareStatement(sql).use { ps ->
            ps.setInt(1, newMarks)
            ps.setLong(2, id)
            val updated = ps.executeUpdate()
            return updated == 1
        }
    }

    /**
     * DELETE
     */
    fun deleteStudent(id: Long): Boolean {
        val sql = "DELETE FROM students WHERE id = ?"
        conn.prepareStatement(sql).use { ps ->
            ps.setLong(1, id)
            val deleted = ps.executeUpdate()
            return deleted == 1
        }
    }
}

private fun ResultSet.toStudentRow(): StudentRow =
    StudentRow(
        id = getLong("id"),
        name = getString("name"),
        marks = getInt("marks")
    )

/* -------------------------------------------------------------------------- */
/* Transactions: commit / rollback                                             */
/* -------------------------------------------------------------------------- */

fun transactionDemo(conn: Connection) {
    println("Transaction demo (commit/rollback)")

    val repo = StudentRepository(conn)

    // Default: autoCommit=true (each statement commits immediately)
    // For transaction: set autoCommit=false and commit manually
    val oldAutoCommit = conn.autoCommit
    conn.autoCommit = false

    try {
        val idA = repo.createStudent("Aman", 85)
        val idB = repo.createStudent("Ravi", 88)

        // Force a failure to demonstrate rollback
        // (Marks invalid => require throws IllegalArgumentException)
        repo.createStudent("BadStudent", 999)

        conn.commit()
        println("- Transaction committed (this line will NOT run in this demo)")
        println("- inserted: $idA, $idB")
    } catch (e: Exception) {
        println("- Transaction failed: ${e::class.simpleName}: ${e.message}")
        println("- Rolling back...")
        conn.rollback()
        println("- Rollback done.")
    } finally {
        conn.autoCommit = oldAutoCommit
    }

    println("After transaction, students in DB:")
    val list = StudentRepository(conn).getAllStudents()
    list.forEach { println("- $it") }
    println()
}

/* -------------------------------------------------------------------------- */
/* ORM Overview (Concept)                                                     */
/* -------------------------------------------------------------------------- */

fun ormOverview() {
    println("ORM overview (concept)")

    println("- JDBC = low-level, full control, more boilerplate")
    println("- ORM  = maps tables <-> objects, reduces boilerplate, adds features\n")

    println("Popular JVM/Kotlin options:")
    println("- JPA/Hibernate (common in Spring)")
    println("- Exposed (Kotlin-first SQL/DAO)")
    println("- jOOQ (type-safe SQL builder)\n")

    println("Rule of thumb:")
    println("- Learn JDBC fundamentals first (this module).")
    println("- Use ORM when you want productivity + consistency, but keep SQL knowledge.\n")
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Add a new table:
 *    courses(id, title, fee)
 *
 * 2) Create CourseRepository with CRUD like StudentRepository
 *
 * 3) Create transaction:
 *    - insert a course
 *    - insert a student
 *    - throw an exception
 *    - verify rollback removed both
 *
 * 4) Add a query:
 *    - getTopStudents(minMarks:Int) using WHERE marks >= ?
 *
 * 5) Discuss:
 *    - Why PreparedStatement is safer than string concatenation?
 *    - When would you prefer ORM over JDBC?
 */
