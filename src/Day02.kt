fun main() {
    fun part1(input: List<String>): Int {
        val round = GameRound(
            RedCube(12),
            GreenCube(13),
            BlueCube(14),
        )
        return input.map { line ->
            Game.from(line)
        }.filter { game ->
            game.rounds.all { round.greaterThan(it) }
        }.sumOf {
            it.id
        }
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            Game.from(line)
        }.sumOf { game ->
            game.rounds.fold(Triple(0, 0, 0)) { acc, round ->
                Triple(
                    acc.first.coerceAtLeast(round.red.count),
                    acc.second.coerceAtLeast(round.green.count),
                    acc.third.coerceAtLeast(round.blue.count),
                )
            }.run {
                var multiply: Int = first
                multiply = if (second > 0) multiply * second else multiply
                multiply = if (third > 0) multiply * third else multiply
                multiply
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    // val testInput = readInput("Day02_test")
    // check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class Game(
    val id: Int,
    val rounds: List<GameRound>,
) {
    companion object {
        fun from(input: String): Game {
            // Game 37: 10 green, 11 red, 3 blue; 2 blue, 6 green, 11 red; 9 green, 8 red, 2 blue
            val (id, rounds) = input.split(":")
            return Game(
                id = id.split(" ").last().toInt(),
                rounds = rounds.split(";").map { GameRound.from(it) }
            )
        }
    }
}

data class GameRound(
    val red: RedCube,
    val green: GreenCube,
    val blue: BlueCube,
) {
    fun greaterThan(other: GameRound): Boolean {
        return red.count >= other.red.count &&
                green.count >= other.green.count &&
                blue.count >= other.blue.count
    }

    companion object {
        fun from(input: String): GameRound {
            //  10 green, 11 red, 3 blue
            val cubes = input.split(",")
                .map { it.trim() }
                .associate {
                    val item = it.split(" ")
                    item.last() to item.first()
                }

            return GameRound(
                RedCube(cubes["red"]?.toInt() ?: 0),
                GreenCube(cubes["green"]?.toInt() ?: 0),
                BlueCube(cubes["blue"]?.toInt() ?: 0),
            )
        }
    }
}

sealed interface Cube
data class RedCube(val count: Int) : Cube
data class GreenCube(val count: Int) : Cube
data class BlueCube(val count: Int) : Cube