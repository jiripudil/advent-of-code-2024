fun main() {
    data class Vector(val x: Int, val y: Int) {
        operator fun plus(other: Vector) = Vector(x + other.x, y + other.y)
        operator fun minus(other: Vector) = Vector(x - other.x, y - other.y)
        operator fun times(factor: Int) = Vector(x * factor, y * factor)
        fun flipX() = Vector(-x, y)
        fun flipY() = Vector(x, -y)
    }

    fun part1(input: List<String>): Int {
        val map = buildMap {
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    put(Vector(x, y), char)
                }
            }
        }

        val directions = listOf(Vector(0, -1), Vector(1, 1), Vector(1, 0), Vector(1, -1), Vector(0, 1), Vector(-1, -1), Vector(-1, 0), Vector(-1, 1))
        fun detectXmas(map: Map<Vector, Char>, start: Vector): Int {
            val startingChar = map.get(start)
            if (startingChar != 'X') return 0

            return directions.count { dir ->
                map.get(start + dir) == 'M' && map.get(start + dir * 2) == 'A' && map.get(start + dir * 3) == 'S'
            }
        }

        return map.keys.sumOf { detectXmas(map, it) }
    }

    fun part2(input: List<String>): Int {
        val map = buildMap {
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    put(Vector(x, y), char)
                }
            }
        }

        val directions = listOf(Vector(-1, -1), Vector(1, 1), Vector(-1, 1), Vector(1, -1))
        fun detectXmas(map: Map<Vector, Char>, start: Vector): Boolean {
            val middleChar = map.get(start)
            if (middleChar != 'A') return false

            return directions.any { dir ->
                map.get(start + dir) == 'M' && map.get(start - dir) == 'S'
                        && ((map.get(start + dir.flipX()) == 'M' && map.get(start + dir.flipY()) == 'S')
                        || (map.get(start + dir.flipY()) == 'M' && map.get(start + dir.flipX()) == 'S'))
            }
        }

        return map.keys.count { detectXmas(map, it) }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
