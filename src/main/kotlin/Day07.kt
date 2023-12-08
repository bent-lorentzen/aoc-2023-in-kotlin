import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.pow

@Suppress("RegExpSuspiciousBackref")
fun main() {
    val fiveOfAKindMatcherScore = Regex("(.)\\1{4}") to 6_000_000
    val fourOfAKindMatcherScore = Regex(".*(.)(.*\\1.*){3}") to 5_000_000
    val fullHouseMatcherScore = Regex("(?=.*(.)(.*\\1.*){2})(?=.*((?!\\1).)(.*\\3.*)).+") to 4_000_000
    val threeOfAKindMatcherScore = Regex(".*(.)(.*\\1.*){2}") to 3_000_000
    val twoPairMatcherScore = Regex("(?=.*(.)(.*\\1.*))(?=.*((?!\\1).)(.*\\3.*)).+") to 2_000_000
    val onePairMatcherScore = Regex(".*(.)(.*\\1.*)") to 1_000_000
    val highCardMatcherScore = Regex(".*") to 0

    val fiveOfAKindMatcherScoreJoker = Regex("(?=.*([^J]).*)(\\1|J){5}|JJJJJ") to 6_000_000
    val fourOfAKindMatcherScoreJoker = Regex(".*(.)(.*\\1.*){3}|(?=.*J.*).*(.)(.*\\3.*){2}|(?=.*J.*J.*).*([^J])(.*\\5.*)") to 5_000_000
    val fullHouseMatcherScoreJoker =
        Regex("(?=.*(.)(.*\\1.*){2})(?=.*((?!\\1).)(.*\\3.*)).+|(?=.*J.*)(?=.*(.)(.*\\5.*))(?=.*((?!\\5).)(.*\\7.*)).+") to 4_000_000
    val threeOfAKindMatcherScoreJoker = Regex(".*(.)(.*\\1.*){2}|(?=.*J.*).*(.)(.*\\3.*)") to 3_000_000
    val onePairMatcherScoreJoker = Regex(".*(.)(.*\\1.*)|.*J.*") to 1_000_000

    val handMatchers = listOf(
        fiveOfAKindMatcherScore,
        fourOfAKindMatcherScore,
        fullHouseMatcherScore,
        threeOfAKindMatcherScore,
        twoPairMatcherScore,
        onePairMatcherScore,
        highCardMatcherScore
    )
    val handMatchersJoker = listOf(
        fiveOfAKindMatcherScoreJoker,
        fourOfAKindMatcherScoreJoker,
        fullHouseMatcherScoreJoker,
        threeOfAKindMatcherScoreJoker,
        twoPairMatcherScore,
        onePairMatcherScoreJoker,
        highCardMatcherScore
    )
    val cardMatchers = ((2..9).map { "$it" to it } + listOf("T" to 10, "J" to 11, "Q" to 12, "K" to 13, "A" to 14)).toMap()
    val cardMatchersWithJoker = ((2..9).map { "$it" to it } + listOf("T" to 10, "J" to 1, "Q" to 12, "K" to 13, "A" to 14)).toMap()

    fun computeScore(hand: String) = handMatchers.first { hand.matches(it.first) }.second +
            hand.mapIndexed { index, card: Char -> cardMatchers[card.toString()]!! * (50625 / 15.0.pow(index).toInt()) }.sum()

    fun computeScoreJoker(hand: String) =
        handMatchersJoker.first { hand.matches(it.first) }.second +
                hand.mapIndexed { index, card: Char -> cardMatchersWithJoker[card.toString()]!! * (50625 / 15.0.pow(index).toInt()) }.sum()

    fun part1(input: List<String>): Int {
        return input.map {
            val elements = it.split(" ")
            val hand = elements.first
            val bid = elements[1]
            computeScore(hand) to bid
        }.sortedBy {
            it.first
        }.mapIndexed { index, pair ->
            (index + 1) * pair.second.toInt()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val elements = it.split(" ")
            val hand = elements.first
            val bid = elements[1]
            computeScoreJoker(hand) to bid
        }.sortedBy {
            it.first
        }.mapIndexed { index, pair ->
            (index + 1) * pair.second.toInt()
        }.sum()
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input =
        readLines("day07-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
