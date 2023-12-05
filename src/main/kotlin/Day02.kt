import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {

    fun gameNumber(line: String) = line.substringBefore(":").substringAfter("Game ").toInt()
    fun games(line: String) = line.substringAfter(": ").split("; ")
    fun cubeCounts(gameStrings: List<String>) = gameStrings.map { game ->
        game.split(", ").map { cubeString ->
            val count = cubeString.substringBefore(" ").toInt()
            val colour = cubeString.substringAfter(" ")
            colour to count
        }
    }.flatten()

    fun part1(input: List<String>): Int {
        val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)
        return input.sumOf {
            val gameNumber = gameNumber(it)
            val games = games(it)
            val cubeCounts = cubeCounts(games)
            if (cubeCounts.any { pair -> limits[pair.first]!! < pair.second }) 0 else gameNumber
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val games = games(line)
            val cubeCounts = cubeCounts(games)
            cubeCounts.groupBy(
                { it.first },
                { it.second }
            ).entries.map {
                it.value.max()
            }.fold(1) { acc: Int, i: Int ->
                acc * i
            }
        }
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day02-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
