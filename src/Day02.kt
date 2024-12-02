import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map {
                val pairs = it.split(' ').map { it.toInt() }.zipWithNext()
                pairs.all { (a, b) -> abs(a - b) in 1..3 } && (pairs.all { (a, b) -> a < b } || pairs.all { (a, b) -> a > b })
            }
            .count { it }
    }

    fun part2(input: List<String>): Int {
        return input
            .map {
                val reports = it.split(' ').map { it.toInt() }
                val combinations = reports.mapIndexed { index, i ->
                    val combination = reports.toMutableList()
                    combination.removeAt(index)
                    combination.zipWithNext()
                }

                combinations.any { pairs ->
                    pairs.all { (a, b) -> abs(a - b) in 1..3 } && (pairs.all { (a, b) -> a < b } || pairs.all { (a, b) -> a > b })
                }
            }
            .count { it }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
