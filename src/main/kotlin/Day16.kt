import java.time.LocalDateTime
import java.time.ZoneOffset

enum class Direction(val action: (Pair<Int, Int>) -> Pair<Int, Int>) {
    UP({ it.first - 1 to it.second }),
    UP_LEFT({ it.first - 1 to it.second - 1 }),
    UP_RIGHT({ it.first - 1 to it.second + 1 }),
    DOWN({ it.first + 1 to it.second }),
    DOWN_LEFT({ it.first + 1 to it.second - 1 }),
    DOWN_RIGHT({ it.first + 1 to it.second + 1 }),
    LEFT({ it.first to it.second - 1 }),
    RIGHT({ it.first to it.second + 1 });

    companion object {
        fun fromString(s: String): Direction {
            return when (s) {
                "U" -> Direction.UP
                "D" -> Direction.DOWN
                "L" -> Direction.LEFT
                "R" -> Direction.RIGHT
                else -> error("Input failure: Direction")
            }
        }
    }

}

fun main() {

    fun mapEnergizedTiles(layout: List<String>, startPoint: Pair<Int, Int> = 0 to -1): List<List<MutableList<Direction>>> {
        val tiles = layout.map { s ->
            s.map { mutableListOf<Direction>() }
        }

        fun traverseTile(
            yAndX: Pair<Int, Int>,
            direction: Direction
        ) {
            val position = direction.action(yAndX)
            if (position.second < 0 || position.first < 0 || position.second > layout.first.lastIndex || position.first > layout.lastIndex) {
                return
            }
            if (tiles[position.first][position.second].contains(direction)) {
                return
            }
            tiles[position.first][position.second].add(direction)
            when (layout[position.first][position.second]) {
                '.' -> {
                    traverseTile(position, direction)
                }

                '|' -> {
                    if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                        traverseTile(position, Direction.UP)
                        traverseTile(position, Direction.DOWN)
                    } else {
                        traverseTile(position, direction)
                    }
                }

                '-' -> {
                    if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                        traverseTile(position, direction)
                    } else {
                        traverseTile(position, Direction.LEFT)
                        traverseTile(position, Direction.RIGHT)
                    }
                }

                '\\' -> {
                    when (direction) {
                        Direction.UP -> traverseTile(position, Direction.LEFT)
                        Direction.DOWN -> traverseTile(position, Direction.RIGHT)
                        Direction.LEFT -> traverseTile(position, Direction.UP)
                        Direction.RIGHT -> traverseTile(position, Direction.DOWN)
                        else -> error("Invalid direction!")
                    }
                }

                '/' -> {
                    when (direction) {
                        Direction.UP -> traverseTile(position, Direction.RIGHT)
                        Direction.DOWN -> traverseTile(position, Direction.LEFT)
                        Direction.LEFT -> traverseTile(position, Direction.DOWN)
                        Direction.RIGHT -> traverseTile(position, Direction.UP)
                        else -> error("Invalid direction!")
                    }
                }

            }
        }

        val direction = if (startPoint.second == -1) {
            Direction.RIGHT
        } else if (startPoint.first == -1) {
            Direction.DOWN
        } else if (startPoint.first == layout.size) {
            Direction.UP
        } else {
            Direction.LEFT
        }

        traverseTile(startPoint, direction)
        return tiles
    }

    fun part1(input: List<String>): Int {
        val tiles: List<List<MutableList<Direction>>> = mapEnergizedTiles(input)

        return tiles.sumOf {
            it.sumOf { list -> if (list.isEmpty()) 0 else 1 as Int }
        }

    }

    // This one might need increased. Runs with vm argument -Xss2m
    fun part2(input: List<String>): Int {

        val startPoints = (0..input.lastIndex).map { it to -1 } +
                (0..input.lastIndex).map { it to input.size } +
                (0..input.first.lastIndex).map { -1 to it } +
                (0..input.first.lastIndex).map { input.first.length to it }

        return startPoints.maxOf {
            mapEnergizedTiles(input, it).sumOf { tiles ->
                tiles.sumOf { list -> if (list.isEmpty()) 0 else 1 as Int }
            }
        }

    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day16-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
