import kotlin.io.path.Path
import kotlin.io.path.readLines


fun readLines(fileName: String): List<String> {
    return Path("src/main/resources/$fileName").readLines()
}
fun Any?.println() = println(this)
