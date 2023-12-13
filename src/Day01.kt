fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val i = line.indexOfFirst { it.isDigit() }
            val j = line.indexOfLast { it.isDigit() }
            val numString = buildString {
                if (i >= 0) append(line[i])
                if (j >= 0) append(line[j])
            }
            numString.toIntOrNull() ?: 0
        }
    }

    fun part2(input: List<String>): Int {
        val stringToDigit = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to  "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9",
        )
        return input.sumOf { line ->
            val firstDigitIndex = line.indexOfFirst { it.isDigit() }
            val (firstStringIndex, firstString) = line.findAnyOf(stringToDigit.keys) ?: (-1 to "0")
            val lastDigitIndex = line.indexOfLast { it.isDigit() }
            val (lastStringIndex, lastString) = line.findLastAnyOf(stringToDigit.keys) ?: (-1 to "0")
            val numString = buildString {
                if (firstDigitIndex >= 0 && (firstStringIndex == -1 || firstDigitIndex < firstStringIndex))
                    append(line[firstDigitIndex])
                if (firstStringIndex >= 0 && (firstDigitIndex == -1 || firstStringIndex < firstDigitIndex))
                    append(stringToDigit[firstString])
                if (lastDigitIndex >= 0 && (lastStringIndex == -1 || lastDigitIndex > lastStringIndex))
                    append(line[lastDigitIndex])
                if (lastStringIndex >= 0 && (lastDigitIndex == -1 || lastStringIndex > lastDigitIndex))
                    append(stringToDigit[lastString])
            }
            numString.toIntOrNull() ?: 0
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
