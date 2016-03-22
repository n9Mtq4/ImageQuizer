package com.n9mtq4.imagequizzer.worker

import com.n9mtq4.imagequizzer.model.ImageQuizzerModel
import com.n9mtq4.kotlin.extlib.io.FileKt
import com.n9mtq4.kotlin.extlib.io.open
import com.n9mtq4.kotlin.extlib.pstAndGiven
import java.io.File

/**
 * Created by will on 3/19/16 at 9:38 PM.
 * 
 * Throughout the code in here, there are many
 * ```
 * if (stop) return
 * ```
 * this is for thread support, so we can stop this from running at any point in time
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class ImageDlThread(val model: ImageQuizzerModel, val progressCallback: (Int, Int) -> Unit = { i1, i2 -> }, val doneCallback: () -> Unit = {}) : Thread("Image Download") {
	
	private var stop = false
	
	override fun run() {
		
		val lines = model.text.lines()
		
		val total = if (model.download) lines.size * 2 else lines.size
		
		val urlLinks = lines.map {
			if (checkStop()) return
			encodeSearchUrl(it)
		}.mapIndexed { i, it ->
			if (checkStop()) return
			progressCallback.invoke(1 + i, total)
			getImageLinks(it, model.imageCount)
		}
		
		val links: List<List<String>>
		if (model.download) {
			val dir = File(model.dir, "imgs/")
			links = urlLinks.mapIndexed { i, list ->
				if (checkStop()) return
				progressCallback.invoke(1 + i + lines.size, total)
				pstAndGiven(listOf("ERROR!")) { downloadImages(list, dir, i.toString(), this) }
			}
		}else {
			links = urlLinks
		}
		
		if (checkStop()) return
		writeJsDatabase(lines.zip(links), open(File(model.dir, "database.js"), "w"))
		
//		the end
		doneCallback.invoke()
		
	}
	
	internal fun cancel() {
		this.stop = true
	}
	
	private fun writeJsDatabase(links: List<Pair<String, List<String>>>, fileToWrite: FileKt) {
		
		if (checkStop()) return
		fileToWrite.writeln("/*AUTO GENERATED CODE FROM n9Mtq4's QUIZ MAKER*/")
		val db = links.map { 
			if (checkStop()) return
			it.second.joinToString(separator = ",", prefix = "[\"${it.first}\",[", postfix = "]]") { "\"$it\"" }
		}.joinToString(separator = ",\n", prefix = "var imageDB = [", postfix = "];")
		
		if (checkStop()) return
		fileToWrite.writeln(db)
		
		fileToWrite.close()
		
	}
	
	internal fun checkStop(): Boolean {
		
		if (stop) doneCallback.invoke()
		return stop
		
	}
	
}
