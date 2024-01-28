import java.lang.Math.min
import kotlin.properties.Delegates

fun main() {
    fun part1(input: List<String>): Int {
        val seeds = mutableListOf<Long>()
        val categoryMappings = mutableListOf<CategoryMapping>()
        input.forEach { line ->
             if (line.startsWith("seeds:")) {
                 seeds.addAll(line.split(":").last().trim().split(" ").map { it.trim().toLong() })
             } else if (line.isEmpty()) {
                 categoryMappings.add(CategoryMapping())
             } else if (line.endsWith("map:")) {
                 categoryMappings.last().name = line.split(" ").first().trim()
             } else {
                 line.trim().split(" ").let { (destination, source, length) ->
                     categoryMappings.last().items.add(CategoryMappingItem(source.toLong(), destination.toLong(), length.toLong()))
                 }
             }
        }
        return seeds.minOf { seed ->
            categoryMappings.fold(seed) { acc, category ->
                category.get(acc)
            }
        }.toInt()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    // check(part1(testInput) == 35)
    check(part2(testInput) == 46)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

class CategoryMapping {
    var name by Delegates.notNull<String>()
    val items = mutableListOf<CategoryMappingItem>()

    fun get(source: Long): Long {
        return items.firstNotNullOfOrNull {
            it.get(source)
        } ?: source
    }
}

class CategoryMappingItem(
    val source: Long,
    val destination: Long,
    private val length: Long,
) {
    fun get(source: Long): Long? {
        if (source in this.source ..< this.source + length) {
            return destination + (source - this.source)
        }
        return null
    }

    private val maxSource: Long = source + length - 1

    fun getRange(sourceRange: LongRange): LongRange? {
        if (sourceRange.first > maxSource
            || sourceRange.last < this.source) {
            return null
        } else if (sourceRange.first in this.source.. maxSource) {
            return (destination + (sourceRange.first - this.source))..(destination + maxSource.coerceAtMost(sourceRange.last) - this.source)
        } else if (sourceRange.last in this.source.. maxSource) {
            return (destination + this.source - sourceRange.first)..(destination + sourceRange.last - this.source)
        }
        return null
    }
}
