fun main() {
    fun part1(input: List<String>): Int {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        return input.sumOf { line ->
            val matches = regex.findAll(line)
            matches.sumOf {
                val (a, b) = it.groupValues.drop(1).map { it.toInt() }
                a * b
            }
        }
    }

    fun part2(input: List<String>): Int {
        val regex = Regex("(mul)\\((\\d+),(\\d+)\\)|(do)\\(\\)|(don't)\\(\\)")
        var sum = 0
        var doSum = true

        input.forEach { line ->
            val matches = regex.findAll(line)
            matches.forEach { match ->
                when {
                    match.groups.get(1) != null -> {
                        val (a, b) = match.groupValues.drop(2).take(2).map { it.toInt() }
                        if (doSum) {
                            sum += a * b
                        }
                    }
                    match.groups.get(4) != null -> {
                        doSum = true
                    }
                    match.groups.get(5) != null -> {
                        doSum = false
                    }
                }
            }
        }

        return sum
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
