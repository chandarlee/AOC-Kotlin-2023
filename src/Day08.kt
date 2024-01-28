fun main() {
    fun String.getInstruction(step: Int): Char {
        return this[(step - 1) % this.length]
    }

    fun part1(input: List<String>): Int {
        val instructions = input.first()
        val nodes = buildMap {
            // AAA = (BBB, CCC)
            input.subList(2, input.size).forEach { line ->
                Regex("""(\w{3})\W*\((\w{3})\W*(\w{3})\)""").matchEntire(line)?.let { match ->
                    val (value, left, right) = match.destructured
                    put(value, Node(left, right, value))
                }
            }
        }
        var step = 0
        var node = nodes["AAA"]!!
        while (node.value != "ZZZ") {
            step++
            node = if (instructions.getInstruction(step) == 'L') {
                nodes[node.left]!!
            } else {
                nodes[node.right]!!
            }
        }
        return step
    }

    fun part2(input: List<String>): Int {
        val instructions = input.first()
        val nodes = buildMap {
            // AAA = (BBB, CCC)
            input.subList(2, input.size).forEach { line ->
                Regex("""(\w{3})\W*\((\w{3})\W*(\w{3})\)""").matchEntire(line)?.let { match ->
                    val (value, left, right) = match.destructured
                    put(value, Node(left, right, value))
                }
            }
        }
        var step = 0
        val stepNodes = nodes.entries.filter { it.key.endsWith('A') }
            .map { it.value }.toMutableList()
        while (stepNodes.any { it.value.endsWith('Z').not() }) {
            step++
            val toLeft = instructions.getInstruction(step) == 'L'
            for (index in stepNodes.indices) {
                val node = stepNodes[index]
                stepNodes[index] = if (toLeft) {
                    nodes[node.left]!!
                } else {
                    nodes[node.right]!!
                }
            }
            println("====$step ${stepNodes.joinToString { it.value }}")
        }
        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    // check(part1(testInput) == 6L)
    check(part2(testInput) == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

data class Node(
    val left: String,
    val right: String,
    val value: String,
)