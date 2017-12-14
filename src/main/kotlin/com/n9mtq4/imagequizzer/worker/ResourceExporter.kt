package com.n9mtq4.imagequizzer.worker

import com.n9mtq4.kotlin.extlib.io.open
import com.n9mtq4.kotlin.extlib.pst
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Created by will on 12/13/2017 at 6:05 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Reads the text from a file inside this jar
 * 
 * @param path the path inside the jar. Make sure it starts with a '/'
 * @return a string of the resources text contents
 * */
internal fun readFromJar(path: String): String {
	
	// TODO: not sure if object {} is the best way to do this
	val input = object {}::class.java.getResourceAsStream(path)
	val ir = InputStreamReader(input)
	val br = BufferedReader(ir)
	
	val text = br.readLines().joinToString(separator = "\n")
	
	br.close()
	ir.close()
	input.close()
	
	return text
	
}

/**
 * Exports files from inside the jar (html to view)
 * to a directory outside the jar.
 * 
 * @param outputDirectory the output directory to save the files to
 * */
internal fun exportResources(outputDirectory: File) {
	
	// read what should be exported
	val exportText = readFromJar("/export.txt")
	
	// split into lines and remove blanks and commands 
	val exportListFiltered = exportText
			.lines() // all lines
			.map(String::trim) // make sure there are no indents
			.filter(String::isNotBlank) // ignore blanks
			.filterNot { it.startsWith("#") } // ignore comments
	
	// TODO: [LOW] could turn this into a getParentFile exists? -> mkdirs instead
	
	// create directories
	exportListFiltered
			.filter { it.endsWith("/") } // only directory creations
			.forEach { File(outputDirectory, it).mkdirs() } // create the directories
	
	// export files
	exportListFiltered
			.filterNot { it.endsWith("/") } // ignore directory creations
			.forEach { jarPath -> pst {
				val fileContents = readFromJar("/web/$jarPath") // read
				open(File(outputDirectory, jarPath), "w").use { it.writeText(fileContents) } // write
			} }
	
}
