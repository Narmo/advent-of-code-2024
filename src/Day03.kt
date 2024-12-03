fun main() {
	val regex = """mul\((\d+),(\d+)\)""".toRegex()
	val doRegex = """do\(\)""".toRegex()
	val dontRegex = """don't\(\)""".toRegex()

	fun part1(input: List<String>): Int {
		return input.flatMap {
			regex.findAll(it).map {
				it.groupValues.drop(1).map { it.toInt() }.toList()
			}
		}.sumOf { (x, y) ->
			x * y
		}
	}

	fun part2(input: List<String>): Int {
		val inst = input.map {
			val mult = regex.findAll(it).let {
				it.map {
					it.groupValues.drop(1).map { it.toInt() }.toList() to it.range.start
				}
			}.toList()

			val dos = doRegex.findAll(it).map { true to it.range.start }.toList()
			val donts = dontRegex.findAll(it).map { false to it.range.start }.toList()

			(mult + dos + donts).sortedBy { it.second }.map { it.first }
		}

		var sum = 0
		var enabled = true

		for (line in inst) {
			var acc = 0

			for (entry in line) {
				if (entry is Boolean) {
					enabled = entry
				}
				else {
					if (enabled) {
						val (x, y) = entry as List<Int>
						acc += x * y
					}
				}
			}

			sum += acc
		}

		return sum
	}

	val testInput1 = readInput("Day03_test1")
	check(part1(testInput1) == 161)

	val testInput2 = readInput("Day03_test2")
	check(part2(testInput2) == 48)

	val input = readInput("Day03")
	println(part1(input))
	println(part2(input))
}
