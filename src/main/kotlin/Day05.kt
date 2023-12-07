import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.SortedMap

fun main() {

    fun readSeads(line: String) = line.substringAfter(": ").split(Regex(" ")).filterNot { it.isEmpty() }.map { it.toLong() }

    fun readSeadRanges(line: String): List<LongRange> {
        val numbers = line.substringAfter(": ").split(Regex(" ")).filterNot { it.isEmpty() }.map { it.toLong() }
        return (0..numbers.lastIndex / 2).mapIndexed { index, i ->
            val from = numbers[index * 2]
            val to = from + numbers[(index * 2) + 1]
            (from..to)
        }
    }

    fun readMap(label: String, lines: List<String>): Map<LongRange, Long> {
        val maplabelIndex = lines.indexOf(label)
        val sublist = lines.subList(maplabelIndex + 1, lines.size)
        val mapEndBlank = sublist.indexOf("")
        val ranges: List<Pair<LongRange, Long>> =
            sublist.subList(0, if (mapEndBlank > 0) mapEndBlank else sublist.size).map {
                val entry = it.split(" ")
                val sorceDestinationOffset = entry[0].toLong() - entry[1].toLong()
                (entry[1].toLong()..<entry[1].toLong() + entry[2].toLong()) to sorceDestinationOffset
            }.sortedBy { it.first.first }
        var previousRangeEnd = -1L
        val inBetweenRanges: List<Pair<LongRange, Long>> = ranges.mapNotNull {
            if (it.first.first > previousRangeEnd + 1) {
                val inBetweenRange = (previousRangeEnd + 1..<it.first.first) to 0L
                previousRangeEnd = it.first.last
                inBetweenRange
            } else if (it == ranges.last) {
                (it.first.last + 1..10_000_000_000) to 0L
            } else {
                previousRangeEnd = it.first.last
                null
            }
        }
        return (ranges + inBetweenRanges).toMap().toSortedMap { o1, o2 -> o1.first.compareTo(o2.first) }
    }

    fun mergeMaps(sourceMap: Map<LongRange, Long>, targetMap: Map<LongRange, Long>): SortedMap<LongRange, Long> {
        return sourceMap.entries.map { sourceEntry ->
            val newRanges = mutableMapOf<LongRange, Long>()
            var targetRangeLast = -1L
            var newFirst = sourceEntry.key.first
            while (targetRangeLast < sourceEntry.key.last + sourceEntry.value) {
                targetMap.entries.first { newFirst + sourceEntry.value in it.key }.let { targetRange ->
                    if (targetRange.key.last >= sourceEntry.key.last + sourceEntry.value) {
                        newRanges[newFirst..sourceEntry.key.last] = sourceEntry.value + targetRange.value
                        targetRangeLast = targetRange.key.last
                    } else {
                        targetRangeLast = targetRange.key.last
                        newRanges[newFirst..(targetRangeLast - sourceEntry.value)] = sourceEntry.value + targetRange.value
                        newFirst = targetRangeLast - sourceEntry.value + 1
                    }
                }
            }
            newRanges
        }.fold(emptyMap<LongRange, Long>()) { acc, mutableMap ->
            acc + mutableMap
        }.toSortedMap { o1, o2 -> o1.first.compareTo(o2.first) }

    }

    fun seedToLocationMap(input: List<String>): Map<LongRange, Long> {
        val mapLabels = listOf(
            "seed-to-soil map:",
            "soil-to-fertilizer map:",
            "fertilizer-to-water map:",
            "water-to-light map:",
            "light-to-temperature map:",
            "temperature-to-humidity map:",
            "humidity-to-location map:"
        )
        return mapLabels.reversed().fold(mapOf(0..10_000_000_000L to 0L)) { acc, mapLabel ->
            val sorceMap = readMap(mapLabel, input)
            mergeMaps(sorceMap, acc)
        }
    }


    fun part1(input: List<String>): Long {
        val seeds = readSeads(input.first)
        val seedToLocationMap = seedToLocationMap(input)
        return seeds.minOf { seed ->
            seedToLocationMap.entries.filter { seed in it.key }.let { seed + it.first.value }
        }
    }

    fun part2(input: List<String>): Long {
        val seedRanges: List<LongRange> = readSeadRanges(input.first).sortedBy { it.first }
        val seedToLocationMap = seedToLocationMap(input)
        val modifiedSeedRanges = seedRanges.map { seedRange ->
            val modifiedRanges = mutableListOf<LongRange>()
            var seedToLocationRange = (-1L..-1L)
            var newFirst = seedRange.first
            while (seedToLocationRange.last < seedRange.last) {
                seedToLocationRange = seedToLocationMap.keys.first { newFirst in it }
                if (seedToLocationRange.last > seedRange.last) {
                    modifiedRanges.add(newFirst..seedRange.last)
                } else {
                    modifiedRanges.add(newFirst..seedToLocationRange.last)
                    newFirst = seedToLocationRange.last + 1
                }
            }
            modifiedRanges
        }.flatten()

        return modifiedSeedRanges.minOf { seedRange ->
            seedToLocationMap.entries.filter { seedRange.first in it.key }.let { seedRange.first + it.first.value }
        }
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    val input = readLines("day05-input.txt")
    seedToLocationMap(input)
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
