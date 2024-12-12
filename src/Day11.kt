fun main() {
	fun getStones(input: String): List<String> {
		return input.split(" ").map { it.toString() }
	}

	fun blinkMap(stones: List<String>, blinks: Int): Long {
		return benchmark {
			println()

			val map = mutableMapOf<String, Long>()

			for (stone in stones) {
				map[stone] = (map[stone] ?: 0L) + 1L
			}

			repeat(blinks) {
				val newMap = mutableMapOf<String, Long>()

				for (stone in map.keys) {
					val entryCount = map[stone]!!

					if (stone == "0") {
						newMap["1"] = (newMap["1"] ?: 0L) + entryCount
					}
					else if (stone.length % 2 == 0) {
						listOf(stone.substring(0, stone.length / 2), stone.substring(stone.length / 2, stone.length)).forEach {
							val newStone = it.dropWhile { it == '0' }

							if (newStone.isEmpty()) {
								newMap["0"] = (newMap["0"] ?: 0L) + entryCount
							}
							else {
								newMap[newStone] = (newMap[newStone] ?: 0L) + entryCount
							}
						}
					}
					else {
						val newStone = (stone.toLong() * 2024L).toString()

						newMap[newStone] = (newMap[newStone] ?: 0L) + entryCount
					}
				}

				map.clear()
				map.putAll(newMap)
			}

			map.values.sum()
		}
	}

	fun task(input: String, blinks: Int): Long {
		return blinkMap(getStones(input), blinks)
	}

	val testInput1 = readInput("Day11_test1").first()
	val testInput2 = readInput("Day11_test2").first()

	check(task(testInput1, 1) == 7L)
	check(task(testInput2, 6) == 22L)
	check(task(testInput2, 25) == 55312L)

	val input = readInput("Day11").first()
	println(task(input, 25))
	println(task(input, 75))
}
