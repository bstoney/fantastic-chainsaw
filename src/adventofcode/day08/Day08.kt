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
        return super.part2(input)
    }

    private fun getTrees(input: List<String>): List<Tree> {
        val trees = input.mapIndexed { r, row ->
            row.map { char -> char.code - 0x30 }
                .mapIndexed { c, height -> Tree(r, c, height) }
        }

        val maxR = trees.lastIndex
        val maxC = trees[0].lastIndex

        return trees.flatten()
            .map { tree ->
                with(tree) {
                    when {
                        (r > 0 && c > 0 && c < maxC && r < maxR) -> {
                            if ((r - 1 downTo 0).map { trees[it][c] }.any { isBlockedBy(it) }
                                && (r + 1 .. maxR).map { trees[it][c] }.any { isBlockedBy(it) }
                                && (c - 1 downTo 0).map { trees[r][it] }.any { isBlockedBy(it) }
                                && (c + 1..maxC).map { trees[r][it] }.any { isBlockedBy(it) }) {
                                tree.withVisibility(TreeVisibility.Hidden)
                            } else {
                                tree.withVisibility(TreeVisibility.Visible)
                            }
                        }

                        else -> {
                            tree.withVisibility(TreeVisibility.Visible)
                        }
                    }
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

    data class Tree(val r: Int, val c: Int, val height: Int, val visibility: TreeVisibility = TreeVisibility.Unknown) {
        fun withVisibility(visibility: TreeVisibility): Tree = Tree(r, c, height, visibility)
        fun isBlockedBy(other: Tree): Boolean {
            if (other.height >= height) {
                debug("tree: $r,$c $height")
                debug("blocked by: ${other.r},${other.c} ${other.height}")
                return true
            }

            return false
        }
    }
}
