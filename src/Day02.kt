import kotlin.math.abs
import kotlin.math.sign

fun main() {
	val regex = "\\s+".toRegex()

	fun checkSafety(levels: List<Int>, toleranceLevel: Int): Boolean {
		var prevDiff: Int? = null

		for (i in 1 until levels.size) {
			val diff = levels[i] - levels[i - 1]

			if (diff == 0 || abs(diff) > 3 || (prevDiff != null && diff.sign != prevDiff.sign)) {
				if (toleranceLevel > 0) {
					for (i in 0 until levels.size) {
						if (checkSafety(levels.toMutableList().also { it.removeAt(i) }, toleranceLevel - 1)) {
							return true
						}
					}
				}

				return false
			}

			prevDiff = diff
		}

		return true
	}

	fun getUnsafeLevelsReports(input: List<String>, toleranceLevel: Int): List<Boolean> {
		return input.map {
			val levels = it.split(regex).map { it.toInt() }
			checkSafety(levels, toleranceLevel)
		}
	}

	fun part1(input: List<String>): Int {
		return getUnsafeLevelsReports(input, toleranceLevel = 0).count { it }
	}

	fun part2(input: List<String>): Int {
		return getUnsafeLevelsReports(input, toleranceLevel = 1).count { it }
	}

	val testInput = readInput("Day02_test")
	check(part1(testInput) == 2)
	check(part2(testInput) == 4)

	val input = readInput("Day02")
	println(part1(input))
	println(part2(input))
}
