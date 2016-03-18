package com.n9mtq4.imagequizer

import com.n9mtq4.kotlin.extlib.pstAndNull
import com.n9mtq4.kotlin.extlib.pstAndUnit
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream

/**
 * Created by will on 3/17/16 at 9:58 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Downloads images in list and renames them
 * */
internal fun downloadImages(images: List<String>, dir: File, prefix: String): List<String> {
	
	dir.mkdirs() // make sure we can make the file where we want
	
//	pair up the urls and the names of the files. ex: prefix = 1 - "11.png"
	val newAndOldPairs = images
			.map 			{ pstAndNull { it.substring(it.lastIndexOf("."), it.length) } } // get the extension
			.filterNot 		{ it == null } // make sure we know the extension
			.mapIndexed 	{ i, s -> prefix + i + s } // format the file with prefix + number + extension
			.map 			{ File(dir, it) } // generate java.io.File for it
			.zip			(images) // pair it with its original url
	
//	download each one
	newAndOldPairs.forEach { pstAndUnit { downloadFile(it.second, it.first) } }
	
//	return a list of the new file paths
	return newAndOldPairs.map { it.first.path }
	
}

/**
 * Downloads a url to a file
 * */
private fun downloadFile(url: String, file: File) = pstAndUnit {
	
//	image response
	val response = Jsoup.connect(url)
			.userAgent(USER_AGENT) // user-agent - some sites don't give bots images 
			.ignoreContentType(true) // ignore content - allows for images instead of html
			.timeout(1000) // timeout - so we make sure we get the image
			.maxBodySize(Integer.MAX_VALUE) // body size - so the images don't get cut off
			.execute() // go!
	
//	write
	val fos = FileOutputStream(file)
	fos.write(response.bodyAsBytes())
	
//	close
	fos.close()
	
}
