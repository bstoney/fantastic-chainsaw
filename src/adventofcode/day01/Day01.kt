package adventofcode.day01

import adventofcode.AdventOfCodeSolution

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
        val nextElfItems = input.takeWhile { it.isNotEmpty() }.map { it.toInt() }
        if (nextElfItems.size < input.size) {
            return arrayOf(
                nextElfItems,
                * getElvesItemCalories(input.subList(nextElfItems.size + 1, input.size))
            )
        }

        return arrayOf(nextElfItems)
    }
}
