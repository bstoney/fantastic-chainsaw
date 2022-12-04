fun main() {
    fun part1(input: List<String>): Int {
        return Solve.getMaxCarriedCalories(input)
    }

    fun part2(input: List<String>): Int {
        return Solve.getTotalTopCarriedCalories(input, 3)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val testPart1 = part1(testInput)
    println(testPart1)
    check(testPart1 == 24000)
    val testPart2 = part2(testInput)
    println(testPart2)
    check(testPart2 == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

object Solve {
    fun getMaxCarriedCalories(input: List<String>): Int {
        return getElvesCalories(input).max()
    }

    fun getTotalTopCarriedCalories(input: List<String>, limit: Int): Int {
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
