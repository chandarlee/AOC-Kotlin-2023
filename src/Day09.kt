fun main() {
    fun nextExtrapolatedValue(ints: List<Int>): Int {
        if (ints.all { it == 0 }) {
            return 0
        }
        if (ints.size == 1) {
            return ints.last()
        }
        val nextList = ints.windowed(2).map { (a, b) ->
            b - a
        }
        return ints.last() + nextExtrapolatedValue(nextList)
    }

    fun previousExtrapolatedValue(ints: List<Int>): Int {
        if (ints.all { it == 0 }) {
            return 0
        }
        if (ints.size == 1) {
            return ints.last()
        }
        val nextList = ints.windowed(2).map { (a, b) ->
            b - a
        }
        return ints.first() - previousExtrapolatedValue(nextList)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val numbers = Regex("""(-?\d+)""")
                .findAll(line).map { it.value.toInt() }.toList()
            nextExtrapolatedValue(numbers)
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val numbers = Regex("""(-?\d+)""")
                .findAll(line).map { it.value.toInt() }.toList()
            previousExtrapolatedValue(numbers)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    // check(part2(testInput) == 0)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}