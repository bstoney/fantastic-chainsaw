package adventofcode

abstract class AdventOfCodeSolution(private val debug: Boolean = false) {
    open fun part1(input: List<String>): Int? {
        return null
    }

    open fun part2(input: List<String>): Int? {
        return null
    }

    fun solve(day: Int, expectedPart1Test: Int, expectedPart2Test: Int? = null) {
        log("Day $day")

        val inputFile = "Day" + day.toString().padStart(2, '0')

        val testInput = readInput("${inputFile}_test")
        val testPart1 = part1(testInput)
        log("Part 1")
        log("test: $testPart1")
        check(testPart1 == expectedPart1Test) { "solution was $testPart1, expected $expectedPart1Test" }

        val input = readInput(inputFile)

        log("result: ${part1(input)}")

        if (expectedPart2Test != null) {
            log()
            log("Part 2")
            val testPart2 = part2(testInput)
            log("test: $testPart2")
            check(testPart2 == expectedPart2Test) { "solution was $testPart2, expected $expectedPart2Test" }

            log("result: ${part2(input)}")
        }
    }

    fun log(message: Any = "") {
        println(message)
    }

    fun debug(message: Any) {
        if (debug) {
            println(message)
        }
    }
}