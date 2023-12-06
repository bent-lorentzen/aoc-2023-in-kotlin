import kotlin.io.path.Path
import kotlin.io.path.readLines


fun readLines(fileName: String): List<String> {
    return Path("src/main/resources/$fileName").readLines()
}
fun Any?.println() = println(this)

fun Iterable<Int>.containsAny(set: Iterable<Int>): Boolean {
    return set.any { this.contains(it) }
}
