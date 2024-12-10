fun main() {
	data class Antenna(val row: Int, val col: Int, val signal: Char)

	fun parseInput(input: List<String>): Map<Char, List<Antenna>> {
		return input.flatMapIndexed { row, line ->
			line.mapIndexed { col, char ->
				Antenna(row, col, char)
			}
		}.groupBy({ it.signal }, { it })
	}

	fun printMap(map: List<String>, antinodes: Set<Pair<Int, Int>>? = null) {
		map.forEachIndexed { rowIndex, row ->
			row.forEachIndexed { columnIndex, column ->
				val antinode = rowIndex to columnIndex

				if (antinodes?.contains(antinode) == true && column == '.') {
					print('#')
				}
				else {
					print(column)
				}
			}

			println()
		}

		repeat(2) { println() }
	}

	fun part1(input: List<String>): Int {
		val antennasMapping = parseInput(input)
		val keys = antennasMapping.keys
		val antinodes = mutableSetOf<Pair<Int, Int>>()

		for (key in keys) {
			if (key == '.') {
				continue
			}

			antennasMapping[key]?.let {
				val combinations = it.combinations(2)

				for (combination in combinations) {
					val vector = combination[0].row - combination[1].row to combination[0].col - combination[1].col
					val testNodes = listOf(combination[1].row - vector.first to combination[1].col - vector.second, combination[0].row + vector.first to combination[0].col + vector.second)

					for (antinode in testNodes) {
						if (antinode.first >= 0 && antinode.second >= 0 && antinode.first < input.size && antinode.second < input[0].length) {
							antinodes.add(antinode)
						}
					}
				}
			}
		}

		printMap(input, antinodes)

		return antinodes.size
	}

	fun Pair<Int, Int>.withinBounds(input: List<String>): Boolean {
		return first >= 0 && second >= 0 && first < input.size && second < input[0].length
	}

	fun part2(input: List<String>): Int {
		val antennasMapping = parseInput(input)
		val keys = antennasMapping.keys
		val antinodes = mutableSetOf<Pair<Int, Int>>()
		val antennas = antennasMapping.values.flatten().filterNot { it.signal == '.' }.map { it.row to it.col }

		for (key in keys) {
			if (key == '.') {
				continue
			}

			antennasMapping[key]?.let {
				val combinations = it.combinations(2)

				for (combination in combinations) {
					val vector = combination[0].row - combination[1].row to combination[0].col - combination[1].col

					val testNodes = mutableListOf<Pair<Int, Int>>()

					var nextNode: Pair<Int, Int> = combination[1].row - vector.first to combination[1].col - vector.second

					while (nextNode.withinBounds(input)) {
						testNodes.add(nextNode)
						nextNode = nextNode.first - vector.first to nextNode.second - vector.second
					}

					nextNode = combination[0].row + vector.first to combination[0].col + vector.second

					while (nextNode.withinBounds(input)) {
						testNodes.add(nextNode)
						nextNode = nextNode.first + vector.first to nextNode.second + vector.second
					}

					for (antinode in testNodes) {
						if (antinode.first >= 0 && antinode.second >= 0 && antinode.first < input.size && antinode.second < input[0].length) {
							if (!antennas.contains(antinode)) {
								antinodes.add(antinode)
							}
						}
					}
				}
			}
		}

		printMap(input, antinodes)

		return antinodes.size + antennas.size
	}

	val testInput = readInput("Day08_test")
	check(part1(testInput) == 14)
	check(part2(testInput) == 34)

	val input = readInput("Day08")
	println(part1(input))
	println(part2(input))
}
