import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.min

fun main() {

    fun getValues(input: List<String>) =
        input.map {
            val list = it.split(" ")
            list.first() to list[1]
        }.map {
            it.first to it.second.split(",").map { s -> s.toInt() }
        }

    fun permut4(s: String, groupSizes: List<Int>, localMaxPoints: Int): Long {

        val remainingGroups = groupSizes.indices.associate { index ->
            val numberOfGroups = groupSizes.lastIndex
            val sizedOfRemainingGroups = groupSizes.mapIndexed { ix, i -> if (ix > index - 1) i else 0 }.sum()
            val sizeOfMinimumGroupsSeparators = (numberOfGroups - index)
            index - 1 to sizedOfRemainingGroups + sizeOfMinimumGroupsSeparators
        }

        fun permuteString(
            s: CharSequence,
            currentChar: Char,
            previous: Char?,
            index: Int,
            groupNumber: Int,
            localPointCount: Int,
            hashCount: Int
        ): Long {
            // Check for enough space for remaining groups
            if (s.length - index < (remainingGroups[groupNumber] ?: 0))
                return 0
            if (index == s.length) {
                return 1
            }

            return when (currentChar) {
                '.' -> {
                    if (previous == '#' && hashCount != groupSizes[groupNumber]) return 0
                    if (previous == '.' && localPointCount == localMaxPoints) return 0
                    permuteString(s, s[min(index + 1, s.lastIndex)], currentChar, index + 1, groupNumber, localPointCount + 1, 0)
                }
                '#' -> {
                    if (previous == '.' && groupNumber == groupSizes.lastIndex) return 0
                    if (previous == '#' && hashCount == groupSizes[groupNumber]) return 0
                    permuteString(
                        s,
                        s[min(index + 1, s.lastIndex)],
                        currentChar,
                        index + 1,
                        if (previous == '#') groupNumber else groupNumber + 1,
                        0,
                        hashCount + 1
                    )
                }

                else -> {
                    permuteString(s, '#', previous, index, groupNumber, localPointCount, hashCount) +
                            permuteString(s, '.', previous, index, groupNumber, localPointCount, hashCount)
                }
            }
        }
        return permuteString(s, s[0], null, 0, -1, 0, 0)
    }

    fun countPermutations(recordToGroups: Pair<String, List<Int>>): Long {
        val groupSizes = recordToGroups.second
        val periodCount = recordToGroups.first.length - groupSizes.sum()
        val maxPeriodLength = periodCount - (groupSizes.size - 1) + 1
        val temp = permut4(recordToGroups.first, groupSizes, maxPeriodLength)
        return temp
    }

    fun part1(input: List<String>): Long {
        val lines = getValues(input)
        var count = 0
        return lines.sumOf {
            count++
            val temp = countPermutations(it)
            temp
        }
    }

    fun part2(input: List<String>): Long {
        val lines = getValues(input)
        var count = 0
        val newLines = lines.map {
            (0..4).joinToString("?") { _ ->
                val temp = it.first
                temp
            } to (0..4).map { _ -> it.second }.flatten()
        }

        return newLines.sumOf {
            val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
            count++
            val temp = countPermutations(it)
            println("$count = $temp  ${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
            temp
        }
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input =
        readLines("day12-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
