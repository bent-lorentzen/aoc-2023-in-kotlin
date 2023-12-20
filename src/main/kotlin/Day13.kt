import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {


    fun findReflectionPont(it: List<String>): Int {
        it.forEachIndexed { index, s ->
            if (index < it.lastIndex && s == it[index + 1]) {
                if (index >= it.lastIndex / 2) {
                    val listAfterMirror = it.subList(index + 1, it.size)
                    val listBeforeMirror = it.subList(index - listAfterMirror.lastIndex, index + 1).reversed()
                    if (listBeforeMirror == listAfterMirror) {
                        return (index + 1)
                    }
                } else {
                    val listBeforeMirror = it.subList(0, index + 1)
                    val listAfterMirror = it.subList(index + 1, index + listBeforeMirror.size + 1)
                    if (listBeforeMirror.reversed() == listAfterMirror) {
                        return (index + 1)
                    }
                }
            }
        }
        return 0
    }

    fun valueOfReflection(list: List<String>): Int {
        val valueOfReflectionHorizontal = findReflectionPont(list)
        return if (valueOfReflectionHorizontal != 0) {
            (valueOfReflectionHorizontal) * 100
        } else {
            findReflectionPont((0..list.first.lastIndex).map {
                list.map { s -> s[it] }.joinToString("")
            })
        }
    }

    fun smudgedCompare(s1: String, s2: String): Int {
        if (s1 == s2) return 0
        return s1.mapIndexed { index, c ->
            if (c == s2[index]) {
                0
            } else {
                1
            }
        }.sum()

    }

    fun smudgedCompare(list1: List<String>, list2: List<String>): Int {
        return list1.mapIndexed { index, s ->
            smudgedCompare(s, list2[index])
        }.sum()
    }

    fun findSmudgedReflectionPont(it: List<String>): Int {
        it.forEachIndexed { index, s ->
            if (index < it.lastIndex && smudgedCompare(s, it[index + 1]) <= 1) {
                if (index >= it.lastIndex / 2) {
                    val listAfterMirror = it.subList(index + 1, it.size)
                    val listBeforeMirror = it.subList(index - listAfterMirror.lastIndex, index + 1).reversed()

                    if (smudgedCompare(listBeforeMirror, listAfterMirror) == 1) {
                        return (index + 1)
                    }
                } else {
                    val listBeforeMirror = it.subList(0, index + 1)
                    val listAfterMirror = it.subList(index + 1, index + listBeforeMirror.size + 1).reversed()
                    if (smudgedCompare(listBeforeMirror, listAfterMirror) == 1) {
                        return (index + 1)
                    }
                }
            }
        }
        return 0
    }

    fun valueOfSmudgedReflection(list: List<String>): Int {
        val valueOfReflectionHorizontal = findSmudgedReflectionPont(list)
        return if (valueOfReflectionHorizontal != 0) {
            (valueOfReflectionHorizontal) * 100
        } else {
            findSmudgedReflectionPont((0..list.first.lastIndex).map {
                list.map { s -> s[it] }.joinToString("")
            })
        }
    }

    fun splitInput(input: List<String>): List<List<String>> {
        val patternSeparators = input.mapIndexedNotNull { index, s ->
            if (s.isEmpty()) index else null
        }

        var start = 0
        return (patternSeparators + input.size).map {
            val item = input.subList(start, it)
            start = it + 1
            item
        }
    }


    fun part1(input: List<String>): Int {
        return splitInput(input).sumOf {
            valueOfReflection(it)
        }
    }

    fun part2(input: List<String>): Int {
        return splitInput(input).sumOf {
            valueOfSmudgedReflection(it)
        }
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input = readLines("day13-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
