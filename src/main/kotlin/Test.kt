
import com.n9mtq4.imagequizzer.worker.*
import kotlinx.coroutines.experimental.runBlocking
import java.io.File

/**
 * Created by will on 12/8/2017 at 8:42 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun main(args: Array<String>) = runBlocking<Unit> {
	
	
	
}

private fun test1() {
	
	val links = getImageLinksFromQuery("pyrite")
	links.mapIndexed { index, s -> "[$index] $s" }.forEach(::println)
	
	println("Starting downloads")
	val files = downloadImages(links, File("data/1/").apply { mkdirs() })
	println("Done with downloads")
	
	val parentFile = File("data")
	files.toHtmlFormat(parentFile).forEach(::println)
	
}

private fun test2() {
	
	val linksList = getImageLinksFromList(listOf("pyrite", "epidote", "lapis lazuli", "tourmaline", "sphalerite"))
	
	batchDownloadList(linksList, File("data/"))
	
}
