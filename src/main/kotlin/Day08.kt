import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {

    fun buildMap(input: List<String>) = input.subList(2, input.size)
        .map {
            it.split(Regex("\\W")).filterNot { item -> item.isEmpty() }
        }.associate { it.first() to (it[1] to it[2]) }


    fun countSteps(instructions: String, map: Map<String, Pair<String, String>>, start: String, end: String): Int {
        var currentPosition = start

        var stepCounter = 0
        while (true) {
            instructions.forEach {
                stepCounter++
                currentPosition = if (it == 'L') map[currentPosition]!!.first else map[currentPosition]!!.second
                if (currentPosition.matches(Regex(end))) {
                    return stepCounter
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val instructions = input.first
        val map = buildMap(input)
        return countSteps(instructions, map, "AAA", "ZZZ")
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first
        val map = buildMap(input)
        val startpoints = map.keys.filter { it.matches(Regex("..A")) }
        return startpoints.map {
            countSteps(instructions, map, it, "..Z") / instructions.length
        }.foldRight(1L) { i, acc ->
            acc * i
        } * instructions.length
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day08-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
