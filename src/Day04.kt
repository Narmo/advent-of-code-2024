typealias Point = Pair<Int, Int>

operator fun Point.plus(other: Point): Point {
	return this.first + other.first to this.second + other.second
}

fun main() {
	fun nextPointOffset(currentPoint: Point, previousPoint: Point?): Point {
		val (row, col) = currentPoint
		val (prevRow, prevCol) = previousPoint ?: (row to col)

		val rowDiff = row - prevRow
		val colDiff = col - prevCol

		return rowDiff to colDiff
	}

	fun part1(input: List<String>): Int {
		val targetWord = "XMAS"

		val nearPoints = listOf(
				Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
				Pair(0, -1), Pair(0, 1),
				Pair(1, -1), Pair(1, 0), Pair(1, 1),
		)

		fun findNextChar(currentPoint: Point, previousPoint: Point?, currentWord: List<Pair<Char, Point>>, map: List<String>, words: MutableSet<List<Pair<Char, Point>>>) {
			if (currentWord.unzip().first.joinToString("") == targetWord) {
				words.add(currentWord)
				return
			}

			val nextChar = targetWord[currentWord.unzip().first.size]
			val mapWidth = map.first().length
			val mapHeight = map.size

			val nextPoints: List<Point>

			if (previousPoint == null) {
				nextPoints = nearPoints
			}
			else {
				val nextPointOffset = nextPointOffset(currentPoint, previousPoint)
				nextPoints = listOf(nextPointOffset)
			}

			for (pointOffset in nextPoints) {
				val nextPoint = currentPoint + pointOffset

				if (nextPoint.first in 0..<mapHeight && nextPoint.second in 0..<mapWidth) {
					val nextCharInMap = map[nextPoint.first][nextPoint.second]

					if (nextCharInMap == nextChar) {
						val previousPoint = currentPoint
						val word: List<Pair<Char, Point>> = currentWord + (nextChar to nextPoint)

						findNextChar(nextPoint, previousPoint, word, map, words)
					}
				}
			}
		}

		val result = mutableSetOf<List<Pair<Char, Point>>>()

		for (row in input.indices) {
			for (col in input[row].indices) {
				if (input[row][col] == 'X') {
					val point = row to col
					findNextChar(point, null, listOf('X' to point), input, result)
				}
			}
		}

		return result.size
	}

	fun part2(input: List<String>): Int {
		val targetWord = "MAS"
		val reversedTargetWord = targetWord.reversed()

		val nearPointsLeft = listOf(Pair(-1, -1), Pair(0, 0), Pair(1, 1))
		val nearPointsRight = listOf(Pair(-1, 1), Pair(0, 0), Pair(1, -1))

		fun checkMas(point: Point): Boolean {
			return listOf(nearPointsLeft, nearPointsRight).fold(true) { acc, nearPoints ->
				acc && nearPoints.map {
					point + it
				}.filter {
					it.first in input.indices && it.second in input[it.first].indices
				}.map {
					input[it.first][it.second]
				}.joinToString("").let {
					it == targetWord || it == reversedTargetWord
				}
			}
		}

		var count = 0

		for (row in input.indices) {
			for (col in input[row].indices) {
				if (input[row][col] == 'A') {
					val point = row to col

					if (checkMas(point)) {
						count += 1
					}
				}
			}
		}

		return count
	}

	val testInput = readInput("Day04_test")
	check(part1(testInput) == 18)
	check(part2(testInput) == 9)

	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
}
