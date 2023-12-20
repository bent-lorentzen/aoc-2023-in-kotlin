import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {

    fun hash(startValue: Int, instruction: String): Int {
        return instruction.fold(startValue) { acc, c ->
            ((acc + c.code) * 17) % 256
        }
    }

    fun part1(input: List<String>): Int {
        val instructions = input.first.split(",")
        return instructions.sumOf {
            hash(0, it)
        }
    }

    fun arrangeInBoxes(input: List<String>): Map<Int, MutableList<Pair<String, Int>>> {
        val boxes = (0..255).associateWith { mutableListOf<Pair<String, Int>>() }
        val instructions = input.first.split(",")
        instructions.forEach {
            val label = it.substringBefore('=', it.substringBefore('-'))
            val remove = it.contains('-')
            val boxNumber = hash(0, label)
            if (remove) {
                val lensIndex = boxes[boxNumber]!!.indexOfFirst { lens -> lens.first == label }
                if (lensIndex >= 0) boxes[boxNumber]!!.removeAt(lensIndex)

            } else {
                val lensIndex = boxes[boxNumber]!!.indexOfFirst { lens -> lens.first == label }
                val lensPower = it.substringAfter("=").toInt()
                if (lensIndex < 0) {
                    boxes[boxNumber]!!.add(label to lensPower)
                } else {
                    boxes[boxNumber]!![lensIndex] = label to lensPower
                }
            }
        }
        return boxes
    }

    fun calculatePower(boxes: Map<Int, MutableList<Pair<String, Int>>>): Int {
        return boxes.entries.sumOf {
            it.value.foldIndexed(0 as Int) { index, acc, pair ->
                acc + (it.key + 1) * (index + 1) * pair.second
            }
        }
    }

    fun part2(input: List<String>): Int {
        val boxes = arrangeInBoxes(input)
        return calculatePower(boxes)
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input =
        readLines("day15-input.txt")

    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
