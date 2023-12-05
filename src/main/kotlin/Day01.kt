import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.first { c -> c in ('1'..'9') }.minus('0') * 10 +
                    it.last { c -> c in ('1'..'9') }.minus('0')
        }
    }

    fun part2(input: List<String>): Int {
        val valueMap = mapOf(
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9,
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )
        return input.sumOf {
            val first: Pair<Int, String> = it.findAnyOf(valueMap.keys) ?: error("Digit missing")
            val last: Pair<Int, String> = it.findLastAnyOf(valueMap.keys) ?: error("Digit missing")
            valueMap[first.second]!! * 10 + valueMap[last.second]!!
        }
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val result1 = part1(readLines("day01-input.txt"))
    "Result1: $result1".println()
    val result2 = part2(readLines("day01-input.txt"))
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
