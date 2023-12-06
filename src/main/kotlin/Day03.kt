import java.lang.Integer.max
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.min

fun main() {

    fun findNumbers(line: String): List<Pair<String, Int>> {
        var startIndex = 0

        return line.split(Regex("\\D+"))
            .filterNot { it.isEmpty() }
            .map {
                val index = line.indexOf(it, startIndex)
                startIndex = index + it.length
                it to index
            }
    }

    fun part1(input: List<String>): Int {
        val symbolPositions: Map<Int, List<Int>> = input.mapIndexed { lineNumber, line ->
            var startIndex = 0
            val symbolIndexes = line.split(Regex("[\\d.]+"))
                .filterNot { it.isEmpty() }
                .map {
                    val index = line.indexOf(it, startIndex)
                    startIndex = index + 1
                    index
                }
            lineNumber to symbolIndexes
        }.toMap()
        return input.mapIndexed { lineNumber, line ->
            findNumbers(line).filter {
                (max(lineNumber - 1, 0)..min(lineNumber + 1, input.size - 1)).any { index ->
                    (max(it.second - 1, 0)..it.second + it.first.length).containsAny(symbolPositions[index]!!)
                }
            }.sumOf { it.first.toInt() }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val potentialGears: Map<Int, List<Int>> = input.mapIndexed { lineNumber, line ->
            var startIndex = 0
            val gearIndexes = line.map {
                val index = line.indexOf("*", startIndex)
                startIndex = index + 1
                index
            }
            lineNumber to gearIndexes
        }.toMap()
        return input.mapIndexed { lineNumber, line ->
            findNumbers(line).map {
                (max(lineNumber - 1, 0)..min(lineNumber + 1, input.size - 1)).map { mapkey ->
                    (max(it.second - 1, 0)..it.second + it.first.length).mapNotNull { index ->
                        if (potentialGears[mapkey]!!.contains(index)) {
                            mapkey to index to it.first
                        } else {
                            null
                        }
                    }
                }.flatten()
            }.flatten()
        }.flatten()
            .groupBy({ it.first }, { it.second })
            .values
            .filter { it.size == 2 }
            .sumOf { it.first().toInt() * it[1].toInt() }

    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day03-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
