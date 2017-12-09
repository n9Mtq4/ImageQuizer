package com.n9mtq4.imagequizzer.worker

import com.n9mtq4.kotlin.extlib.io.open
import com.n9mtq4.kotlin.extlib.pst
import com.n9mtq4.kotlin.extlib.pstAndGiven
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Created by will on 12/9/2017 at 3:11 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Copies all files listed in export.txt inside
 * the jar from inside the exported jar file
 * to outside the exported jar file
 * */
internal fun exportWebFromJar(outputDir: File) {
	
	// get the files it needs to export
	readFromJar("export.txt").lines().forEach { name ->
		pst { 
			val jarPath = "web/$name" // path inside jar
			val text = readFromJar(jarPath) // contents
			open(File(outputDir, name), "w").use { it.write(text) } // copy it outside jar
		}
	}
	
}

/**
 * Reads the text from a file inside this jar
 * */
private fun readFromJar(path: String): String = pstAndGiven("Error when exporting files!") {
	
	// TODO: not sure if object {} is the best way to do this
	val input = object {}::class.java.getResourceAsStream(path)
	val ir = InputStreamReader(input)
	val br = BufferedReader(ir)
	
	val text = br.readLines().joinToString(separator = "\n")
	
	br.close()
	ir.close()
	input.close()
	
	return@pstAndGiven text
	
}
