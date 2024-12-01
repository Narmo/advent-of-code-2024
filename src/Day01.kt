import kotlin.math.abs

fun main() {
	fun part1(input: List<String>): Int {
		val lists = input.map { it.split(regex = Regex("\\s+")) }
		val left = mutableListOf<Int>()
		val right = mutableListOf<Int>()

		for (pair in lists) {
			val (l, r) = pair
			left.add(l.toInt())
			right.add(r.toInt())
		}

		left.sort()
		right.sort()

		return left.zip(right).sumOf { (l, r) -> abs(l - r) }
	}

	fun part2(input: List<String>): Int {
		val lists = input.map { it.split(regex = Regex("\\s+")) }
		val left = mutableListOf<Int>()
		val right = mutableListOf<Int>()

		for (pair in lists) {
			val (l, r) = pair
			left.add(l.toInt())
			right.add(r.toInt())
		}

		return left.sumOf { right.count { r -> it == r } * it }
	}

	val testInput = readInput("Day01_test")
	check(part1(testInput) == 11)
	check(part2(testInput) == 31)

	val input = readInput("Day01")
	println(part1(input))
	println(part2(input))
}
