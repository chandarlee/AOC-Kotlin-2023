fun main() {
    fun part1(input: List<String>): Int {
        val times = input.first().split(":")
            .last()
            .trim()
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.trim().toInt() }
        val distance = input.last().split(":")
            .last()
            .trim()
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.trim().toInt() }
        return times.mapIndexed { index, duration ->
            (0..duration).map { holdDuration ->
                holdDuration * (duration - holdDuration)
            }.count {
                it > distance[index]
            }
        }.reduce {
            acc, i -> acc * i
        }
    }

    fun part2(input: List<String>): Int {
        val duration = input.first().split(":")
            .last()
            .replace(" ", "")
            .toLong()
        val distance = input.last().split(":")
            .last()
            .replace(" ", "")
            .toLong()
        return (0..duration).map { holdDuration ->
            holdDuration * (duration - holdDuration)
        }.count {
            it > distance
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}