import kotlin.time.measureTimedValue

fun main() {
    fun part1(input: List<String>): Int {
        val positionedNums = mutableListOf<PositionedNum>()
        val positionedSymbols = mutableMapOf<Position,Char>()
        val numBuilder = StringBuilder()
        fun addNumIfExists(row: Int, column: Int) {
            if (numBuilder.isNotEmpty()) {
                positionedNums.add(
                    PositionedNum(
                        num = numBuilder.toString().toInt(),
                        row = row,
                        start = column - numBuilder.length,
                        end = column - 1,
                    )
                )
                numBuilder.clear()
            }
        }
        input.forEachIndexed { row, line ->
            line.forEachIndexed { column, char ->
                when {
                    char.isDigit() -> {
                        numBuilder.append(char)
                    }
                    char != '.' -> {
                        positionedSymbols[Position(row, column)] = char
                    }
                }
                if (char.isDigit().not() || column == line.length - 1) {
                    addNumIfExists(row, column)
                }
            }
        }
        fun hasSymbolAt(row: Int, column: Int): Boolean {
            return positionedSymbols.contains(Position(row, column))
        }
        return positionedNums.filter {
            hasSymbolAt(it.row, it.start - 1)
                    || hasSymbolAt(it.row, it.end + 1)
                    || (it.start - 1 .. it.end + 1).any { column ->
                        hasSymbolAt(it.row -1, column)
                                || hasSymbolAt(it.row + 1, column)
                    }
        }.sumOf { it.num }
    }

    fun part2(input: List<String>): Int {
        val positionedNums = mutableListOf<MutableList<PositionedNum>>()
        val positionedStar = mutableListOf<PositionedSymbol>()
        val numBuilder = StringBuilder()
        fun addNumIfExists(row: Int, column: Int) {
            var rowNums = positionedNums.getOrNull(row)
            if (rowNums == null) {
                rowNums = mutableListOf()
                positionedNums.add(rowNums)
            }
            if (numBuilder.isNotEmpty()) {
                rowNums.add(
                    PositionedNum(
                        num = numBuilder.toString().toInt(),
                        row = row,
                        start = column - numBuilder.length,
                        end = column - 1,
                    )
                )
                numBuilder.clear()
            }
        }
        input.forEachIndexed { row, line ->
            line.forEachIndexed { column, char ->
                when {
                    char.isDigit() -> {
                        numBuilder.append(char)
                    }
                    char == '*' -> {
                        positionedStar.add(PositionedSymbol(row, column, char))
                    }
                }
                if (char.isDigit().not() || column == line.length - 1) {
                    addNumIfExists(row, column)
                }
            }
        }
        fun filterPartNum(
            rowNums: List<PositionedNum>,
            startColumn: Int,
            endColumn: Int,
        ): List<PositionedNum> {
            return rowNums.filter {
                it.start >= startColumn && it.end <= endColumn
                        || it.start in startColumn..endColumn
                        || it.end in startColumn..endColumn
            }
        }
        return positionedStar.filter {
            it.partNumbers.addAll(filterPartNum(
                buildList {
                    addAll(positionedNums.getOrElse(it.row - 1) { emptyList() })
                    addAll(positionedNums.getOrElse(it.row) { emptyList() })
                    addAll(positionedNums.getOrElse(it.row + 1) { emptyList() })
                },
                it.column - 1,
                it.column + 1,
            ))
            it.partNumbers.size == 2
        }.sumOf {
            it.partNumbers[0].num * it.partNumbers[1].num
        }
    }

    // test if implementation meets criteria from the description, like:
    // val testInput = readInput("Day03_test")
    // check(part1(testInput) == 4361)
    // check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

data class PositionedNum(
    val num: Int,
    val row: Int,
    val start: Int,
    val end: Int,
)

data class Position(
    val row: Int,
    val column: Int,
)

data class PositionedSymbol(
    val row: Int,
    val column: Int,
    val char: Char,
    val partNumbers: MutableList<PositionedNum> = mutableListOf(),
)

