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

fun <E> List<E>.splitAt(predicate: (E) -> Boolean): Pair<List<E>, List<E>> {
    val listPart = this.takeWhile { !predicate(it) }
    if (listPart.size < this.size) {
        return Pair(listPart, this.subList(listPart.size + 1, this.size))
    }

    return Pair(listPart, listOf())
}

fun <E> List<E>.splitAt(index: Int): Pair<List<E>, List<E>> {
    return Pair(this.subList(0, index), this.subList(index, this.size))
}

fun <E> Iterable<E>.split(predicate: (E) -> Boolean): Sequence<List<E>> {
    val iterator = iterator()
    return generateSequence {
        while (iterator.hasNext()) {
            val part = arrayListOf<E>()
            do {
                val next = iterator.next()
                if (predicate(next)) {
                    break
                }
                part.add(next)
            } while (iterator.hasNext())

            return@generateSequence part
        }

        null
    }
}

fun <T, R, V> Iterable<T>.zipAll(other: Iterable<R>, transform: (T?, R?) -> V): Sequence<V> {
    val first = iterator()
    val second = other.iterator()
    return generateSequence {
        while (first.hasNext() || second.hasNext()) {
            return@generateSequence transform(
                first.hasNext().iif({ first.next() }, { null }),
                second.hasNext().iif({ second.next() }, { null })
            )
        }
        null
    }
}

fun <T> Boolean.iif(supplyTrue: () -> T, supplyFalse: () -> T): T =
    if (this) {
        supplyTrue()
    } else {
        supplyFalse()
    }
