package com.n9mtq4.imagequizzer.worker

import com.n9mtq4.imagequizzer.OutputData
import com.n9mtq4.imagequizzer.listparsers.ParserOutput
import java.io.File

/**
 * Created by will on 12/9/2017 at 2:46 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Turns a list of queries into the final product.
 * 
 * 1. Gets images links from the query list.
 * 2.Downloads all the links if desired.
 * 3. Saves the images and/or database to the output directory.
 * 4. Saves the rest of the html/css/js files needed to view
 * the images and/or database to the output directory.
 * 
 * @param queryList the parser output. Contains names and queries
 * @param outputDirectory a directory to save everything to
 * @param prefix the prefix to add to every query, default: ''
 * @param suffix the suffix to add to every query, default: ''
 * @param numImages the number of images to get, default: -1
 * @param shouldDownload if it should download the images, default: false
 * */
internal fun queryListToDatabaseAndImages(
		queryList: ParserOutput, 
		outputDirectory: File, 
		prefix: String = "",
		suffix: String = "",
		numImages: Int = -1, 
		shouldDownload: Boolean = false) {
	
	// apply prefix and suffix onto queries
	val searchQueries = queryList.map { it.second }.map { "$prefix$it$suffix" }
	// get the links for them
	val links = getImageLinksFromList(searchQueries, numImages)
	
	// if needed, download the images
	val downloadedLinks = if (!shouldDownload)
		links
	else
		batchDownloadList(links, File(outputDirectory, "imgs").apply { mkdirs() }).map { it.toHtmlFormat(outputDirectory) }
	
	// match the names back up with the links
	val outputData: OutputData = queryList.map { it.first }.zip(downloadedLinks)
	
	// make sure the output directory, if needed (mkdirs checks if it exists)
	outputDirectory.mkdirs()
	
	// save the output data
	saveToJson(outputData, File(File(outputDirectory, "/db/").apply { mkdirs() }, "database.json"))
	saveToJs(outputData, File(File(outputDirectory, "/db/").apply { mkdirs() }, "database.js"))
	
	// export the rest of the viewer files
	exportResources(outputDirectory)
	
}
