package com.n9mtq4.imagequizzer.worker

import com.n9mtq4.imagequizzer.OutputData
import java.io.File

/**
 * Created by will on 12/9/2017 at 2:46 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

internal fun queryListToDatabaseAndImages(
		queryList: List<String>, 
		outputDirectory: File, 
		prefix: String = "",
		suffix: String = "",
		numImages: Int = -1, 
		shouldDownload: Boolean = true) {
	
	// apply prefix and suffix onto queries
	val searchQueries = queryList.map { "$prefix$it$suffix" }
	// get the links for them
	val links = getImageLinksFromList(searchQueries, numImages)
	
	// if needed, download the images
	val downloadedLinks = if (!shouldDownload)
		links
	else
		batchDownloadList(links, File(outputDirectory, "imgs")).map { it.toHtmlFormat(outputDirectory) }
	
	// match the names back up with the links
	val outputData: OutputData = queryList.zip(downloadedLinks)
	
	saveToJson(outputData, File(outputDirectory, "database.json"))
	
}
