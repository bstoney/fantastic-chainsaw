package adventofcode.day02

import adventofcode.AdventOfCodeSolution

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(2, 15, 12)
    }

    override fun part1(input: List<String>): Int {
        return getRounds(input) { _: Play, playerInput: String ->
            when (playerInput) {
                "X" -> Play.A
                "Y" -> Play.B
                "Z" -> Play.C
                else -> throw IllegalArgumentException("Illegal player input: $playerInput")
            }
        }
            .sumOf { it.roundScore() }
    }

    override fun part2(input: List<String>): Int {
        return getRounds(input) { opponentPlay: Play, playerInput: String ->
            when (playerInput) {
                "X" -> opponentPlay.defeats()
                "Y" -> opponentPlay
                "Z" -> opponentPlay.defeatedBy()
                else -> throw IllegalArgumentException("Illegal player input: $playerInput")
            }
        }
            .sumOf { it.roundScore() }
    }

    private fun getRounds(
        input: List<String>,
        playerMove: (opponentInput: Play, playerInput: String) -> Play
    ): List<Round> = input
        .filter { it.length == 3 }
        .map { it.split(" ") }
        .map { (opponentInput, playerInput) ->
            val opponentPlay = Play.valueOf(opponentInput)
            val round = Round(opponentPlay, playerMove(opponentPlay, playerInput))
            debug("${round.opponent.move} ($opponentInput) vs ${round.player.move} ($playerInput): ${round.roundResult()} + ${round.player.move} = ${round.roundScore()}")
            round
        }

    private data class Round(val opponent: Play, val player: Play) {
        fun roundResult() = opponent.playAgainst(player)

        fun roundScore() = roundResult().score + player.move.score
    }

    private enum class RoundResult(val score: Int) {
        Win(6),
        Draw(3),
        Lose(0),
        ;
    }

    private enum class Play(val move: Move) {
        A(Move.Rock),
        B(Move.Paper),
        C(Move.Scissors),
        ;

        fun playAgainst(play: Play): RoundResult =
            when (play.move) {
                this.move -> RoundResult.Draw
                defeats().move -> RoundResult.Lose
                else -> RoundResult.Win
            }

        fun defeats(): Play = when (move) {
            Move.Rock -> C
            Move.Paper -> A
            Move.Scissors -> B
        }

        fun defeatedBy(): Play = when (move) {
            Move.Rock -> B
            Move.Paper -> C
            Move.Scissors -> A
        }
    }

    private enum class Move(val score: Int) {
        Rock(1),
        Paper(2),
        Scissors(3),
    }
}
