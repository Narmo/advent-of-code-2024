fun main() {
	data class Instructions(val ordering: List<Pair<Int, Int>>, val updates: List<List<Int>>)

	fun <T> List<T>.middle(): T {
		return this[this.size / 2]
	}

	fun processInput(input: List<String>): Instructions {
		val iterator = input.iterator()
		val ordering = mutableListOf<Pair<Int, Int>>()

		while (true) {
			val line = iterator.next()

			if (line.isEmpty()) {
				break
			}

			val (a, b) = line.split("|").let { it[0].toInt() to it[1].toInt() }

			ordering.add(a to b)
		}

		val updates = iterator.asSequence().map { it.split(",").map { it.toInt() } }.toList()

		return Instructions(ordering, updates)
	}

	fun sortPages(input: List<Int>, instructions: Instructions): List<Int> {
		return input.sortedWith { o1, o2 ->
			val inst = instructions.ordering.find { pair ->
				pair.first == o1 && pair.second == o2
			} ?: return@sortedWith 0

			if (inst.first == o1) {
				-1
			}
			else {
				1
			}
		}
	}

	fun part1(input: List<String>): Int {
		val instructions = processInput(input)

		return instructions.updates.filter {
			sortPages(it, instructions) == it
		}.sumOf {
			it.middle()
		}
	}

	fun part2(input: List<String>): Int {
		val instructions = processInput(input)

		val incorrectUpdates = instructions.updates.filter {
			sortPages(it, instructions) != it
		}

		return incorrectUpdates.map { t ->
			sortPages(t, instructions)
		}.sumOf {
			it.middle()
		}
	}

	val testInput = readInput("Day05_test")
	check(part1(testInput) == 143)
	check(part2(testInput) == 123)

	val input = readInput("Day05")
	println(part1(input))
	println(part2(input))
}
