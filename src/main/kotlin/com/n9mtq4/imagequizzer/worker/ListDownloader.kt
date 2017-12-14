package com.n9mtq4.imagequizzer.worker

import com.n9mtq4.imagequizzer.LinkListList
import com.n9mtq4.imagequizzer.debugPrint
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import java.io.File

/**
 * Created by will on 12/9/2017 at 12:39 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

internal fun getImageLinksFromList(queryList: List<String>, size: Int = -1) = runBlocking<List<List<String>>> {
	
	// old, limit to speed/http connects, had to group
//	queryList.map { getImageLinksFromQuery(it, size) }
	
	val gSize = 10
	val groups = queryList.divideIntoGroupsOf(gSize)
	
	val list = groups.map { l -> l.map { getImageLinksFromQuery(it, size) } }.flatten()
	
	return@runBlocking list
	
}

internal fun batchDownloadList(linkListList: LinkListList, fileParent: File) = runBlocking<List<List<File>>> {
	
	val gSize = 5
	val groups = linkListList.divideIntoGroupsOf(gSize)
	
	val list = groups.mapIndexed { i, it -> batchDownloadListFull(it, fileParent, i * gSize) }.flatten()
	
	return@runBlocking list
	
}

private fun <R> List<R>.divideIntoGroupsOf(size: Int): List<List<R>> {
	
	var l = this
	val o = ArrayList<List<R>>()
	while (l.isNotEmpty()) {
		val s = if (l.size < size) l.size else size
		o.add(l.take(s))
		l = l.drop(s)
	}
	return o.toList()
	
}

// TODO: this does not have cancellation support
internal fun batchDownloadListFull(linkListList: LinkListList, fileParent: File, startIndex: Int = 0) = runBlocking<List<List<File>>> {
	
	val jobs = List(linkListList.size) { linkListIndex ->
		
		// download all the images asynchronously
		async { 
			
			// the link list to download
			val linkList = linkListList[linkListIndex]
			
			debugPrint("Started image download")
			// download them
			val downloadedList = downloadImages(linkList, File(fileParent, "${linkListIndex + startIndex}").apply { mkdirs() })
			
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
