package adventofcode

abstract class AdventOfCodeSolution {
    open fun part1(input: List<String>): Int? {
        return null
    }

    open fun part2(input: List<String>): Int? {
        return null
    }

    fun solve(day: Int, expectedPart1Test: Int, expectedPart2Test: Int? = null) {
        val inputFile = "Day" + day.toString().padStart(2, '0')

        val testInput = readInput("${inputFile}_test")
        val testPart1 = part1(testInput)
        println("Part 1")
        println("test: $testPart1")
        check(testPart1 == expectedPart1Test)

        val input = readInput(inputFile)

        println("result: ${part1(input)}")

        if (expectedPart2Test != null) {
            println()
            println("Part 2")
            val testPart2 = part2(testInput)
            println("test: $testPart2")
            check(testPart2 == expectedPart2Test)

            println("result: ${part2(input)}")
        }
    }
}