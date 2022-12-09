package adventofcode.day09

import adventofcode.AdventOfCodeSolution
import kotlin.math.abs

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(9, 13, 36)
    }

    override fun part1TestFile(inputFile: String) = "${inputFile}_test-part1"

    override fun part1(input: List<String>): Int {
        return getTailLocations(input)
            .toSet()
            .size
    }

    override fun part2TestFile(inputFile: String) = "${inputFile}_test-part2"

    override fun part2(input: List<String>): Int {
        return getTailLocations(input, 9)
            .toSet()
            .size
    }

    private fun getTailLocations(input: List<String>, followingKnots: Int = 1): Sequence<Location> {
        val headSequence: Sequence<Location> = getMovements(input)
            .runningFold(HeadLocation(0, 0)) { head, move -> head.move(move) }
        return (0 until followingKnots)
            .fold(headSequence) { s, _ ->
                s.runningFold(KnotLocation(0, 0)) { tail, head -> tail.follow(head) }
            }
    }

    private fun getMovements(input: List<String>) = input.asSequence()
        .map { it.split(" ", limit = 2) }
        .flatMap { (direction, distance) ->
            (1..distance.toInt())
                .map { Movement(Direction.valueOf(direction)) }
        }

    interface Location {
        val x: Int
        val y: Int
    }

    data class HeadLocation(override val x: Int, override val y: Int) : Location {
        fun move(movement: Movement): HeadLocation = when (movement.direction) {
            Direction.U -> HeadLocation(x, y + 1)
            Direction.D -> HeadLocation(x, y - 1)
            Direction.L -> HeadLocation(x - 1, y)
            Direction.R -> HeadLocation(x + 1, y)
        }
    }

    data class KnotLocation(override val x: Int, override val y: Int) : Location {
        fun follow(previous: Location): KnotLocation {
            val dx = previous.x - x
            val dy = previous.y - y
            return KnotLocation(x + moveBy(dx, dy), y + moveBy(dy, dx))
        }

        private fun moveBy(da: Int, db: Int): Int {
            return if (da == 0 || abs(da) <= 1 && abs(db) <= 1) {
                0
            } else {
                da / abs(da)
            }
        }
    }

    data class Movement(val direction: Direction)

    enum class Direction {
        U, D, L, R
    }
}
