package com.n9mtq4.imagequizzer

import com.n9mtq4.kotlin.extlib.io.errPrintln
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import java.awt.Desktop
import java.net.URI
import java.util.HashMap

/**
 * Created by will on 3/19/16 at 9:00 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
private const val USER_NAME = "n9mtq4"
private const val REPO_NAME = "ImageQuizzer"
private const val API_UPDATE_URL = "https://api.github.com/repos/$USER_NAME/$REPO_NAME/releases"
private const val UPDATE_URL = "https://github.com/$USER_NAME/$REPO_NAME/releases/latest"

/**
 * Checks if an update is needed, if so open the download url
 * */
internal fun checkForUpdate() {
	
	try {
		// Download the json data
		val jsonRaw = Jsoup.connect(API_UPDATE_URL).ignoreContentType(true).execute().body()
		
		// parse it
		val parser = JSONParser()
		val json = parser.parse(jsonRaw) as JSONArray
		
		// get the latest version
		val latestVersion = json[0] as HashMap<*, *>
		
		// get the tag name 
		val tagName = latestVersion["tag_name"] as String
		val tokens = tagName.split("-")
		val targetBuildNumber = tokens[tokens.size - 1].toInt() // get the build number
		// val targetBuildNumber = Integer.MAX_VALUE // testing value
		
		if (BUILD_NUMBER < targetBuildNumber) {
			updateNeeded()
		}
		
	}catch (e: Exception) {
		errPrintln("Failed to get update status")
		e.printStackTrace()
	}
	
}

/**
 * Open the latest releases page
 * */
internal fun updateNeeded() {
	
	val link = URI.create(UPDATE_URL)
	
	try {
		
		Desktop.getDesktop().browse(link)
		
	}catch (e: Exception) {
		
		if (System.getProperty("os.name").contains("mac", ignoreCase = true)) {
			
			Runtime.getRuntime().exec(arrayOf("/usr/bin/open", link.toString()))
			
		}else {
			errPrintln("Failed to open update url")
			e.printStackTrace()
		}
		
	}
	
}
