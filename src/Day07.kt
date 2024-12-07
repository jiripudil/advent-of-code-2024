enum class Operator {
    ADD,
    MULTIPLY,
    CONCAT;

    fun apply(a: Long, b: Long) = when (this) {
        ADD -> a + b
        MULTIPLY -> a * b
        CONCAT -> "$a$b".toLong()
    }
}

data class Equation(
    val result: Long,
    val numbers: List<Long>,
) {
    fun canBeCompleted(allowedOperators: List<Operator>): Boolean {
        fun combine(positions: Int): List<List<Operator>> {
            if (positions == 1) {
                return allowedOperators.map { listOf(it) }
            }

            val combinations = combine(positions - 1)
            return combinations.flatMap { combination ->
                allowedOperators.map { listOf(it) + combination }
            }
        }

        val combinations = combine(numbers.size - 1)
        return combinations.any { operators ->
            var a = numbers.first()
            operators.forEachIndexed { index, operator ->
                val b = numbers[index + 1]
                a = operator.apply(a, b)
            }

            a == result
        }
    }

    companion object {
        fun from(input: String): Equation {
            val (result, numbers) = input.split(": ", limit = 2)
            return Equation(
                result.toLong(),
                numbers.split(' ').map { it.toLong() }
            )
        }
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val equations = input.map(Equation::from)
        return equations
            .filter { it.canBeCompleted(listOf(Operator.ADD, Operator.MULTIPLY)) }
            .sumOf { it.result }
    }

    fun part2(input: List<String>): Long {
        val equations = input.map(Equation::from)
        return equations
            .filter { it.canBeCompleted(listOf(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT)) }
            .sumOf { it.result }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
