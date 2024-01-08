import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.PriorityQueue

fun main() {

    data class CityBlock(
        val id: Pair<Int, Int>,
        val cost: Int

    ) {
        override fun toString(): String {
            return "Node(id=$id, cost=$cost)"
        }
    }

    fun getMap(lines: List<List<Int>>): Map<Pair<Int, Int>, CityBlock> {
        return lines.mapIndexed { y, ints ->
            ints.mapIndexed { x, i ->
                CityBlock(y to x, i)
            }
        }.flatten().associateBy { it.id }
    }

    val validDirections = mapOf(
        Direction.DOWN to listOf(Direction.DOWN, Direction.RIGHT, Direction.LEFT),
        Direction.UP to listOf(Direction.RIGHT, Direction.UP, Direction.LEFT),
        Direction.LEFT to listOf(Direction.DOWN, Direction.LEFT, Direction.UP),
        Direction.RIGHT to listOf(Direction.RIGHT, Direction.DOWN, Direction.UP)
    )

    data class Arrival(val cityBlock: CityBlock, val lastDirection: Direction, val steps: Int)
    data class Path(val arrival: Arrival, val cost: Int)

    fun findShortestPath2(
        grid: List<List<Int>>,
        minStep: Int = 1,
        maxStep: Int = 3
    ): Int {
        val cityMap = getMap(grid)

        val first = cityMap[0 to 0]!!
        val target = cityMap[grid.first().lastIndex to grid.lastIndex]!!

        val queue = PriorityQueue { o1: Path, o2: Path -> o1.cost.compareTo(o2.cost) }
        queue.add(Path(Arrival(first, Direction.DOWN, 0), 0))
        queue.add(Path(Arrival(first, Direction.RIGHT, 0), 0))
        val visits = mutableSetOf<Arrival>()

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.arrival.cityBlock == target && current.arrival.steps >= minStep) return current.cost

            validDirections[current.arrival.lastDirection]!!.filterNot {
                current.arrival.steps < minStep && it != current.arrival.lastDirection ||
                        current.arrival.lastDirection == it && current.arrival.steps == maxStep
            }.mapNotNull {
                val nextBlock = cityMap[it.action(current.arrival.cityBlock.id)]
                if (nextBlock == null) null else Arrival(nextBlock, it, if (it == current.arrival.lastDirection) current.arrival.steps + 1 else 1)
            }.forEach {
                if (it !in visits) {
                    queue.add(Path(it, current.cost + it.cityBlock.cost))
                    visits.add(it)
                }
            }
        }
        return Int.MAX_VALUE
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.map { c -> c.toString().toInt() } }
        return findShortestPath2(grid)
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.map { c -> c.toString().toInt() } }
        return findShortestPath2(grid, 4, 10)
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day17-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
