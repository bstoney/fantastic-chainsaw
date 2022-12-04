fun main() {
    fun part1(input: List<String>): Int {
        return Solve.getMaxCarriedCalories(input)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val testPart1 = part1(testInput)
    println(testPart1)
    check(testPart1 == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

object Solve {
    private fun getElvesCalories(input: List<String>): Array<List<Int>> {
        val nextElfItems = input.takeWhile { it.isNotEmpty() }.map { it.toInt() }
        if (nextElfItems.size < input.size) {
            return arrayOf(
                nextElfItems,
                * getElvesCalories(input.subList(nextElfItems.size + 1, input.size))
            )
        }

        return arrayOf(nextElfItems)
    }

    fun getMaxCarriedCalories(input: List<String>): Int {
        return getElvesCalories(input).maxOf { it.sum() }
    }
}
