fun main() {
    data class Vector(val x: Int, val y: Int) {
        operator fun plus(other: Vector) = Vector(x + other.x, y + other.y)
        fun rotate() = Vector(-y, x)
    }

    data class Step(val position: Vector, val direction: Vector)
    data class Path(val steps: List<Step>, val endsWithLoop: Boolean)

    data class Grid(val width: Int, val height: Int, val obstacles: Set<Vector>) {
        fun contains(position: Vector) = position.x in 0 until width && position.y in 0 until height

        fun draw(path: Path, extraObstacle: Vector? = null): String {
            var output = ""
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val position = Vector(x, y)
                    output += when {
                        position in obstacles -> '#'
                        position == extraObstacle -> 'O'
                        position in path.steps.map { it.position } -> 'X'
                        else -> '.'
                    }
                }

                output += "\n"
            }

            return output
        }
    }

    fun parseInput(input: List<String>): Triple<Grid, Vector, Vector> {
        val rows = input.size
        val columns = input[0].length

        var startingPosition: Vector? = null
        var direction: Vector? = null
        val obstacles = buildSet {
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    if (char == '#') {
                        add(Vector(x, y))
                    }

                    if (char == '^') {
                        startingPosition = Vector(x, y)
                        direction = Vector(0, -1)
                    }
                }
            }
        }

        val grid = Grid(columns, rows, obstacles)
        return Triple(grid, startingPosition!!, direction!!)
    }

    fun predictPath(grid: Grid, startingPosition: Vector, startingDirection: Vector): Path {
        val path = mutableListOf<Step>()

        var position = startingPosition
        var direction = startingDirection
        while (grid.contains(position)) {
            path.add(Step(position, direction))

            val nextPosition = position + direction
            val nextStep = Step(nextPosition, direction)

            if (nextStep in path) {
                return Path(path, true)
            }

            if (nextPosition in grid.obstacles) {
                direction = direction.rotate()
            } else {
                position = nextPosition
            }
        }

        return Path(path, false)
    }

    fun part1(input: List<String>): Int {
        val (grid, startingPosition, startingDirection) = parseInput(input)
        val path = predictPath(grid, startingPosition, startingDirection)
        return path.steps.map { it.position }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val (grid, startingPosition, startingDirection) = parseInput(input)
        val path = predictPath(grid, startingPosition, startingDirection)

        val loops = path.steps.map {
            val positionForObstacle = it.position + it.direction
            if (positionForObstacle in grid.obstacles
                || positionForObstacle == startingPosition
                || !grid.contains(positionForObstacle)
            ) {
                return@map null
            }

            val gridWithExtraObstacle = grid.copy(obstacles = grid.obstacles + positionForObstacle)
            val alternativePath = predictPath(gridWithExtraObstacle, startingPosition, startingDirection)

            if (!alternativePath.endsWithLoop) {
                return@map null
            }

            alternativePath
        }.filterNotNull()

        return loops.toSet().size
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
