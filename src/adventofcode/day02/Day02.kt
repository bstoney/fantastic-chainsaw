package adventofcode.day02

import adventofcode.AdventOfCodeSolution

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution() {
    fun solve() {
        solve(2, 15)
    }

    override fun part1(input: List<String>): Int {
        return getRounds(input).sumOf { it.roundScore() }
    }

    private fun getRounds(input: List<String>): List<Round> = input
        .filter { it.length == 3 }
        .map(::getRound)

    private fun getRound(input: String): Round {
        val plays = input.split(" ")
            .map(Play::valueOf)

        val round = Round(plays[0], plays[1])
        debug("$round -> ${round.opponent.move} vs ${round.player.move}: ${round.roundResult()} + ${round.player.move} = ${round.roundScore()}")
        return round
    }

    private data class Round(val opponent: Play, val player: Play) {
        fun roundResult() = opponent.playAgainst(player)

        fun roundScore() = roundResult().score + player.move.score
    }

    private enum class RoundResult(val score: Int) {
        Win(6),
        Draw(3),
        Lose(0);
    }

    private enum class Play(val move: Move, private val defeats: Move) {
        A(Move.Rock, Move.Scissors),
        B(Move.Paper, Move.Rock),
        C(Move.Scissors, Move.Paper),
        X(Move.Rock, Move.Scissors),
        Y(Move.Paper, Move.Rock),
        Z(Move.Scissors, Move.Paper),
        ;

        fun playAgainst(play: Play): RoundResult =
            when (play.move) {
                this.move -> RoundResult.Draw
                defeats -> RoundResult.Lose
                else -> RoundResult.Win
            }
    }

    private enum class Move(val score: Int) {
        Rock(1),
        Paper(2),
        Scissors(3),
    }
}
