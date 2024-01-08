import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {

    fun Direction.toSymbol(): String {
        return when (this) {
            Direction.UP -> "\u2191"
            Direction.DOWN -> "\u2193"
            Direction.LEFT -> "\u2190"
            Direction.RIGHT -> "\u2192"
            Direction.UP_LEFT -> "\u2196"
            Direction.UP_RIGHT -> "\u2197"
            Direction.DOWN_LEFT -> "\u2199"
            Direction.DOWN_RIGHT -> "\u2198"
        }
    }

    fun bridgeDirections(direction: Direction, nextDirection: Direction): Direction {
        return when (direction) {
            Direction.UP -> if (nextDirection == Direction.LEFT) Direction.UP_LEFT else Direction.UP_RIGHT
            Direction.DOWN -> if (nextDirection == Direction.RIGHT) Direction.DOWN_RIGHT else Direction.DOWN_LEFT
            Direction.LEFT -> if (nextDirection == Direction.UP) Direction.UP_LEFT else Direction.DOWN_LEFT
            Direction.RIGHT -> if (nextDirection == Direction.DOWN) Direction.DOWN_RIGHT else Direction.UP_RIGHT
            else -> error("Invalid direction")
        }
    }

    fun splitParts(s: String): Triple<Direction, Long, String> {
        val parts = s.split(" ")
        return Triple(Direction.fromString(parts.first), parts[1].toLong(), parts[2])

    }

    fun calculateSizeOfPool(data: List<Pair<Direction, Long>>): Long {
        var currentPosition = 0L to 0L

        val horizontalDitches = mutableMapOf<Long, MutableList<Pair<LongRange, Direction>>>()
        val verticalDitches = mutableMapOf<Long, MutableList<Pair<LongRange, Direction>>>()

        data.forEachIndexed { dataRow, triple ->
            val nextDirection = if (dataRow == data.lastIndex) data.first.first else data[dataRow + 1].first
            if (triple.first == Direction.RIGHT) {
                if (horizontalDitches[currentPosition.first] == null) horizontalDitches[currentPosition.first] = mutableListOf()
                if (triple.second != 1L) {
                    horizontalDitches[currentPosition.first]!!
                        .add((currentPosition.second + 1..<currentPosition.second + triple.second) to triple.first)
                }
                currentPosition = currentPosition.first to currentPosition.second + triple.second
                horizontalDitches[currentPosition.first]!!.add(
                    (currentPosition.second..currentPosition.second) to bridgeDirections(triple.first, nextDirection)
                )
            }
            if (triple.first == Direction.LEFT) {
                if (horizontalDitches[currentPosition.first] == null) horizontalDitches[currentPosition.first] = mutableListOf()
                if (triple.second != 1L) {
                    horizontalDitches[currentPosition.first]!!
                        .add((currentPosition.second - (triple.second) + 1..<currentPosition.second) to triple.first)
                }
                currentPosition = currentPosition.first to currentPosition.second - triple.second
                horizontalDitches[currentPosition.first]!!.add(
                    (currentPosition.second..currentPosition.second) to bridgeDirections(triple.first, nextDirection)
                )
            }
            if (triple.first == Direction.DOWN) {
                if (verticalDitches[currentPosition.second] == null) verticalDitches[currentPosition.second] = mutableListOf()
                if (triple.second != 1L) {
                    verticalDitches[currentPosition.second]!!.add((currentPosition.first + 1..<currentPosition.first + triple.second) to triple.first)
                }
                currentPosition = currentPosition.first + triple.second to currentPosition.second
                if (horizontalDitches[currentPosition.first] == null) horizontalDitches[currentPosition.first] = mutableListOf()
                horizontalDitches[currentPosition.first]!!.add(
                    (currentPosition.second..currentPosition.second) to bridgeDirections(triple.first, nextDirection)
                )
            }
            if (triple.first == Direction.UP) {
                if (verticalDitches[currentPosition.second] == null) verticalDitches[currentPosition.second] = mutableListOf()
                if (triple.second != 1L) {
                    verticalDitches[currentPosition.second]!!
                        .add((currentPosition.first - (triple.second) + 1..<currentPosition.first) to triple.first)
                }
                currentPosition = currentPosition.first - triple.second to currentPosition.second
                if (horizontalDitches[currentPosition.first] == null) horizontalDitches[currentPosition.first] = mutableListOf()
                horizontalDitches[currentPosition.first]!!.add(
                    (currentPosition.second..currentPosition.second) to bridgeDirections(triple.first, nextDirection)
                )
            }
        }

        val sortedVertical = verticalDitches.toSortedMap()

        val sortedHorizontal = horizontalDitches.toSortedMap()
        var previous: Long? = null
        val inBetweens = sortedHorizontal.mapNotNull {
            sortedVertical.forEach { entry: Map.Entry<Long, MutableList<Pair<LongRange, Direction>>> ->
                entry.value.firstOrNull { pair: Pair<LongRange, Direction> ->
                    it.key in pair.first
                }?.also { pair ->
                    sortedHorizontal[it.key]!!.add(entry.key..entry.key to pair.second)
                }
            }

            if (previous == null || (previous!! + 1) == it.key) {
                previous = it.key
                null
            } else {
                (previous!! + 1..<it.key).also { _ -> previous = it.key }
            }
        }
        val inBetweensSize = inBetweens.sumOf {
            val intersections = sortedVertical.mapNotNull { entry: Map.Entry<Long, MutableList<Pair<LongRange, Direction>>> ->
                if (entry.value.any { pair: Pair<LongRange, Direction> -> it.first in pair.first }) {
                    entry.key
                } else {
                    null
                }
            }
            (0..intersections.lastIndex).mapNotNull { index ->
                if (index % 2 == 0) {
                    null
                } else {
                    intersections[index] - intersections[index - 1] + 1
                }
            }.sum() * (it.last - it.first + 1)
        }

        sortedHorizontal.forEach { entry ->
            entry.value.sortBy { it.first.first }
        }

        return sortedHorizontal.entries.sumOf { entry ->
            var enclosedByDitch = false
            var enclosureEntry: Long? = null
            var ditchEntry: Pair<LongRange, Direction>? = null
            val linesum: Long = entry.value.sumOf { ditchPosition: Pair<LongRange, Direction> ->
                when (ditchPosition.second) {
                    Direction.UP,
                    Direction.DOWN -> {
                        enclosedByDitch = !enclosedByDitch
                        if (enclosedByDitch) {
                            enclosureEntry = ditchPosition.first.first
                            1
                        } else {
                            val volume = ditchPosition.first.first - enclosureEntry!!
                            enclosureEntry = null
                            volume
                        }
                    }

                    Direction.DOWN_LEFT,
                    Direction.DOWN_RIGHT -> {
                        if (ditchEntry == null) {
                            ditchEntry = ditchPosition.first to ditchPosition.second
                            if (enclosedByDitch) {
                                val volume = ditchPosition.first.first - enclosureEntry!!
                                enclosureEntry = null
                                volume
                            } else {
                                1L
                            }
                        } else {
                            if (ditchEntry!!.second in setOf(Direction.DOWN_RIGHT, Direction.DOWN_LEFT)) {
                                enclosedByDitch = !enclosedByDitch
                            }
                            if (enclosedByDitch) {
                                enclosureEntry = ditchPosition.first.first
                            }
                            ditchEntry = null
                            1L
                        }
                    }

                    Direction.UP_RIGHT,
                    Direction.UP_LEFT -> {
                        if (ditchEntry == null) {
                            ditchEntry = ditchPosition
                            if (enclosedByDitch) {
                                val volume = ditchPosition.first.first - enclosureEntry!!
                                enclosureEntry = null
                                volume
                            } else {
                                1L
                            }
                        } else {
                            if (ditchEntry!!.second in setOf(Direction.UP_RIGHT, Direction.UP_LEFT)) {
                                enclosedByDitch = !enclosedByDitch
                            }
                            if (enclosedByDitch) {
                                enclosureEntry = ditchPosition.first.first
                            }
                            ditchEntry = null
                            1L
                        }
                    }

                    Direction.LEFT, Direction.RIGHT -> (ditchPosition.first.last - ditchPosition.first.first + 1)
                }
            }
            linesum
        } + inBetweensSize

    }

    fun part1(input: List<String>): Long {
        val data = input.map { splitParts(it) }.map { it.first to it.second }
        return calculateSizeOfPool(data)
    }

    fun part2(input: List<String>): Long {
        val data = input.map { splitParts(it) }.map {
            val hexString = it.third.trim('#', '(', ')')
            val direction = when (hexString.last()) {
                '0' -> Direction.RIGHT
                '1' -> Direction.DOWN
                '2' -> Direction.LEFT
                '3' -> Direction.UP
                else -> error("Input failure!")
            }
            val number = hexString.substring(0, 5).toLong(16)
            direction to number
        }
        return calculateSizeOfPool(data)
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day18-input.txt")

    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
