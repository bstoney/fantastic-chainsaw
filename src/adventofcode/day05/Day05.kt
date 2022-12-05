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
        solve(5, "CMZ", "MCD")
    }

    override fun part1(input: List<String>): String {
        return getSupplyStacks(input) { count, from, to -> to.addAll(from.remove(count).reversed()) }
            .map { it.getTopCrate() }
            .joinToString("") { it.toString() }
    }

    override fun part2(input: List<String>): String {
        return getSupplyStacks(input) { count, from, to -> to.addAll(from.remove(count)) }
            .map { it.getTopCrate() }
            .joinToString("") { it.toString() }
    }

    private fun getSupplyStacks(
        input: List<String>,
        moveBy: (count: Int, from: SupplyStack, to: SupplyStack) -> Unit
    ): List<SupplyStack> {
        val (supplyStacksInput, instructionsInput) = input.split { it.isEmpty() }
        val supplyStacks = getSupplyStacksInitialState(supplyStacksInput)

        debug(supplyStacks)

        val regex = Regex("move (\\d+) from (\\w+) to (\\w+)")
        instructionsInput.mapNotNull { regex.find(it) }
            .forEach {
                val (count, from, to) = it.destructured
                debug("moving $count crate(s) from $from to $to")
                moveBy(count.toInt(), supplyStacks[from]!!, supplyStacks[to]!!)
            }

        return supplyStacks.values.sortedBy(SupplyStack::id)
    }

    private fun getSupplyStacksInitialState(supplyStacksInput: List<String>): Map<String, SupplyStack> {
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

    // TODO refactor to use immutable structure
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
