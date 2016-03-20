package com.n9mtq4.imagequizzer

import com.n9mtq4.imagequizzer.ui.ListEditorWindow
import java.io.File

/**
 * Created by will on 3/19/16 at 9:38 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ImageDlThread(val parent: ListEditorWindow, val outputDir: File) : Thread("Image Download") {
	
	override fun run() {
		
		
		
	}
	
	internal fun cancel() {
		
	}
	
}
