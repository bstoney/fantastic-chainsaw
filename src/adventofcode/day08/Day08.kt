package adventofcode.day08

import adventofcode.AdventOfCodeSolution
import adventofcode.peek

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(8, 21, 8)
    }

    override fun part1(input: List<String>): Int {
        return getTrees(input).count { it.visibility == TreeVisibility.Visible }
    }

    override fun part2(input: List<String>): Int? {
        return getTrees(input).maxOfOrNull { it.scenicScore }
    }

    private fun getTrees(input: List<String>): List<Tree> {
        val trees = input.mapIndexed { r, row ->
            row.map { char -> char.code - 0x30 }
                .mapIndexed { c, height -> UnexploredTree(r, c, height) }
        }

        val maxR = trees.lastIndex
        val maxC = trees[0].lastIndex

        return trees.flatten()
            .map { tree ->
                with(tree) {
                    val treeNorth = (r - 1 downTo 0).map { trees[it][c] }.find { isBlockedBy(it) } ?: Edge(0, c)
                    val treeSouth = (r + 1..maxR).map { trees[it][c] }.find { isBlockedBy(it) } ?: Edge(maxR, c)
                    val treeWest = (c - 1 downTo 0).map { trees[r][it] }.find { isBlockedBy(it) } ?: Edge(r, 0)
                    val treeEast = (c + 1..maxC).map { trees[r][it] }.find { isBlockedBy(it) } ?: Edge(r, maxC)
                    tree.viewTo(treeNorth, treeEast, treeSouth, treeWest)
                }
            }
            .peek { debug("tree ${it.r},${it.c} ${it.height} is ${it.visibility}") }
            .toList()
    }

    enum class TreeVisibility {
        Visible,
        Hidden,
        Unknown,
    }

    interface GridLocation {
        val r: Int
        val c: Int
    }

    data class Edge(override val r: Int, override val c: Int) : GridLocation

    interface Tree : GridLocation {
        val height: Int
        val visibility: TreeVisibility
        val scenicScore: Int

        fun isBlockedBy(other: Tree): Boolean {
            if (other.height >= height) {
                debug("tree: $r,$c $height")
                debug("blocked by: ${other.r},${other.c} ${other.height}")
                return true
            }

            return false
        }
    }

    data class UnexploredTree(override val r: Int, override val c: Int, override val height: Int) : Tree {
        override val visibility = TreeVisibility.Unknown

        override val scenicScore: Int
            get() = throw IllegalStateException("Unexplored")

        fun viewTo(n: GridLocation, e: GridLocation, s: GridLocation, w: GridLocation) =
            ExploredTree(r, c, height, ViewTo(n, e, s, w))
    }

    data class ExploredTree(
        override val r: Int,
        override val c: Int,
        override val height: Int,
        val view: ViewTo
    ) : Tree {
        override val visibility: TreeVisibility by lazy {
            if (view.any { it is Edge }) {
                TreeVisibility.Visible
            } else {
                TreeVisibility.Hidden
            }
        }

        override val scenicScore: Int by lazy {
            val viewNorth = r - view.n.r
            val viewEast = view.e.c - c
            val viewSouth = view.s.r - r
            val viewWest = c - view.w.c

            viewNorth * viewEast * viewSouth * viewWest
        }
    }

    data class ViewTo(val n: GridLocation, val e: GridLocation, val s: GridLocation, val w: GridLocation) :
        Iterable<GridLocation> {
        override fun iterator(): Iterator<GridLocation> = listOf(n, e, s, w).iterator()
    }
}
