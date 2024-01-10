import java.time.LocalDateTime
import java.time.ZoneOffset


fun main() {

    class Rule(val category: Char, val limit: Int, val greater: Boolean, val result: String) {
        fun run(part: Map<Char, Int>): String? {
            return if (greater) {
                if (part[category]!! > limit) result else null
            } else {
                if (part[category]!! < limit) result else null
            }
        }
    }

    fun parseWorkflows(input: List<String>): Map<String, List<Rule>> {
        return input.associate {
            val items = it.split('{', '}', ',').filterNot { s -> s.isBlank() }
            val id = items.first
            val rules = items.subList(1, items.size).map { s ->
                if (s.contains(':')) {
                    val parts = s.split('<', '>', ':')
                    val greater = s[1] == '>'
                    val limit = parts[1].toInt()
                    Rule(s.first(), limit, greater, parts.last)
                } else {
                    Rule('x', 0, true, s)
                }
            }
            id to rules
        }
    }

    fun parseParts(input: List<String>): List<Map<Char, Int>> {
        return input.map {
            val items = it.split('{', '}', ',').filterNot { s -> s.isBlank() }
            items.associateBy({ item -> item.first() }, { item -> item.substringAfter('=').toInt() })
        }
    }

    fun part1(input: List<String>): Int {
        val divider = input.indexOf("")
        val workflows: Map<String, List<Rule>> = parseWorkflows(input.subList(0, divider))
        val parts = parseParts(input.subList(divider + 1, input.size))
        return parts.sumOf {
            var result = "in"
            while (result !in setOf("A", "R")) {
                val temp: List<Rule> = workflows[result]!!
                result = temp.firstNotNullOf { rule -> rule.run(it) }
            }

            if (result == "A") {
                it.values.sum()
            } else {
                0
            }
        }
    }

    fun getAcceptedMasks(
        workflows: Map<String, List<Rule>>,
    ): List<Pair<String, List<Pair<Char, IntRange>>>> {

        fun permuteRules(
            rules: List<Rule>,
            masks: List<Pair<Char, IntRange>>
        ): List<Pair<String, List<Pair<Char, IntRange>>>> {

            val rule = rules.first()
            if (rule.limit == 0) {
                return if (rule.result in setOf("A", "R")) {
                    listOf(rule.result to masks)
                } else {
                    permuteRules(workflows[rule.result]!!, masks)
                }
            }
            return if (rule.greater) {
                if (rule.result in setOf("A", "R")) {
                    listOf(rule.result to (masks + (rule.category to rule.limit + 1..4000))) +
                            permuteRules(rules.subList(1, rules.size), masks + (rule.category to (1..rule.limit)))
                } else {
                    permuteRules(workflows[rule.result]!!, masks + (rule.category to (rule.limit + 1..4000))) +
                            permuteRules(rules.subList(1, rules.size), masks + (rule.category to (1..rule.limit)))
                }
            } else {
                if (rule.result in setOf("A", "R")) {
                    listOf(rule.result to (masks + (rule.category to (1..<rule.limit)))) +
                            permuteRules(rules.subList(1, rules.size), masks + (rule.category to (rule.limit..4000)))
                } else {
                    permuteRules(workflows[rule.result]!!, masks + (rule.category to (1..<rule.limit))) +
                            permuteRules(rules.subList(1, rules.size), masks + (rule.category to (rule.limit..4000)))
                }
            }
        }

        return permuteRules(workflows["in"]!!, listOf('x' to (1..4000), 'm' to (1..4000), 'a' to (1..4000), 's' to (1..4000)))

    }

    fun part2(input: List<String>): Long {
        val divider = input.indexOf("")
        val workflows: Map<String, List<Rule>> = parseWorkflows(input.subList(0, divider))
        val partMask = getAcceptedMasks(workflows)
        val res = partMask.filter { it.first == "A" }.sumOf { pair ->
            pair.second.groupBy({ it.first }, { it.second })
                .values.map {
                    it.fold((1..4000).toSet()) { acc, intRange ->
                        acc.intersect(intRange)
                    }
                }.fold(1L) { acc, ints ->
                    acc * ints.size
                }
        }
        return res
    }

    val timer = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    val input =
        readLines("day19-input.txt")
    val result1 = part1(input)
    "Result1: $result1".println()
    val result2 = part2(input)
    "Result2: $result2".println()
    println("${LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - timer} ms")
}
