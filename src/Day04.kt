import java.lang.Integer.min

fun main() {
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (winnings, numbers) = line.split(":")
                .last()
                .split("|")
            val winningNumbers = winnings.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
            numbers.split(" ").map { it.trim() }
                .filter {
                    it in winningNumbers
                }.foldIndexed(0) { index, acc, s ->
                    if (index == 0) 1 else 2 * acc
                } as Int
        }
    }

    fun part2(input: List<String>): Int {
        val cards = MutableList(input.size) { 1 }
        input.forEachIndexed { index, line ->
            val (winnings, numbers) = line.split(":")
                .last()
                .split("|")
            val winningNumbers = winnings.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
            val matchNumCounts = numbers.split(" ").map { it.trim() }
                .filter {
                    it in winningNumbers
                }.size

            for (i in index + 1..min(index + matchNumCounts, cards.size - 1)) {
                cards[i] += cards[index]
            }
        }
        return cards.sum()
    }

    // test if implementation meets criteria from the description, like:
    // val testInput = readInput("Day04_test")
    // check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
