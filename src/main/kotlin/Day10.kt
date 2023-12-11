import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {
    fun findFirstStep(grid: List<List<Char>>, s: Pair<Int, Int>): Pair<Int, Int> {
        return when {
            grid[s.first + 1][s.second] in setOf('J', 'L', '|') -> s.first + 1 to s.second
            grid[s.first][s.second + 1] in setOf('J', '7', '-') -> s.first to s.second + 1
            grid[s.first - 1][s.second] in setOf('F', '7', '|') -> s.first - 1 to s.second
            grid[s.first][s.second - 1] in setOf('F', 'L', '-') -> s.first to s.second - 1
            else -> error("Broken pipe")
        }
    }

    fun findTypeOfS(grid: List<List<Char>>, s: Pair<Int, Int>): Char {
        val maxY = grid.lastIndex
        val maxX = grid.first.lastIndex
        when (s) {
            (0 to 0) -> return 'F'
            (0 to maxX) -> return '7'
            (maxY to 0) -> return 'L'
            (maxY to maxX) -> return 'J'
        }

        if (s.first == 0) {
            return if (grid[1][s.second] in setOf('J', 'L', '|')) {
                if (grid[0][s.second + 1] in setOf('-', '7')) 'F' else '7'
            } else '-'
        }
        if (s.second == 0) {
            return if (grid[s.first][1] in setOf('7', 'J', '-')) {
                if (grid[s.first + 1][0] in setOf('|', 'L')) 'F' else 'L'
            } else '|'
        }
        if (s.first == maxY) {
            return if (grid[maxY - 1][s.second] in setOf('7', 'F', '|')) {
                if (grid[maxY][s.second + 1] in setOf('-', 'J')) 'L' else 'J'
            } else '-'
        }
        if (s.second == maxX) {
            return if (grid[s.first][maxX - 1] in setOf('L', 'F', '-')) {
                if (grid[s.first + 1][maxX] in setOf('|', 'J')) '7' else 'J'
            } else '|'
        }
        return when {
            grid[s.first + 1][s.second] in setOf('J', 'L', '|') -> if (grid[s.first][s.second + 1] in setOf('-', '7')) 'F' else '7'
            grid[s.first][s.second + 1] in setOf('J', '7', '-') -> if (grid[s.first + 1][s.second] in setOf('|', 'L')) 'F' else 'L'
            grid[s.first - 1][s.second] in setOf('F', '7', '|') -> if (grid[s.first][s.second + 1] in setOf('-', 'J')) 'L' else 'J'
            grid[s.first][s.second - 1] in setOf('F', 'L', '-') -> if (grid[s.first + 1][s.second] in setOf('|', 'J')) '7' else 'J'
            else -> error("Broken pipe")
        }
    }

    fun findNextStep(grid: List<List<Char>>, position: Pair<Int, Int>, previousPosition: Pair<Int, Int>): Pair<Int, Int> {
        return when (grid[position.first][position.second]) {
            '|' -> if (previousPosition.first < position.first) position.first + 1 to position.second else position.first - 1 to position.second
            '-' -> if (previousPosition.second < position.second) position.first to position.second + 1 else position.first to position.second - 1
            'L' -> if (previousPosition.first < position.first) position.first to position.second + 1 else position.first - 1 to position.second
            'J' -> if (previousPosition.first < position.first) position.first to position.second - 1 else position.first - 1 to position.second
            '7' -> if (previousPosition.first > position.first) position.first to position.second - 1 else position.first + 1 to position.second
            'F' -> if (previousPosition.first > position.first) position.first to position.second + 1 else position.first + 1 to position.second
            else -> error("Ruptured pipe")
        }
    }

    fun part1(input: List<String>): Int {
        var position = Pair(0, 0)
        val grid: List<List<Char>> = input.mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (c == 'S') position = Pair(y, x)
                c
            }
        }
        val start = position
        var previousPosition = position
        var steps = 0
        do {
            if (steps == 0) {
                position = findFirstStep(grid, start)
            } else {
                val newPosition = findNextStep(grid, position, previousPosition)
                previousPosition = position
                position = newPosition
            }
            steps++
        } while (position != start)
        return steps / 2
    }

    fun part2(input: List<String>): Int {
        var position = Pair(0, 0)
        val grid: List<List<Char>> = input.mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (c == 'S') position = Pair(y, x)
                c
            }
        }
        val start = position
        var previousPosition = position
        val positions = mutableSetOf<Pair<Int, Int>>()
        do {
            if (positions.isEmpty()) {
                position = findFirstStep(grid, start)
            } else {
                val newPosition = findNextStep(grid, position, previousPosition)
                previousPosition = position
                position = newPosition
            }
            positions.add(position)
        } while (position != start)

        return grid.mapIndexed { y, line ->
            var enclosedByLoop = false
            var entry = ' '
            line.mapIndexed { x, c ->
                if (positions.contains(y to x)) {
                    val pipe = if (c != 'S') c else findTypeOfS(grid, start)
                    if (pipe in setOf('L', 'F')) entry = c
                    if (pipe == 'J' && entry == 'F') enclosedByLoop = !enclosedByLoop
                    if (pipe == '7' && entry == 'L') enclosedByLoop = !enclosedByLoop
                    if (pipe == '|') enclosedByLoop = !enclosedByLoop
                    0
                } else {
                    if (enclosedByLoop) 1 else 0
                }
            }.sum()
        }.sum()
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    val input = readLines("day10-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
