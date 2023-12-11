import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {

    fun getValues(input: List<String>, label: String) =
        input.first { it.startsWith(label) }.substringAfter(label).split(Regex(" +")).filterNot { it.isEmpty() }.map { it.toInt() }

    fun solveEquation(distance: Long, time: Long): Pair<Int, Int> {
        val b = time
        val a = -1
        val c = -distance
        val lowLimit = (-b + sqrt(((b * b) + (-4 * a * c)).toDouble())) / (2 * a)
        val highLimit = (-b + -sqrt(((b * b) + (-4 * a * c)).toDouble())) / (2 * a)
        return floor(lowLimit).toInt() to floor(highLimit).toInt()
    }

    fun part1(input: List<String>): Int {
        val times = getValues(input, "Time:")
        val distances = getValues(input, "Distance:")
        return times.mapIndexed { index, time ->
            val lowHigh = solveEquation(distances[index].toLong(), time.toLong())
            lowHigh.second - lowHigh.first
        }.fold(1) { acc, i ->
            acc * i
        }
    }

    fun part2(input: List<String>): Int {
        val time = getValues(input, "Time:").joinToString("").toLong()
        val distance = getValues(input, "Distance:").joinToString("").toLong()
        val lowHigh = solveEquation(distance, time)
        return lowHigh.second - lowHigh.first
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day06-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
