package adventofcode.day03

import adventofcode.AdventOfCodeSolution

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution(true) {
    fun solve() {
        solve(3, 157)
    }

    override fun part1(input: List<String>): Int {
        return getIncorrectItems(input)
            .map(Item::priority)
            .sum()
    }

    private fun getIncorrectItems(input: List<String>) = getRucksacks(input)
        .mapIndexed { index, rucksack ->
            val sharedItem = rucksack.sharedItem()
            debug("Rucksack $index -> $sharedItem priority: ${sharedItem.priority()}")
            sharedItem
        }

    private fun getRucksacks(input: List<String>): List<Rucksack> = input
        .map { it.map(::Item) }
        .map {
            val splitAt = it.size / 2
            val compartment1Items = it.subList(0, splitAt).toSet()
            val compartment2Items = it.subList(splitAt, it.size).toSet()
            Rucksack(compartment1Items, compartment2Items)
        }

    data class Rucksack(val compartment1: Set<Item>, val compartment2: Set<Item>) {
        fun sharedItem() = compartment1.intersect(compartment2).single()
    }

    data class Item(val id: Char) {
        fun priority() = if (id >= 'a') id.code - 0x60 else id.code - 0x40 + 26
    }
}
