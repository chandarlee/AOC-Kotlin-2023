fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line ->
            line.split(" ").let { (cards, bid) ->
                Hand(cards) to bid.toInt()
            }
        }.sortedBy { it.first }
            .mapIndexed { index, pair ->
                (index + 1) * pair.second
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            line.split(" ").let { (cards, bid) ->
                Hand(cards, considerJoker = true) to bid.toInt()
            }
        }.sortedBy { it.first }
            .mapIndexed { index, pair ->
                (index + 1) * pair.second
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

data class Hand(
    private val cards: String,
    private val considerJoker: Boolean = false,
) : Comparable<Hand> {

    companion object {
        val strength = mapOf(
            '2' to 2,
            '3' to 3,
            '4' to 4,
            '5' to 5,
            '6' to 6,
            '7' to 7,
            '8' to 8,
            '9' to 9,
            'T' to 10,
            'J' to 11,
            'Q' to 12,
            'K' to 13,
            'A' to 14,
        )

        val strengthWithJoker = mapOf(
            'J' to 1,
            '2' to 2,
            '3' to 3,
            '4' to 4,
            '5' to 5,
            '6' to 6,
            '7' to 7,
            '8' to 8,
            '9' to 9,
            'T' to 10,
            'Q' to 11,
            'K' to 12,
            'A' to 13,
        )
    }

    init {
        require(cards.length == 5)
    }

    private val jokerCount = if(considerJoker) cards.count { it == 'J' } else 0

    private val hasJoker = jokerCount > 0

    val kind: HandType by lazy {
        val grouped = cards.groupBy { it }
        when (grouped.size) {
            1 -> HandType.FiveOfAKind
            2 -> {
                if (grouped.values.any { it.size == 4 }) {
                    if (hasJoker) HandType.FiveOfAKind else HandType.FourOfAKind
                } else {
                    if (hasJoker) HandType.FiveOfAKind else HandType.FullHouse
                }
            }
            3 -> {
                if (grouped.values.any { it.size == 3 }) {
                    if (hasJoker) HandType.FourOfAKind else HandType.ThreeOfAKind
                } else {
                    when (jokerCount) {
                        1 -> HandType.FullHouse
                        2 -> HandType.FourOfAKind
                        else -> HandType.TwoPairs
                    }
                }
            }
            4 -> if (hasJoker) HandType.ThreeOfAKind else HandType.OnePair
            else -> if (hasJoker) HandType.OnePair else HandType.HighCard
        }
    }

    override fun compareTo(other: Hand): Int {
        if (other.kind != kind) {
            return if (other.kind.ordinal > kind.ordinal) 1 else -1
        }
        val strength = if (considerJoker) strengthWithJoker else strength
        for (i in cards.indices) {
            if (cards[i] != other.cards[i]) {
                return if (strength[cards[i]]!! > strength[other.cards[i]]!!) 1 else -1
            }
        }
        return 0
    }
}

enum class HandType {
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    TwoPairs,
    OnePair,
    HighCard;
}