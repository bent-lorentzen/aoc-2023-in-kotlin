import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {

    fun calculateWeight(grid: List<List<Char>>): Int {
        return grid.mapIndexed { y, chars ->
            chars.sumOf { c ->
                if (c == 'O') grid.size - y else 0
            }
        }.sum()
    }

    fun tiltNorth(input: List<List<Char>>): List<List<Char>> {
        val grid = input.map { it.toMutableList() }
        val availablePositions = MutableList(grid.first.size) { mutableListOf<Int>() }
        grid.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                when (c) {
                    'O' -> if (availablePositions[x].isNotEmpty()) {
                        grid[availablePositions[x].first][x] = 'O'
                        availablePositions[x].removeFirst()
                        grid[y][x] = '.'
                        availablePositions[x].add(y)
                    }

                    '.' -> availablePositions[x].add(y)
                    '#' -> availablePositions[x].clear()
                }
            }
        }
        return grid
    }

    fun tiltSouth(input: List<List<Char>>): List<List<Char>> {
        return tiltNorth(input.reversed()).reversed()
    }

    fun tiltWest(input: List<List<Char>>): List<List<Char>> {
        val grid = input.map { it.toMutableList() }
        val availablePositions = MutableList(grid.size) { mutableListOf<Int>() }
        grid.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                when (c) {
                    'O' -> if (availablePositions[y].isNotEmpty()) {
                        grid[y][availablePositions[y].first] = 'O'
                        availablePositions[y].removeFirst()
                        grid[y][x] = '.'
                        availablePositions[y].add(x)
                    }

                    '.' -> availablePositions[y].add(x)
                    '#' -> availablePositions[y].clear()
                }
            }
        }
        return grid
    }

    fun tiltEast(input: List<List<Char>>): List<List<Char>> {
        return tiltWest(input.map { it.reversed() }).map { it.reversed() }
    }

    fun cycle(grid: List<List<Char>>): List<List<Char>> {
        return tiltEast(tiltSouth(tiltWest(tiltNorth(grid))))
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList() }
        return calculateWeight(tiltNorth(grid))
    }

    fun cycleUntilRepetition(grid: List<List<Char>>): MutableList<List<List<Char>>> {
        val cycles = mutableListOf<List<List<Char>>>()
        var lastCycle = grid

        repeat(1_000_000_000) {
            lastCycle = cycle(lastCycle)
            if (cycles.contains(lastCycle)) {
                cycles.add(lastCycle)
                return cycles
            }
            cycles.add(lastCycle)
        }
        return cycles
    }

    fun findBillionthCycle(grid: List<List<Char>>): List<List<Char>> {
        val cycles = cycleUntilRepetition(grid)
        val firstIndex = cycles.indexOf(cycles.last)
        val lastIndex = cycles.lastIndex
        val indexInLoop = (1_000_000_000 - (firstIndex) - 1) % (lastIndex - firstIndex)
        return cycles[firstIndex + indexInLoop]
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toList() }
        val cycled = findBillionthCycle(grid)
        return calculateWeight(cycled)
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day14-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
