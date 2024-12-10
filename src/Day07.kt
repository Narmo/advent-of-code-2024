fun main() {
	fun parseInput(input: List<String>): List<Pair<Long, List<Long>>> {
		return input.map {
			val (left, right) = it.split(":")
			val sum = left.trim().toLong()
			val numbers = right.trim().split(" ").map { it.trim().toLong() }
			sum to numbers
		}
	}

	val plus = "+"
	val mult = "*"
	val concat = "||"

	fun part1(input: List<String>): Long {
		return parseInput(input).mapNotNull { (sum, numbers) ->
			val operatorCombinations = mutableListOf<MutableList<String>>()

			fun generateCombinations(index: Int, current: MutableList<String>) {
				if (index == numbers.size - 1) {
					operatorCombinations.add(current.toMutableList())
					return
				}

				for (op in listOf(plus, mult)) {
					current.add(op)
					generateCombinations(index + 1, current)
					current.removeLast()
				}
			}

			generateCombinations(0, mutableListOf())

			for (operators in operatorCombinations) {
				var result = numbers.first()

				for (i in 1..<numbers.size) {
					val operatorIndex = i - 1

					if (operators[operatorIndex] == plus) {
						result += numbers[i]
					}
					else {
						result *= numbers[i]
					}
				}

				if (result == sum) {
					return@mapNotNull result
				}
			}

			null
		}.sum()
	}

	fun part2(input: List<String>): Long {
		val result = parseInput(input).mapNotNull { (sum, numbers) ->
			val operatorCombinations = mutableListOf<MutableList<String>>()

			fun generateCombinations(index: Int, current: MutableList<String>) {
				if (index == numbers.size - 1) {
					operatorCombinations.add(current.toMutableList())
					return
				}

				for (op in listOf(plus, mult, concat)) {
					current.add(op)
					generateCombinations(index + 1, current)
					current.removeLast()
				}
			}

			generateCombinations(0, mutableListOf())

			for (operators in operatorCombinations) {
				var result = numbers.first()

				for (i in 1..<numbers.size) {
					val operatorIndex = i - 1

					if (operators[operatorIndex] == concat) {
						val lastNumber = result
						result = (lastNumber.toString() + numbers[i].toString()).toLong()
					}
					else if (operators[operatorIndex] == plus) {
						result += numbers[i]
					}
					else {
						result *= numbers[i]
					}
				}

				if (result == sum) {
					return@mapNotNull result
				}
			}

			null
		}

		return result.sum()
	}

	val testInput = readInput("Day07_test")
	check(part1(testInput) == 3749L)
	check(part2(testInput) == 11387L)

	val input = readInput("Day07")
	println(part1(input))
	println(part2(input))
}
