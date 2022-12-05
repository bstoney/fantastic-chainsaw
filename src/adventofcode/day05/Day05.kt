package adventofcode.day05

import adventofcode.AdventOfCodeSolution
import adventofcode.split
import adventofcode.splitAt

fun main() {
    Solution.solve()
}

typealias Crate = Char

object Solution : AdventOfCodeSolution<String>() {
    override fun solve() {
        solve(5, "CMZ")
    }

    override fun part1(input: List<String>): String {
        return getSupplyStacksAndInstructions(input)
            .map { it.getTopCrate() }
            .joinToString("") { it.toString() }
    }

    private fun getSupplyStacksAndInstructions(input: List<String>): List<SupplyStack> {
        val (supplyStacksInput, instructionsInput) = input.split { it.isEmpty() }
        val supplyStacks = getSupplyStacks(supplyStacksInput)

        debug(supplyStacks)

        val regex = Regex("move (\\d+) from (\\w+) to (\\w+)")
        instructionsInput.map { regex.find(it) }
            .filterNotNull()
            .forEach {
                val (count, from, to) = it.destructured
                debug("moving $count crate(s) from $from to $to")
                val removedCrates = supplyStacks[from]!!.remove(count.toInt())
                supplyStacks[to]!!.addAll(removedCrates.reversed())
            }

        return supplyStacks.values.sortedBy(SupplyStack::id)
    }

    private fun getSupplyStacks(supplyStacksInput: List<String>): Map<String, SupplyStack> {
        val supplyStacks = supplyStacksInput.map { it.chunked(4) { stack -> stack[1] } }
            .reversed()
            .fold(listOf<SupplyStack>()) { acc, chars ->
                if (acc.isEmpty()) {
                    chars.map { SupplyStack(it.toString()) }
                } else {
                    chars.mapIndexed { index, c -> Pair(index, c) }
                        .filter { it.second != ' ' }
                        .forEach { (index, c) -> acc[index].add(c) }
                    acc
                }
            }
            .fold(mapOf<String, SupplyStack>()) { acc, supplyStack -> acc + Pair(supplyStack.id, supplyStack) }
        return supplyStacks
    }

    class SupplyStack(val id: String, private var crates: List<Crate> = listOf()) {
        fun add(vararg crate: Crate) {
            addAll(crate.asList())
        }

        fun addAll(toAdd: List<Crate>) {
            crates = crates + toAdd
        }

        fun remove(count: Int): List<Crate> {
            val (remaining, removed) = crates.splitAt(crates.size - count)
            crates = remaining
            return removed
        }

        fun getTopCrate() = crates.last()
    }
}
