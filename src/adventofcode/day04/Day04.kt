package adventofcode.day04

import adventofcode.AdventOfCodeSolution

fun main() {
    Solution.solve()
}

typealias Section = Int

object Solution : AdventOfCodeSolution() {
    fun solve() {
        solve(4, 2, 4)
    }

    override fun part1(input: List<String>): Int {
        return getCleanUpPairs(input)
            .filter { it.assignment1.sections.size == it.sections.size || it.assignment2.sections.size == it.sections.size }
            .size
    }

    override fun part2(input: List<String>): Int {
        return getCleanUpPairs(input)
            .filter { it.assignment1.sections.size + it.assignment2.sections.size > it.sections.size }
            .size
    }

    private fun getCleanUpPairs(input: List<String>): List<CleanUpPair> = input
        .map { CleanUpPair(it) }
        .mapIndexed { index, cleanUpPair ->
            debug("Pair $index -> (${cleanUpPair.sections.size}) {${cleanUpPair.assignment1}(${cleanUpPair.assignment1.sections.size}),${cleanUpPair.assignment2}(${cleanUpPair.assignment2.sections.size})")
            cleanUpPair
        }

    class CleanUpPair(private val input: String) {
        private val assignments: List<Assignment> by lazy {
            input.split(",")
                .map { range ->
                    val (start, end) = range.split("-")
                        .map { it.toInt() }
                    Assignment(start, end)
                }
        }

        val assignment1: Assignment by lazy { assignments[0] }
        val assignment2: Assignment by lazy { assignments[1] }

        val sections: Set<Section> by lazy { assignment1 union assignment2 }
    }

    data class Assignment(val start: Int, val end: Int) {
        val sections: Set<Section> by lazy { (start..end).toSet() }
    }
}

private infix fun Solution.Assignment.union(assignment: Solution.Assignment): Set<Section> =
    sections union assignment.sections
