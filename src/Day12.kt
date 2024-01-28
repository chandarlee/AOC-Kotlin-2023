import kotlin.time.measureTime

fun main() {
    fun calArrangement(input: MutableList<Char>, reg: Regex, pounds: List<Int>, poundTotal: Int): Int {
        val lastQuestionMarkIndex = input.lastIndexOf('?')
        if (lastQuestionMarkIndex == -1) {
            return if(reg.matches(input.joinToString(""))) 1 else 0
        }
        if (input.count { it == '#' } == poundTotal) {
            val s = input.map { if (it == '?') '.' else it }
            return if(reg.matches(s.joinToString(""))) 1 else 0
        }

        val matchedPound = input.subList(lastQuestionMarkIndex + 1, input.size).joinToString("")
            .split(".")
            .filter { it.isNotEmpty() }
            .map { it.length }
        if (matchedPound.size > pounds.size) return 0
        for (i in 1..matchedPound.size) {
            val j = matchedPound.size - i
            val jv = matchedPound[j]

            val k = pounds.size - i
            val kv = pounds[k]

            if (j > 1 && jv != kv) return 0
            if (j == 1 && jv > kv) return 0
        }

        return calArrangement(input.toMutableList().also { it[lastQuestionMarkIndex] = '#' }, reg, pounds, poundTotal) +
                calArrangement(input.toMutableList().also { it[lastQuestionMarkIndex] = '.' }, reg, pounds, poundTotal)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (first,second) = line.split(" ")
            val poundCount = second.split(",")
                .map { it.trim().toInt() }
            val reg = Regex(poundCount.joinToString(separator = "\\.+", prefix = "\\.*", postfix = "\\.*") {
                    "#{$it}"
                })
            calArrangement(first.toMutableList(), reg, poundCount, poundCount.sum())
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val (first,second) = line.split(" ")
            val value = listOf(first, first, first, first, first)
                .joinToString("?")
            val poundCount = listOf(second, second, second, second, second)
                .joinToString(",").split(",")
                .map { it.trim().toInt() }
            val reg = Regex(poundCount.joinToString(separator = "\\.+", prefix = "\\.*", postfix = "\\.*") {
                    "#{$it}"
                })
            calArrangement(value.toMutableList(), reg, poundCount, poundCount.sum())
        }
    }

    // test if implementation meets criteria from the description, like:
    // val testInput = readInput("Day12_test")
    // check(part1(testInput) == 21)
    // check(part2(testInput) == 525152)

    val input = readInput("Day12")
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}