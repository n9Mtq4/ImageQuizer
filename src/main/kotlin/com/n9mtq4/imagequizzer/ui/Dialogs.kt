package com.n9mtq4.imagequizzer.ui

import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane

/**
 * Created by will on 3/19/16 at 9:40 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
/**
 * Opens the JFileChooser for saving
 * */
internal  fun openSaveDialog(parent: JFrame, title: String): File? {
	
	val chooser = JFileChooser()
	chooser.dialogTitle = title
	val result = chooser.showSaveDialog(parent)
	return if (result == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
	
}

/**
 * Opens the JFileChooser for opening files
 * */
internal fun openOpenDialog(parent: JFrame, title: String): File? {
	
	val chooser = JFileChooser()
	chooser.dialogTitle = title
	val result = chooser.showOpenDialog(parent)
	return if (result == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
	
}

/**
 * Opens the JFileChooser for opening directories
 * */
internal fun openDirectoryChooser(parent: JFrame, title: String): File? {
	
	val chooser = JFileChooser()
	chooser.dialogTitle = title
	chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
	chooser.isAcceptAllFileFilterUsed = false
	val result = chooser.showOpenDialog(parent)
	return if (result == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
	
}

/**
 * Asks the user for a string, runs onSuccess of they entered one
 * */
internal  fun requestString(parent: JFrame, text: String, initValue: String = "", onSuccess: (String) -> Unit) {
	val response = JOptionPane.showInputDialog(parent, text, initValue)
	onSuccess.invoke(response ?: return)
}

