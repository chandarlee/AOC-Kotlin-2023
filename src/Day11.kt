import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val rows = input.flatMap { line ->
            if (line.any { it != '.' }) listOf(
                line,
            ) else listOf(line, line)
        }
        val columns = rows.first().indices.map { index ->
            rows.map { it[index] }.joinToString("")
        }.flatMap { column ->
            if (column.any { it != '.' }) listOf(
                column,
            ) else listOf(column, column)
        }
        val galaxies = mutableListOf<Pair<Int, Int>>()
        columns.forEachIndexed { colIndex, s ->
            s.forEachIndexed { rowIndex, c ->
                if (c == '#') {
                    galaxies.add(rowIndex to colIndex)
                }
            }
        }
        val galaxiesPairs = galaxies.flatMapIndexed { index, pair ->
            ((index + 1)..<galaxies.size).map {
                pair to galaxies[it]
            }.toList()
        }
        return galaxiesPairs.sumOf { (from, to) ->
            (to.first - from.first).absoluteValue + (to.second - from.second).absoluteValue
        }
    }

    fun part2(input: List<String>): Int {
        val emptyRowsIndex = input.mapIndexedNotNull { index, s ->
            if (s.all { it == '.' }) index else null
        }
        val emptyColumnsIndex = input.first().indices.map { index ->
            input.map { it[index] }.joinToString("")
        }.mapIndexedNotNull { index, s ->
            if (s.all { it == '.' }) index else null
        }
        val galaxies = mutableListOf<Pair<Int, Int>>()
        input.forEachIndexed { rowIndex, line ->
            line.forEachIndexed { colIndex, c ->
                if (c == '#') {
                    val a = emptyRowsIndex.count {
                        it < rowIndex
                    }
                    val b = emptyColumnsIndex.count {
                        it < colIndex
                    }
                    galaxies.add(((rowIndex - a) + 1000000 * a) to ((colIndex - b) + 1000000 * b))
                }
            }
        }
        val galaxiesPairs = galaxies.flatMapIndexed { index, pair ->
            ((index + 1)..<galaxies.size).map {
                pair to galaxies[it]
            }.toList()
        }
        return galaxiesPairs.sumOf { (from, to) ->
            (to.first - from.first).absoluteValue + (to.second - from.second).absoluteValue
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    // check(part1(testInput) == 374)
    // check(part2(testInput) == 1030)
    // check(part2(testInput) == 8410)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}