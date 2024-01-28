import kotlin.properties.Delegates

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

object ConnectedPipe {
    // left to right
    private val horizontal = setOf(
        '-' to '-',
        '-' to 'J',
        '-' to '7',

        'L' to 'J',
        'L' to '7',
        'L' to '-',

        'F' to '-',
        'F' to 'J',
        'F' to '7',
    )
    // top to bottom
    private val vertical = setOf(
        '|' to '|',
        '|' to 'J',
        '|' to 'L',

        'F' to '|',
        'F' to 'J',
        'F' to 'L',

        '7' to '|',
        '7' to 'J',
        '7' to 'L',
    )

    fun canMoveTo(from: Char?, to: Char?, direction: Direction): Boolean {
        if (from == null || to == null || from == '.' || to == '.') return false
        if (from == 'S' || to == 'S') return true
        return when (direction) {
            Direction.UP -> vertical.contains(to to from)
            Direction.DOWN -> vertical.contains(from to to)
            Direction.LEFT -> horizontal.contains(to to from)
            Direction.RIGHT -> horizontal.contains(from to to)
        }
    }
}

fun main() {
    fun List<String>.getOrNull(x: Int, y: Int): Char? {
        if (x < 0 || x >= this.size) return null
        if (y < 0 || y >= this.first().length) return null
        return this[x][y]
    }

    fun searchLoop(curStep: Pair<Int,Int>, beforeSteps: List<Pair<Int,Int>>, start: Pair<Int,Int>, map: List<String>): MutableList<Pair<Int,Int>> {
        if (beforeSteps.size <= 30) {
            println("searchLoop: ${beforeSteps.joinToString(" -> ") { (x, y) ->
                map[x][y].toString()
            }}")
        }
        if (beforeSteps.isNotEmpty() && curStep == start) {
            return mutableListOf(curStep)
        }
        val (curX, curY) = curStep
        val curChar = map.getOrNull(curX, curY)
        val nextSteps = listOf(
            (curX to curY - 1) to Direction.LEFT,
            (curX to curY + 1) to Direction.RIGHT,
            (curX - 1 to curY) to Direction.UP,
            (curX + 1 to curY) to Direction.DOWN,
        ).filter { (nextStep, direction) ->
            ConnectedPipe.canMoveTo(curChar, map.getOrNull(nextStep.first, nextStep.second), direction)
                    && (nextStep == start || beforeSteps.all { it != nextStep })
        }.map { (nextStep, _) ->
            val nextBeforeSteps = beforeSteps.toMutableList().apply {
                add(curStep)
            }
            val nextSteps = searchLoop(nextStep, nextBeforeSteps, start, map)
            nextSteps
        }.maxByOrNull {
            it.size
        }
        val steps = mutableListOf<Pair<Int,Int>>()
        if (!nextSteps.isNullOrEmpty()) {
            steps.add(curStep)
            steps.addAll(nextSteps)
        }
        return steps
    }

    fun part1(input: List<String>): Int {
        var startRow by Delegates.notNull<Int>()
        var startColumn by Delegates.notNull<Int>()
        input.forEachIndexed { index, line ->
            val sIndex = line.indexOfFirst {
                it == 'S'
            }
            if (sIndex != -1) {
                startRow = index
                startColumn = sIndex
                return@forEachIndexed
            }
        }
        val start = startRow to startColumn
        val steps = searchLoop(start, emptyList(), start, input)
        steps.joinToString(" -> ") { (x, y) ->
            input[x][y].toString()
        }.println()
        return steps.size / 2
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    // check(part2(testInput) == 0)

    val input = readInput("Day10")
    part1(input).println()
    // part2(input).println()
}