package adventofcode.day06

import adventofcode.AdventOfCodeSolution

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<List<Int>>() {
    override fun solve() {
        solve(6, listOf(5, 6, 10, 11), listOf(19, 23, 23, 29, 26))
    }

    override fun part1TestFile(inputFile: String): String = "${inputFile}_test-part1"

    override fun part1(input: List<String>): List<Int> {
        return input.map { getStartOfPacket(it, 4) }
    }

    override fun part2TestFile(inputFile: String): String = "${inputFile}_test-part2"

    override fun part2(input: List<String>): List<Int> {
        return input.map { getStartOfPacket(it, 14) }
    }

    private fun getStartOfPacket(input: String, markerSize: Int): Int = input.windowed(markerSize)
        .takeWhile { it.toSet().size < markerSize }
        .size + markerSize
}
