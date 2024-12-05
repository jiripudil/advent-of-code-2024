fun main() {
    fun parseInput(input: List<String>): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        val split = input.indexOf("")

        val orderingRules = input.subList(0, split)
            .map {
                val (a, b) = it.split('|', limit = 2).map { it.toInt() }
                Pair(a, b)
            }
            .groupBy { it.first }
            .mapValues { it.value.map { it.second} }

        val updates = input.subList(split + 1, input.size)
            .map { it.split(',').map { it.toInt() } }

        return Pair(orderingRules, updates)
    }

    fun isInOrder(list: List<Int>, orderingRules: Map<Int, List<Int>>): Boolean {
        list.forEachIndexed { index, i ->
            val orderingRule = orderingRules.getOrDefault(i, emptyList())
            val previousPages = list.subList(0, index)
            if (previousPages.intersect(orderingRule).isNotEmpty()) {
                return false
            }
        }

        return true
    }

    fun putInOrder(list: List<Int>, orderingRules: Map<Int, List<Int>>): List<Int> {
        return list.sortedWith { a, b ->
            val orderingRuleA = orderingRules.getOrDefault(a, emptyList())
            if (orderingRuleA.contains(b)) return@sortedWith -1

            val orderingRuleB = orderingRules.getOrDefault(b, emptyList())
            if (orderingRuleB.contains(a)) return@sortedWith 1

            0
        }
    }

    fun part1(input: List<String>): Int {
        val (orderingRules, updates) = parseInput(input)
        return updates
            .filter { isInOrder(it, orderingRules) }
            .sumOf { it.get(it.size / 2) }
    }

    fun part2(input: List<String>): Int {
        val (orderingRules, updates) = parseInput(input)
        return updates
            .filter { !isInOrder(it, orderingRules) }
            .map { putInOrder(it, orderingRules) }
            .sumOf { it.get(it.size / 2) }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
