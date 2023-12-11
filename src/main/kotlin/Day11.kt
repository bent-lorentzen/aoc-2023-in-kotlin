import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs

fun main() {

    fun calculateDistances(input: List<String>, driftAdder: Long): Long {
        var emptyLines = 0L
        val galaxies = input.mapIndexedNotNull { lineNumber, s ->
            val galaxyCoordinates = s.mapIndexedNotNull { index, c ->
                if (c == '#') lineNumber + emptyLines to index else null
            }
            if (galaxyCoordinates.isEmpty()) {
                emptyLines += driftAdder
                null
            } else {
                galaxyCoordinates
            }
        }.flatten()
        val columnsWithGalaxies = galaxies.map { it.second }
        val lastColumnWithGalaxy = columnsWithGalaxies.max()
        val columnsWithoutGalaxies = (0..lastColumnWithGalaxy).filterNot { it in columnsWithGalaxies }
        return galaxies.mapIndexed { index, a ->
            galaxies.subList(index, galaxies.size).sumOf { b ->
                if (a == b) {
                    0
                } else {
                    val correctedPositionForA = columnsWithoutGalaxies.count { it < a.second } * driftAdder + a.second
                    val correctedPositionForB = columnsWithoutGalaxies.count { it < b.second } * driftAdder + b.second
                    abs(a.first - b.first) + abs(correctedPositionForA - correctedPositionForB)
                }
            }
        }.sum()
    }

    fun part1(input: List<String>): Long {
        return calculateDistances(input, 1L)
    }

    fun part2(input: List<String>): Long {
        return calculateDistances(input, 1_000_000L - 1)
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input =
        readLines("day11-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
