package adventofcode

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = with(File("input", "$name.txt")) {
    return when {
        exists() -> readLines()
        else -> throw IllegalStateException("Missing input file: input/$name.txt")
    }
}

/**
 * Converts string to adventofcode.md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
