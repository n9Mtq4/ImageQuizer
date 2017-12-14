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

/**
 * Saves the ouputdata as a json file.
 * 
 * @param outputData the output data (names and links)
 * @param file the output file. should be something.json
 * */
internal fun saveToJson(outputData: OutputData, file: File) = open(file, "w").use { wFile ->
	
	wFile.writeln(generateJson(outputData))
	
}

/**
 * Saves the output data as a javascript file with a
 * varaible that contains the json string.
 * 
 * Chrome doesn't serve ajax from the local file system,
 * which includes json files. For this reason it is
 * impossible to load a json file without setting up
 * a local web server, This gets around that restriction
 * by storing the json insude a javascript file that can be
 * loaded through html's script tag.
 * 
 * Js: JSON.parse(dbjson)
 * 
 * @param outputData the output data (names and links)
 * @param file the output file. should be something.js
 * */
internal fun saveToJs(outputData: OutputData, file: File) = open(file, "w").use { wFile ->
	
	val json = generateJson(outputData)
	wFile.writeln("var dbjson = '$json'")
	
}

/**
 * Generates a json string from the output data
 * 
 * @param outputData the output data names and links
 * @return String of the json
 * */
private fun generateJson(outputData: OutputData): String {
	
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
	
	return gson.toJson(rootArray)
	
}
