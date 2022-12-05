package adventofcode.day01

import adventofcode.AdventOfCodeSolution
import adventofcode.split

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(1, 24000, 45000)
    }

    override fun part1(input: List<String>): Int {
        return getMaxCarriedCalories(input)
    }

    override fun part2(input: List<String>): Int {
        return getTotalTopCarriedCalories(input, 3)
    }

    private fun getMaxCarriedCalories(input: List<String>): Int {
        return getElvesCalories(input).max()
    }

    private fun getTotalTopCarriedCalories(input: List<String>, limit: Int): Int {
        return getElvesCalories(input).sorted().reversed().take(limit).sum()
    }

    private fun getElvesCalories(input: List<String>) = getElvesItemCalories(input).map { it.sum() }

    private fun getElvesItemCalories(input: List<String>): Array<List<Int>> {
        val (listPart, rest) = input.split { it.isEmpty() }
        val nextElfItems = listPart.map { it.toInt() }
        if (rest.isNotEmpty()) {
            return arrayOf(nextElfItems, * getElvesItemCalories(rest))
        }

        return arrayOf(nextElfItems)
    }
}
