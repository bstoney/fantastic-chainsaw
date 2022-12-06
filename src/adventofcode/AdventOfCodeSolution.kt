package adventofcode

private const val ENABLE_DEBUG = false

abstract class AdventOfCodeSolution<TSolution>(private val debug: Boolean = ENABLE_DEBUG) {
    open fun part1(input: List<String>): TSolution? {
        return null
    }

    open fun part2(input: List<String>): TSolution? {
        return null
    }

    abstract fun solve()

    fun solve(day: Int, expectedPart1Test: TSolution, expectedPart2Test: TSolution? = null) {
        log("Day $day")

        val inputFile = "Day" + day.toString().padStart(2, '0')

        val testPart1Input = readInput(part1TestFile(inputFile))
        val testPart1 = part1(testPart1Input)
        log("Part 1")
        log("test: $testPart1")
        check(testPart1 == expectedPart1Test) { "solution was $testPart1, expected $expectedPart1Test" }

        val input = readInput(inputFile)

        log("result: ${part1(input)}")

        if (expectedPart2Test != null) {
            log()
            log("Part 2")
            val testPart2Input = readInput(part2TestFile(inputFile))
            val testPart2 = part2(testPart2Input)
            log("test: $testPart2")
            check(testPart2 == expectedPart2Test) { "solution was $testPart2, expected $expectedPart2Test" }

            log("result: ${part2(input)}")
        }
    }

    protected open fun part1TestFile(inputFile: String) = "${inputFile}_test"

    protected open fun part2TestFile(inputFile: String) = part1TestFile(inputFile)

    fun log(message: Any = "") {
        println(message)
    }

    fun debug(message: Any) {
        if (debug) {
            println(message)
        }
    }
}