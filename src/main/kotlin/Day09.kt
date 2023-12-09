import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            var ints = line.split(" ").map { it.toInt() }
            var sum = 0
            while (ints.any { it != 0 }) {
                sum += ints.last
                val zipped = ints.zipWithNext { a: Int, b: Int -> b - a }
                ints = zipped
            }
            sum
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            var ints = line.split(" ").map { it.toInt() }
            val firsts = mutableListOf<Int>()
            while (ints.any { it != 0 }) {
                firsts.add(ints.first)
                val zipped = ints.zipWithNext { a: Int, b: Int -> b - a }
                ints = zipped
            }
            firsts.reversed().fold(0) { acc, i ->
                i - acc
            } as Int
        }
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input =
        readLines("day09-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
