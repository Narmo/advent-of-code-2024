fun main() {
	val freeSpaceIndicator = -1L

	fun createDiskMap(input: String): MutableList<Long> {
		val diskMap = mutableListOf<Long>()
		var fileId = 0L

		input.forEachIndexed { index, char ->
			if (index % 2 == 0) {
				// file

				repeat(char.digitToInt()) {
					diskMap.add(fileId)
				}

				fileId += 1
			}
			else {
				// free space

				repeat(char.digitToInt()) {
					diskMap.add(freeSpaceIndicator)
				}
			}
		}

		while (diskMap.last() == freeSpaceIndicator) {
			diskMap.removeLast()
		}

		return diskMap
	}

	fun part1(input: String): Long {
		val diskMap = createDiskMap(input)
		var index = 0

		while (index < diskMap.size) {
			val currentBlock = diskMap[index]

			if (currentBlock == freeSpaceIndicator) {
				diskMap[index] = diskMap.removeLast()
			}

			while (diskMap.last() == freeSpaceIndicator) {
				diskMap.removeLast()
			}

			index += 1
		}

		return diskMap.mapIndexed { index, block ->
			index * block
		}.sum()
	}

	fun part2(input: String, debugPrint: Boolean = false): Long {
		val diskMap = createDiskMap(input)

		val movedFilesIds = mutableSetOf<Long>()

		if (debugPrint) {
			println("Initial map ${diskMap.map { if (it == freeSpaceIndicator) '.' else it }.joinToString("")}")
		}

		var lastIndex = diskMap.lastIndex

		out@ while (true) {
			if (diskMap[lastIndex] == freeSpaceIndicator) {
				lastIndex -= 1
				continue
			}

			val lastFile = mutableListOf<Long>()

			do {
				lastFile.add(0, diskMap[lastIndex])
				lastIndex -= 1

				if (lastIndex < 0) {
					break@out
				}
			} while (lastFile.all { it == diskMap[lastIndex] })

			if (movedFilesIds.contains(lastFile.first())) {
				continue
			}

			if (debugPrint) {
				println("Next file: $lastFile (length ${lastFile.size} at ${lastIndex}..${lastIndex + lastFile.size})")
			}

			var index = 0
			var currentBlock = diskMap[index]

			inner@ while (index < lastIndex) {
				index += 1

				if (index >= diskMap.size) {
					break@inner
				}

				currentBlock = diskMap[index]

				if (debugPrint) {
					println("Checking current block $currentBlock at $index…")
				}

				var freeSpaceLength = 0

				while (currentBlock == freeSpaceIndicator && freeSpaceLength < lastFile.size) {
					freeSpaceLength += 1
					index += 1

					if (index >= diskMap.size) {
						break@inner
					}

					currentBlock = diskMap[index]
				}

				if (freeSpaceLength >= lastFile.size) {
					if (debugPrint) {
						println("Found free space at $index, size $freeSpaceLength")
						println("Found last file: $lastFile (at ${lastIndex}..${lastIndex + lastFile.size}), moving to ${index - freeSpaceLength}..${index - freeSpaceLength + lastFile.size}…")
					}

					for (i in 0 until lastFile.size) {
						diskMap[index - freeSpaceLength + i] = lastFile[i]
					}

					movedFilesIds.add(lastFile.first())

					index += lastFile.size

					if (debugPrint) {
						println("Moved index to $index")
					}

					if (debugPrint) {
						println("Filling free space from $lastIndex to ${lastIndex + lastFile.size}…")
					}

					for (i in lastIndex + 1..<lastIndex + lastFile.size + 1) {
						diskMap[i] = freeSpaceIndicator
					}

					if (debugPrint) {
						println("Map now is ${diskMap.map { if (it == freeSpaceIndicator) '.' else it }.joinToString("")}")
						println()
					}

					break@inner
				}
			}
		}

		if (debugPrint) {
			println("Map now is ${diskMap.map { if (it == freeSpaceIndicator) '.' else it }.joinToString("")}")
		}

		var sum = 0L

		diskMap.forEachIndexed { index, block ->
			if (block != freeSpaceIndicator) {
				sum += index * block
			}
		}

		return sum
	}

	val testInput = readInput("Day09_test").first()
	check(part1(testInput) == 1928L)
	check(part2(testInput, debugPrint = false) == 2858L)

	val input = readInput("Day09").first()
	println(part1(input))
	println(part2(input, debugPrint = false))
}
