package adventofcode.day06

import adventofcode.AdventOfCodeSolution

fun main() {
    Solution.solve()
}

private const val START_OF_PACKET_MARKER_SIZE = 4

object Solution : AdventOfCodeSolution<List<Int>>() {
    override fun solve() {
        solve(6, listOf(5, 6, 10, 11))
    }

    override fun part1(input: List<String>): List<Int> {
        return input.map(::getStartOfPacket)
    }

    private fun getStartOfPacket(input: String): Int = input.windowed(START_OF_PACKET_MARKER_SIZE)
        .takeWhile { it.toSet().size < START_OF_PACKET_MARKER_SIZE }
        .size + START_OF_PACKET_MARKER_SIZE
}
