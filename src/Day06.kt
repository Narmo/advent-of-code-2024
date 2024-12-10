fun main() {
	val directions = listOf('^', '>', 'V', '<')
	val directionsDeltas = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
	val guardPositions = directions.zip(directionsDeltas).toMap()
	val obstacle = '#'

	data class GuardPosition(var row: Int, var column: Int, var direction: Char)

	fun createMap(input: List<String>): Pair<List<List<Char>>, GuardPosition> {
		val map = mutableListOf<MutableList<Char>>()
		var guardPosition = GuardPosition(Int.MIN_VALUE, Int.MIN_VALUE, ' ')

		input.forEachIndexed { rowIndex, line ->
			val row = mutableListOf<Char>()

			line.forEachIndexed { columnIndex, char ->
				if (char in guardPositions.keys) {
					guardPosition.row = rowIndex
					guardPosition.column = columnIndex
					guardPosition.direction = char

					row.add('.')
				}
				else {
					row.add(char)
				}
			}

			map.add(row)
		}

		return map to guardPosition
	}

	fun printMap(map: List<List<Char>>, guardPosition: GuardPosition, suggestedObstacles: Set<Pair<Int, Int>>? = null) {
		map.forEachIndexed { rowIndex, row ->
			row.forEachIndexed { columnIndex, column ->
				if (guardPosition.column == columnIndex && guardPosition.row == rowIndex) {
					print(guardPosition.direction)
				}
				else {
					if (suggestedObstacles?.contains(rowIndex to columnIndex) == true) {
						print('O')
					}
					else {
						print(column)
					}
				}
			}

			println()
		}

		repeat(2) { println() }
	}

	fun traverseMap(map: List<List<Char>>, guardPosition: GuardPosition, visited: MutableSet<GuardPosition>, suggestedObstacles: MutableSet<Pair<Int, Int>>? = null): Int? {
		do {
			visited.add(guardPosition.copy())

			val (rowDelta, columnDelta) = guardPositions[guardPosition.direction]!!
			val nextRow = guardPosition.row + rowDelta
			val nextColumn = guardPosition.column + columnDelta

			var performStep = true

			if (nextRow in 0..<map.size && nextColumn in 0..<map[0].size) {
				if (map[nextRow][nextColumn] == obstacle) {
					guardPosition.direction = directions[(directions.indexOf(guardPosition.direction) + 1) % directions.size]
					performStep = false
				}

				if (suggestedObstacles != null && !suggestedObstacles.contains(nextRow to nextColumn)) {
					val suggestedDirection = directions[(directions.indexOf(guardPosition.direction) + 1) % directions.size]
					val (suggestedRowDelta, suggestedColumnDelta) = guardPositions[suggestedDirection]!!
					val suggestedNextRow = guardPosition.row + suggestedRowDelta
					val suggestedNextColumn = guardPosition.column + suggestedColumnDelta
					val suggestedNextGuardPosition = GuardPosition(row = suggestedNextRow, column = suggestedNextColumn, direction = suggestedDirection)

					if (visited.contains(suggestedNextGuardPosition)) {
						// val newMap = map.map { it.toMutableList() }.toMutableList()
						// newMap[nextRow][nextColumn] = '#'

						suggestedObstacles.add(nextRow to nextColumn)

						// traverseMap(newMap, guardPosition, visited, suggestedObstacles)
					}
				}
			}

			if (performStep) {
				guardPosition.row = nextRow
				guardPosition.column = nextColumn
			}

			// printMap(map, guardPosition, suggestedObstacles)
		} while (guardPosition.row in 0..<map.size && guardPosition.column in 0..<map[0].size)

		return suggestedObstacles?.size.also {
			println("Suggested obstacles: $it")
		}
	}

	fun part1(input: List<String>): Int {
		val (map, guardPosition) = createMap(input)
		val visited = mutableSetOf<GuardPosition>()

		traverseMap(map, guardPosition, visited)

		return visited.map { it.row to it.column }.toSet().size
	}

	fun printVisited(map: List<List<Char>>, visited: Set<GuardPosition>) {
		val visitedPositions = visited.map { it.row to it.column }.toSet()

		map.forEachIndexed { rowIndex, row ->
			row.forEachIndexed { columnIndex, column ->
				if (visitedPositions.contains(rowIndex to columnIndex)) {
					print(visited.first { it.row == rowIndex && it.column == columnIndex }.direction)
				}
				else {
					print(column)
				}
			}

			println()
		}

		repeat(2) { println() }
	}

	fun part2(input: List<String>): Int {
		val (originalMap, originalGuardPosition) = createMap(input)
		val map = originalMap.map { it.toMutableList() }.toMutableList()
		val guardPosition = originalGuardPosition.copy()
		val visited = mutableSetOf<GuardPosition>()

		traverseMap(map, guardPosition, visited)

		// printVisited(originalMap, visited)

		val suggestedObstacles = mutableSetOf<Pair<Int, Int>>()

		return traverseMap(originalMap, originalGuardPosition, visited, suggestedObstacles) ?: 0
	}

	val testInput = readInput("Day06_test")
	check(part1(testInput) == 41)
	// check(part2(testInput) == 6)

	val input = readInput("Day06")
	println(part1(input))
	// println(part2(input))
}
