package adventofcode.day11

import adventofcode.AdventOfCodeSolution
import adventofcode.split

fun main() {
    Solution.solve()
}

typealias MonkeyId = Int
typealias WorryLevel = Int

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(11, 10605)
    }

    override fun part1(input: List<String>): Int {
        val (monkey1, monkey2) = playRounds(getMonkeys(input), 20)
            .sortedBy { it.itemsInspected }
            .reversed()
            .take(2)

        return monkey1.itemsInspected * monkey2.itemsInspected
    }

    private fun playRounds(monkeys: List<Monkey>, rounds: Int): List<Monkey> {
        return (1..rounds).fold(monkeys) { previousRound, round ->
            val roundResult = previousRound.toMutableList()
            previousRound.indices.forEach { index ->
                val monkey = roundResult[index]
                monkey.items.forEach { item ->
                    val worryLevel = monkey.inspect(item) / 3
                    val throwTo = monkey.test(worryLevel)
                    roundResult[throwTo] = roundResult[throwTo].receiveItem(worryLevel)
                }

                roundResult[index] = Monkey(
                    monkey.id,
                    emptyList(),
                    monkey.inspect,
                    monkey.test,
                    monkey.itemsInspected + monkey.items.size
                )
            }

            debug("After round $round, the monkeys are holding items with these worry levels:")
            roundResult.forEach { debug("Monkey ${it.id}: ${it.items.joinToString(", ")}") }

            roundResult.toList()
        }
    }

    private fun getMonkeys(input: List<String>): List<Monkey> {
        return input.split { it.isEmpty() }
            .mapIndexed { id, definition ->
                Monkey(
                    id,
                    getItemList(definition[1]),
                    getInspectionOperation(definition[2]),
                    getBoredTest(definition[3], definition[4], definition[5])
                )
            }
            .toList()
    }

    private fun getItemList(input: String): List<WorryLevel> {
        return input.substring("  Starting items: ".length)
            .split(", ")
            .map { it.toInt() }
    }

    private fun getInspectionOperation(input: String): Inspection {
        val (operand1, operator, operand2) = input.substring("  Operation: new = ".length)
            .split(" ")
        return Inspection(operand1, operator, operand2)
    }

    private fun getBoredTest(condition: String, trueAction: String, falseAction: String): BoredTest {
        val testValue = condition.substring("  Test: divisible by ".length).toInt()
        val trueMonkey = trueAction.substring("    If true: throw to monkey ".length).toInt()
        val falseMonkey = falseAction.substring("    If false: throw to monkey ".length).toInt()
        return BoredTest(testValue, trueMonkey, falseMonkey)
    }

    data class Monkey(
        val id: MonkeyId,
        val items: List<WorryLevel>,
        val inspect: Inspection,
        val test: BoredTest,
        val itemsInspected: Int = 0
    ) {
        fun receiveItem(item: WorryLevel): Monkey = Monkey(id, items + item, inspect, test, itemsInspected)
    }

    data class Inspection(val operand1: String, val operator: String, val operand2: String) {
        operator fun invoke(input: WorryLevel): WorryLevel =
            when (operator) {
                "*" -> getOperand(operand1, input) * getOperand(operand2, input)
                "+" -> getOperand(operand1, input) + getOperand(operand2, input)
                else -> throw IllegalStateException("Unknown operator: $operator")
            }

        private fun getOperand(operand: String, input: WorryLevel): WorryLevel =
            if (operand == "old") {
                input
            } else {
                operand.toInt()
            }
    }

    data class BoredTest(val testValue: WorryLevel, val trueMonkey: MonkeyId, val falseMonkey: MonkeyId) {
        operator fun invoke(worryLevel: WorryLevel): MonkeyId =
            if (worryLevel % testValue == 0) {
                trueMonkey
            } else {
                falseMonkey
            }
    }
}
