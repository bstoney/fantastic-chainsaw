package adventofcode.day05

import adventofcode.*

fun main() {
    Solution.solve()
}

typealias Crate = Char

object Solution : AdventOfCodeSolution<String>() {
    override fun solve() {
        solve(5, "CMZ", "MCD")
    }

    override fun part1(input: List<String>): String {
        return getSupplyStacks(input) { crates -> crates.reversed() }
            .map { it.getTopCrate() }
            .joinToString("") { it.toString() }
    }

    override fun part2(input: List<String>): String {
        return getSupplyStacks(input) { crates -> crates }
            .map { it.getTopCrate() }
            .joinToString("") { it.toString() }
    }

    private fun getSupplyStacks(
        input: List<String>,
        moveBy: (List<Crate>) -> List<Crate>
    ): List<SupplyStack> {
        val (supplyStacksInput, instructionsInput) = input.splitAt { it.isEmpty() }
        val supplyStacks = getSupplyStacksInitialState(supplyStacksInput)

        debug(supplyStacks)

        val regex = Regex("move (\\d+) from (\\w+) to (\\w+)")
        return instructionsInput.mapNotNull { regex.find(it) }
            .map {
                val (count, from, to) = it.destructured
                Instruction(count.toInt(), from, to)
            }
            .peek { debug(it) }
            .fold(supplyStacks.values) { stacks, instruction ->
                var movedCrates: List<Crate> = emptyList()
                stacks.map {
                    when (it.id) {
                        instruction.fromId -> {
                            movedCrates = it.getTopCrates(instruction.count)
                            ({ it.removeCrates(instruction.count) })
                        }

                        instruction.toId -> ({ it.addCrates(moveBy(movedCrates)) })
                        else -> ({ it })
                    }
                }
                    .map { it() }
            }
            .sortedBy(SupplyStack::id)
    }

    private fun getSupplyStacksInitialState(supplyStacksInput: List<String>): Map<String, SupplyStack> {
        val supplyStacks = supplyStacksInput.map { it.chunked(4) { stack -> stack[1] } }
            .reversed()
            .fold(listOf<SupplyStack>()) { acc, chars ->
                if (acc.isEmpty()) {
                    chars.map { SupplyStack(it.toString(), listOf()) }
                } else {
                    acc.zipAll(chars) { a, b ->
                        if (b != null && b != ' ') {
                            SupplyStack(a!!.id, a.crates + b)
                        } else {
                            a!!
                        }
                    }
                        .toList()
                }
            }
            .fold(mapOf<String, SupplyStack>()) { acc, supplyStack -> acc + Pair(supplyStack.id, supplyStack) }
        return supplyStacks
    }

    data class SupplyStack(val id: String, val crates: List<Crate>) {
        fun getTopCrate() = crates.last()

        fun getTopCrates(count: Int) = crates.takeLast(count)

        fun removeCrates(count: Int) = SupplyStack(id, crates.subList(0, crates.size - count))

        fun addCrates(crates: List<Crate>) = SupplyStack(id, this.crates + crates)
    }

    data class Instruction(val count: Int, val fromId: String, val toId: String) {
        override fun toString(): String {
            return "moving $count crate(s) from $fromId to $toId"
        }
    }
}
