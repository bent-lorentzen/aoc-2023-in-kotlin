import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {

    fun getValues(input: List<String>, label: String) =
        input.first { it.startsWith(label) }.substringAfter(label).split(Regex(" +")).filterNot { it.isEmpty() }.map { it.toInt() }

    fun part1(input: List<String>): Int {
        val times = getValues(input, "Time:")
        val distances = getValues(input, "Distance:")
        return times.mapIndexed { index, time ->
            (0..time).map {
                (time - it) * it
            }.count { it > distances[index] }
        }.fold(1) { acc, i ->
            acc * i
        }
    }

    fun part2(input: List<String>): Int {
        val time = getValues(input, "Time:").joinToString("").toLong()
        val distance = getValues(input, "Distance:").joinToString("").toLong()
        return (0..time).map {
            (time - it) * it
        }.count { it > distance }
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day06-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
