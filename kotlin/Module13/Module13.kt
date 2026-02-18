/**
 * Techlambda Training — Kotlin (INTERMEDIATE)
 * Module 13: File I/O & Serialization
 *
 * Audience: Working developers
 * Goal: Production readiness for reading/writing files + configs + JSON basics
 *
 * WHAT:
 * - Reading/writing files (text)
 * - JSON handling (concept + lightweight demo)
 * - Configuration files (properties-style)
 * - Resource management (use {} / try-finally mindset)
 *
 * WHY:
 * - Backend services load configs, write logs, export reports
 * - Android reads assets, caches responses, persists small configs
 * - Production requires safe resource handling + clear error strategy
 *
 * WHEN:
 * - Reading env/config, importing/exporting data, caching, logging, reporting
 *
 * HOW:
 * - Use java.nio.file (modern JVM file APIs)
 * - Use Kotlin's `use {}` for safe resource closing
 * - Demonstrate:
 *   1) Write text file
 *   2) Read text file
 *   3) Write properties config + read it back
 *   4) Minimal JSON encode/decode for simple maps (teaching demo)
 *
 * NOTE on JSON:
 * - Real production JSON uses libraries:
 *   - kotlinx.serialization (Kotlin-native)
 *   - Jackson / Moshi / Gson
 * - Here we show a tiny “safe enough for demo” parser for flat JSON objects only.
 */

import java.io.IOException
import java.io.StringReader
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

fun main() {
    println("=== Module 13: File I/O & Serialization (Techlambda) ===\n")

    val workDir = Files.createTempDirectory("techlambda_kotlin_io_demo")
    println("Work directory: $workDir\n")

    writeAndReadTextFile(workDir)
    configFilesWithProperties(workDir)
    jsonHandlingDemo(workDir)
    resourceManagementDemo(workDir)

    println("\n=== End of Module 13 ===")
}

/* -------------------------------------------------------------------------- */
/* 1) Reading/Writing Text Files                                               */
/* -------------------------------------------------------------------------- */

fun writeAndReadTextFile(dir: Path) {
    println("1) Reading/Writing Text Files")

    val file = dir.resolve("notes.txt")

    // WHAT: Writing a file
    // WHY: store logs, reports, exports, caches
    // WHEN: whenever you need persistence outside memory
    // HOW: Files.writeString / Files.write (JVM NIO)
    val content = buildString {
        appendLine("Techlambda Training - Module 13")
        appendLine("Topic: File I/O")
        appendLine("This file was generated at: ${System.currentTimeMillis()}")
    }

    Files.writeString(file, content, StandardCharsets.UTF_8)
    println("- Wrote file: $file")

    // Reading back
    val readBack = Files.readString(file, StandardCharsets.UTF_8)
    println("- Read file content:\n$readBack")

    println()
}

/* -------------------------------------------------------------------------- */
/* 2) Config Files (Properties style)                                          */
/* -------------------------------------------------------------------------- */

fun configFilesWithProperties(dir: Path) {
    println("2) Configuration Files (Properties style)")

    val configPath = dir.resolve("app.properties")

    // WHY: Properties is a very common config format on JVM
    // WHEN: simple key-value configs (env, region, flags, timeouts)
    val props = Properties().apply {
        setProperty("env", "prod")
        setProperty("region", "ME")
        setProperty("timeoutMs", "3000")
    }

    // Resource management: use {} auto-closes OutputStream
    Files.newOutputStream(configPath).use { out ->
        props.store(out, "Techlambda App Config")
    }
    println("- Saved properties config: $configPath")

    // Load again
    val loaded = Properties()
    Files.newInputStream(configPath).use { input ->
        loaded.load(input)
    }

    val env = loaded.getProperty("env")
    val region = loaded.getProperty("region")
    val timeoutMs = loaded.getProperty("timeoutMs")?.toLongOrNull() ?: 0L

    println("- Loaded config env=$env, region=$region, timeoutMs=$timeoutMs\n")
}

/* -------------------------------------------------------------------------- */
/* 3) JSON Handling (Intro + Minimal Demo)                                     */
/* -------------------------------------------------------------------------- */

fun jsonHandlingDemo(dir: Path) {
    println("3) JSON Handling (Intro + Minimal Demo)")

    // In production you would use:
    // - kotlinx.serialization: @Serializable data class ...
    // - Jackson: ObjectMapper().readValue(...)
    // - Moshi/Gson
    //
    // For teaching (single-file, no dependencies), we do:
    // - Encode a flat Map<String, String> -> JSON
    // - Decode flat JSON object -> Map<String, String>
    //
    // LIMITATION: This demo supports only flat JSON like:
    // {"env":"prod","region":"ME"}
    // (No nested objects, arrays, numbers, booleans.)

    val jsonFile = dir.resolve("config.json")

    val configMap = mapOf(
        "env" to "prod",
        "region" to "ME",
        "owner" to "techlambda"
    )

    val jsonText = SimpleJson.encodeFlatObject(configMap)
    Files.writeString(jsonFile, jsonText, StandardCharsets.UTF_8)
    println("- Wrote JSON: $jsonFile")
    println("  $jsonText")

    val readJson = Files.readString(jsonFile, StandardCharsets.UTF_8)
    val parsed = SimpleJson.decodeFlatObject(readJson)

    println("- Parsed JSON map => $parsed")
    println("- Example access: env=${parsed["env"]}, region=${parsed["region"]}\n")
}

/**
 * Minimal JSON utility for training only.
 * Supports:
 * - flat object: {"k":"v","k2":"v2"}
 * - string keys + string values only
 */
object SimpleJson {

    fun encodeFlatObject(map: Map<String, String>): String {
        val body = map.entries.joinToString(",") { (k, v) ->
            "\"${escape(k)}\":\"${escape(v)}\""
        }
        return "{$body}"
    }

    fun decodeFlatObject(json: String): Map<String, String> {
        val trimmed = json.trim()
        require(trimmed.startsWith("{") && trimmed.endsWith("}")) { "Invalid JSON object" }

        val inner = trimmed.removePrefix("{").removeSuffix("}").trim()
        if (inner.isBlank()) return emptyMap()

        // Split by commas not inside quotes (simple approach for flat strings)
        val pairs = splitByCommaOutsideQuotes(inner)

        val out = mutableMapOf<String, String>()
        for (p in pairs) {
            val idx = p.indexOf(':')
            require(idx > 0) { "Invalid key:value pair: $p" }

            val rawKey = p.substring(0, idx).trim()
            val rawVal = p.substring(idx + 1).trim()

            val key = unquote(rawKey)
            val value = unquote(rawVal)

            out[key] = unescape(value)
        }
        return out
    }

    private fun splitByCommaOutsideQuotes(s: String): List<String> {
        val result = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false

        for (ch in s) {
            when (ch) {
                '"' -> {
                    inQuotes = !inQuotes
                    sb.append(ch)
                }
                ',' -> {
                    if (inQuotes) sb.append(ch)
                    else {
                        result.add(sb.toString().trim())
                        sb.clear()
                    }
                }
                else -> sb.append(ch)
            }
        }
        if (sb.isNotBlank()) result.add(sb.toString().trim())
        return result
    }

    private fun unquote(x: String): String {
        val t = x.trim()
        require(t.startsWith("\"") && t.endsWith("\"")) { "Expected quoted string: $x" }
        return t.substring(1, t.length - 1)
    }

    private fun escape(x: String): String =
        x.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")

    private fun unescape(x: String): String =
        x.replace("\\n", "\n")
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
}

/* -------------------------------------------------------------------------- */
/* 4) Resource Management (use {}, try-catch, safe cleanup)                    */
/* -------------------------------------------------------------------------- */

fun resourceManagementDemo(dir: Path) {
    println("4) Resource Management (use {} + error handling)")

    val file = dir.resolve("stream_demo.txt")

    // use {} ensures resource closes even if exception happens
    try {
        Files.newBufferedWriter(file, StandardCharsets.UTF_8).use { writer ->
            writer.appendLine("Line 1")
            writer.appendLine("Line 2")
        }
        println("- BufferedWriter closed safely via use {}")

        val lines = Files.newBufferedReader(file, StandardCharsets.UTF_8).use { reader ->
            reader.readLines()
        }
        println("- Read lines via BufferedReader use {} => $lines")

    } catch (e: IOException) {
        // Production: log with context, map to domain exception if needed
        println("- IO Error: ${e.message}")
    }

    println("\nKey rule:")
    println("- Always close streams/readers/writers using use {} to prevent leaks.\n")
}

/**
 * PRACTICE TASKS (Student)
 *
 * 1) Write a file "students.txt" with 5 student names (one per line), then read it back.
 *
 * 2) Create properties config:
 *    - baseUrl=https://techlambda.com
 *    - retryCount=3
 *    Save + reload + print values.
 *
 * 3) Extend SimpleJson:
 *    - Add support for empty object {}
 *    - Reject invalid JSON clearly
 *
 * 4) (Production note)
 *    - Add kotlinx.serialization in Gradle and serialize a data class Config(env, region).
 */
