
import com.n9mtq4.imagequizzer.worker.downloadImages
import com.n9mtq4.imagequizzer.worker.encodeSearchUrl
import com.n9mtq4.imagequizzer.worker.getImageLinks
import kotlinx.coroutines.experimental.runBlocking
import java.io.File

/**
 * Created by will on 12/8/2017 at 8:42 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun main(args: Array<String>) = runBlocking<Unit> {
	
	val links = getImageLinks(encodeSearchUrl("epidote"), -1)
	links.mapIndexed { index, s -> "[$index] $s" }.forEach(::println)
	
	println("Starting downloads")
	val files = downloadImages(links, File("data/").apply { mkdirs() })
	println("Done with downloads")
	
	files.mapIndexed { index, file -> "[$index] ${file.name}" }.forEach(::println)
	
}
