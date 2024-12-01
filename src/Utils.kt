import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Reads the whole input txt file as a single string.
 */
fun readInputPlain(name: String) = Path("src/$name.txt").readText()

@OptIn(ExperimentalContracts::class)
inline fun <T> benchmark(block: () -> T): T {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}

	val start = System.currentTimeMillis()
	val result = block()
	val end = System.currentTimeMillis()

	println("${end - start}ms")

	return result
}

fun <T> MutableList<T>.removeDuplicates() {
	val setItems: Set<T> = LinkedHashSet(this)
	clear()
	addAll(setItems)
}

fun <T> MutableList<T>.copyOf(): MutableList<T> {
	return this.map { it }.toMutableList()
}

fun <T> List<T>.pairs(): List<Pair<T, T>> {
	val pairs = mutableListOf<Pair<T, T>>()

	for (i in 0 until this.size - 1) {
		for (j in i + 1 until this.size) {
			pairs.add(this[i] to this[j])
		}
	}

	return pairs
}
