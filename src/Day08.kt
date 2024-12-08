fun <T> List<T>.cartesianProduct(): List<Pair<T, T>> {
    return buildList {
        this@cartesianProduct.forEachIndexed { index, a ->
            this@cartesianProduct.subList(index, this@cartesianProduct.size).forEach { b ->
                if (a != b) {
                    add(a to b)
                }
            }
        }
    }
}

data class Vector(val x: Int, val y: Int) {
    operator fun plus(other: Vector) = Vector(x + other.x, y + other.y)
    operator fun minus(other: Vector) = Vector(x - other.x, y - other.y)
}

data class Grid(
    private val width: Int,
    private val height: Int,
    private val antennae: Map<Vector, Char>,
) {
    fun isInBounds(position: Vector) = position.x in 0 until width && position.y in 0 until height

    fun getAntinodes(
        collect: (a: Vector, b: Vector) -> Sequence<Vector>
    ): Set<Vector> {
        val byFrequency = antennae.entries.groupBy { it.value }.mapValues { it.value.map { it.key } }
        return buildSet {
            byFrequency.forEach { _, positions ->
                val pairs = positions.cartesianProduct()
                pairs.forEach { (a, b) -> addAll(collect(a, b)) }
            }
        }
    }

    companion object {
        fun from(input: List<String>): Grid {
            val width = input[0].length
            val height = input.size

            val antennae = buildMap {
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val position = Vector(x, y)
                        val frequency = input[y][x]
                        if (frequency != '.') {
                            put(position, frequency)
                        }
                    }
                }
            }

            return Grid(width, height, antennae)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = Grid.from(input)
        val antinodes = grid.getAntinodes { a, b ->
            sequence {
                val distance = b - a

                val antinodeA = b + distance
                if (grid.isInBounds(antinodeA)) {
                    yield(antinodeA)
                }

                val antinodeB = a - distance
                if (grid.isInBounds(antinodeB)) {
                    yield(antinodeB)
                }
            }
        }

        return antinodes.size
    }

    fun part2(input: List<String>): Int {
        val grid = Grid.from(input)
        val antinodes = grid.getAntinodes { a, b ->
            sequence {
                val distance = b - a

                var antinode = a
                while (grid.isInBounds(antinode)) {
                    yield(antinode)
                    antinode -= distance
                }

                antinode = b
                while (grid.isInBounds(antinode)) {
                    yield(antinode)
                    antinode += distance
                }
            }
        }

        return antinodes.size
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
