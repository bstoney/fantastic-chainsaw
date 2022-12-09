package adventofcode.day09

import adventofcode.AdventOfCodeSolution
import kotlin.math.abs

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(9, 13)
    }

    override fun part1(input: List<String>): Int {
        return getTailLocations(input)
            .toSet()
            .size
    }

    private fun getTailLocations(input: List<String>): List<TailLocation> {
        return input.map { it.split(" ", limit = 2) }
            .flatMap { (direction, distance) ->
                (1..distance.toInt())
                    .map { Movement(Direction.valueOf(direction)) }
            }
            .runningFold(HeadLocation(0, 0)) { head, move -> head.move(move) }
            .runningFold(TailLocation(0, 0)) { tail, head -> tail.follow(head) }
    }

    data class HeadLocation(val x: Int, val y: Int) {
        fun move(movement: Movement): HeadLocation = when (movement.direction) {
            Direction.U -> HeadLocation(x, y + 1)
            Direction.D -> HeadLocation(x, y - 1)
            Direction.L -> HeadLocation(x - 1, y)
            Direction.R -> HeadLocation(x + 1, y)
        }
    }

    data class TailLocation(val x: Int, val y: Int) {
        fun follow(head: HeadLocation): TailLocation {
            val dx = head.x - x
            val dy = head.y - y
            return TailLocation(x + moveBy(dx, dy), y + moveBy(dy, dx))
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
