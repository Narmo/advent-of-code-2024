fun main() {
	data class Position(val row: Int, val col: Int, val height: Int)

	data class Path(val trailhead: Position, val path: MutableList<Position>) {
		fun last() = path.last()

		operator fun plus(position: Position): Path {
			return Path(trailhead, (path + position).toMutableList())
		}

		operator fun contains(position: Position): Boolean {
			return path.contains(position)
		}
	}

	fun buildMap(input: List<String>): Pair<List<List<Position>>, List<Position>> {
		val trailheads = mutableListOf<Position>()

		val map = input.mapIndexed { row, line ->
			line.mapIndexed { col, c ->
				if (c.isDigit()) {
					c.digitToInt().also { height ->
						if (height == 0) {
							trailheads.add(Position(row, col, height))
						}
					}.let {
						Position(row, col, it)
					}
				}
				else {
					Position(row, col, Int.MAX_VALUE)
				}
			}
		}

		return map to trailheads
	}

	fun getAdjacentPoints(points: List<List<Position>>, row: Int, col: Int): Set<Position> {        /*
		 -------------------
		 |     |     |     |
		 |     |-1,0 |     |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 | 0,-1| 0,0 | 0,1 |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 |     | 1,0 |     |
		 |     |     |     |
		 -------------------
		 */

		return listOf(Pair(1, 0), Pair(0, 1), Pair(0, -1), Pair(-1, 0)).mapNotNull {
			if ((row + it.first) in points.indices && (col + it.second) in points[row].indices) {
				points[row + it.first][col + it.second]
			}
			else {
				null
			}
		}.toSet()
	}

	fun findPaths(map: List<List<Position>>, trailheads: List<Position>): Set<Path> {
		val paths = mutableSetOf<Path>()

		for (trailhead in trailheads) {
			val queue = mutableListOf<Path>()
			queue.add(Path(trailhead, mutableListOf(trailhead)))

			while (queue.isNotEmpty()) {
				val path = queue.removeAt(0)
				val current = path.last()

				if (current.height == 9) {
					paths.add(path)
					continue
				}

				val adjacentPoints = getAdjacentPoints(map, current.row, current.col)

				for (adjacentPoint in adjacentPoints) {
					if (adjacentPoint.height == current.height + 1 && adjacentPoint !in path) {
						queue.add(path + adjacentPoint)
					}
				}
			}
		}

		return paths.toSet()
	}

	fun part1(input: List<String>): Int {
		val (map, trailheads) = buildMap(input)
		val paths = findPaths(map, trailheads)

		return paths.groupBy {
			it.trailhead
		}.flatMap {
			it.value.distinctBy { it.last() }
		}.size
	}

	fun part2(input: List<String>): Int {
		val (map, trailheads) = buildMap(input)
		val paths = findPaths(map, trailheads)
		return paths.size
	}

	val testInput1 = readInput("Day10_test1")
	val testInput2 = readInput("Day10_test2")
	val testInput3 = readInput("Day10_test3")
	val testInput4 = readInput("Day10_test4")
	val testInput5 = readInput("Day10_test5")
	val testInput6 = readInput("Day10_test6")

	check(part1(testInput1) == 1)
	check(part1(testInput2) == 36)

	check(part2(testInput3) == 3)
	check(part2(testInput4) == 13)
	check(part2(testInput5) == 227)
	check(part2(testInput6) == 81)

	val input = readInput("Day10")
	println(part1(input))
	println(part2(input))
}
