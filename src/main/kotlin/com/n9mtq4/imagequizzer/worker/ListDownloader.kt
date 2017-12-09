package com.n9mtq4.imagequizzer.worker

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import java.io.File

/**
 * Created by will on 12/9/2017 at 12:39 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

internal fun getImageLinksFromList(queryList: List<String>, size: Int = -1) = queryList.map { getImageLinksFromQuery(it, size) }

// TODO: this does not have cancellation support
internal fun batchDownloadList(linkListList: List<List<String>>, fileParent: File) = runBlocking<List<List<File>>> {
	
	val jobs = List(linkListList.size) { linkListIndex ->
		
		// download all the images asynchronously
		async { 
			
			// the link list to download
			val linkList = linkListList[linkListIndex]
			
			// download them
			val downloadedList = downloadImages(linkList, File(fileParent, "$linkListIndex").apply { mkdirs() })
			
			// return the outputed files
			return@async downloadedList
			
		}
		
	}
	
	// wait for them all to complete and package them up into the correct return structure
	return@runBlocking jobs.map { it.await() }
	
}


internal fun List<File>.toHtmlFormat(parentDir: File): List<String> = this.map { it.toHtmlFormat(parentDir) }

internal fun File.toHtmlFormat(parentDir: File): String = this.absolutePath
		.substring(parentDir.absolutePath.length + 1) // cut off part before parent dir
		.replace("\\", "/") // since windows use \ instead of /. // TODO: might cause subtle bugs if file name contains '\'
