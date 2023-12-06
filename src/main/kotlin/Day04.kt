import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.max

fun main() {

    fun winningNumbers(line: String) = line.substringBefore("|").substringAfter(":").split(Regex(" +")).filterNot { it.isEmpty() }
    fun personalNumbers(line: String) = line.substringAfter("|").split(Regex(" +")).filterNot { it.isEmpty() }

    fun winCount(line: String) = personalNumbers(line).count { winningNumbers(line).contains(it) }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            winCount(line).let { if (it <= 1) it else 1 shl (it - 1) }
        }
    }

    fun part2(input: List<String>): Int {
        val winCounts = input.map {
            winCount(it)
        }
        val cardCount = mutableListOf<Int>()
        winCounts.reversed().forEach { i ->
            val sublist = cardCount.subList(max(cardCount.size - i, 0), cardCount.size)
            cardCount.add(1 + sublist.sum())
        }
        return cardCount.sum()
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    val input = readLines("day04-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
