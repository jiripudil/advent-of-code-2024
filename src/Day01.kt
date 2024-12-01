import kotlin.math.abs

fun main() {
    fun buildLists(input: List<String>): Pair<List<Int>, List<Int>> {
        val a = mutableListOf<Int>()
        val b = mutableListOf<Int>()

        input.forEach { line ->
            val (a_no, b_no) = line.split(Regex("\\s+"))
            a.add(a_no.toInt())
            b.add(b_no.toInt())
        }

        return Pair(a, b)
    }

    fun part1(input: List<String>): Int {
        val (aUnsorted, bUnsorted) = buildLists(input)
        val a = aUnsorted.sorted()
        val b = bUnsorted.sorted()

        return a.mapIndexed { index, _ -> abs(a[index] - b[index]) }.sum()
    }

    fun part2(input: List<String>): Int {
        val (a, b) = buildLists(input)

        val bCounts = b.groupBy { it }.mapValues { it.value.size }
        return a.fold(0) { acc, i -> acc + i * bCounts.getOrElse(i, { 0 }) }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
