package com.n9mtq4.imagequizzer.worker

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.n9mtq4.imagequizzer.OutputData
import com.n9mtq4.kotlin.extlib.io.open
import java.io.File

/**
 * Created by will on 12/9/2017 at 2:28 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

internal fun saveToJson(outputData: OutputData, file: File) = open(file, "w").use { wFile ->
	
	// the whole thing is one json array
	val rootArray = JsonArray()
	
	// go through each set of links and name
	outputData.forEach { (name, links) ->
		
		val listObject = JsonObject() // the object for the links and name
		val jsonLinks = JsonArray(links.size) // the array for the links
		links.forEach { link -> jsonLinks.add(link) } // put the links into the array
		
		listObject.apply {
			addProperty("name", name) // add the name
			add("links", jsonLinks) // add the links
		}
		
		// add the object onto the list
		rootArray.add(listObject)
		
	}
	
	// create the gson
	val gson = GsonBuilder().create()
	
	// write to the file
	wFile.writeln(gson.toJson(rootArray))
	
}
