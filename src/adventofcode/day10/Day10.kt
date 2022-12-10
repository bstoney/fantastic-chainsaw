package adventofcode.day10

import adventofcode.AdventOfCodeSolution
import adventofcode.peek

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(10, 13140)
    }

    override fun part1(input: List<String>): Int {
        return getSignals(input)
            .mapIndexed { index, pair -> Triple(index, pair.first, pair.second) }
            .pick {
                when (it.first) {
                    20, 60, 100, 140, 180, 220 -> true
                    else -> false
                }
            }
            .peek { debug("$it -> ${it.first * it.second}") }
            .map { it.first * it.second }
            .sum()
    }

    private fun getSignals(input: List<String>): Sequence<Pair<Int, Int>> {
        return input.asSequence()
            .flatMap {
                with(it) {
                    when {
                        equals("noop") -> listOf(Noop())
                        startsWith("addx") -> {
                            val (_, value) = split(" ")
                            listOf(Noop(), AddX(value.toInt()))
                        }

                        else -> throw IllegalStateException("Unknown instruction: $it")
                    }
                }
            }
            .runningFold(Pair(1, 1)) { s, instruction -> Pair(s.second, instruction(s.second)) }
    }

    interface Instruction {
        operator fun invoke(signal: Int): Int
    }

    class Noop : Instruction {
        override fun invoke(signal: Int): Int = signal
    }

    class AddX(private val x: Int) : Instruction {
        override fun invoke(signal: Int): Int = signal + x
    }
}

private fun <T> Sequence<T>.pick(predicate: (T) -> Boolean): Sequence<T> {
    val iterator = iterator()
    return generateSequence {
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (predicate(next)) {
                return@generateSequence next
            }
        }
        null
    }
}
