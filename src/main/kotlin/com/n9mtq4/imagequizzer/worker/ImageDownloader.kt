package com.n9mtq4.imagequizzer.worker

import com.n9mtq4.imagequizzer.USER_AGENT
import com.n9mtq4.kotlin.extlib.pstAndNull
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream

/**
 * Created by will on 12/8/2017 at 11:33 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

typealias DlFileNameGenerator = (Int, String) -> String

/**
 * Downloads a list of images asynchronously
 * */
fun downloadImages(links: List<String>, outputParent: File, outputFile: DlFileNameGenerator = { i, e -> "$i.$e" }) = runBlocking<List<File>> {
	
	val jobs = List(links.size) { i ->
		
		async {
			
			// the link to download
			val link = links[i]
			
			// get the file extension from the url
			val fileExtension = link
					.split("?") // split based on before/after url params
					.first() // we want before, the url part and not the params part
					.split(".") // split based on dots
					.last() // we want the last one. should be file extension
					.trim() // trim just in case
					.toLowerCase() // .jpg is better than .JPG
			val fileName = outputFile(i, fileExtension) // use inputed function to get file name
			val file = File(outputParent, fileName) // turn into a file
			
			// if downloadImage fails and it throws an exception, give it a value of null,
			// otherwise return the file it was called
			val out = pstAndNull {
				downloadImage(link, file) // download the image
				file // return the file output path val out
			}
			
			return@async out // push out back to async
			
		}
		
	}
	
	// collect all out's from the jobs into a list and return it
	return@runBlocking jobs.mapNotNull { it.await() }
	
}

/**
 * Download the image with a coroutine
 * */
suspend private fun downloadImage(link: String, file: File) {
	
	// image request
	val response = Jsoup
			.connect(link)
			.userAgent(USER_AGENT) // user-agent - some sites don't give images to bots
			.ignoreContentType(true) // ignore content - allows for images instead of html
			.timeout(5000) // timeout - so we make sure we get the image
			.maxBodySize(Int.MAX_VALUE) // body size- so the images don't get cut off
			.execute() // go!
	
	// write to file
	FileOutputStream(file).use { fos ->
		fos.write(response.bodyAsBytes())
	}
	
}
