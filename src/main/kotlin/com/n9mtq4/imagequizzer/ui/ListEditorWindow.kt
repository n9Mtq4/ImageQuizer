package com.n9mtq4.imagequizzer.ui

import com.n9mtq4.imagequizzer.checkForUpdate
import com.n9mtq4.imagequizzer.model.ImageQuizzerModel
import com.n9mtq4.imagequizzer.worker.ImageDlThread
import com.n9mtq4.kotlin.extlib.ignoreAndNull
import com.n9mtq4.kotlin.extlib.io.open
import com.n9mtq4.kotlin.extlib.pstAndUnit
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.File
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.WindowConstants
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * Created by will on 3/18/16 at 12:59 PM.
 * 
 * TODO: this file is large - move things to other files
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class ListEditorWindow {
	
	companion object {
		private const val DEFAULT_TITLE = "Image Quizzer"
	}
	
	internal val frame: JFrame
	internal val textArea: JTextArea
	internal val go: JButton
	
	private var lastSave: File? = null
	
	internal var prefix = ""
		private set
	internal var suffix = ""
		private set
	internal var imageSize = -1
		private set
	internal var download = false
		private set
	
	private var thread: ImageDlThread? = null
	
	init {
		
		this.frame = JFrame(DEFAULT_TITLE)
		
		this.textArea = JTextArea()
		this.go = JButton("Start")
		
		val menuBar = menuBar {
			
			menuList("File") {
				
				menuItem("Open").onAction { open() }.shortcut('o')
				menuItem("Save").onAction { save(lastSave) }.shortcut('s')
				menuItem("Save As").onAction { save(null) }.shortcut('s', shift = true)
				menuItem("Clear").onAction { textArea.text = "" }.shortcut('c', shift = true)
				menuItem("Exit").onAction { frame.dispose() }
				
			}
			
			menuList("Options") {
				
				menuItem("Prefix").onAction { requestString(frame, "Please enter a prefix\n(currently '$prefix')", prefix) { prefix = it } }
				menuItem("Suffix").onAction { requestString(frame, "Please enter a suffix\n(currently '$suffix')", suffix) { suffix = it } }
				menuItem("Number of images").onAction {
					requestString(frame, "Please enter the number of images (-1 for all)", imageSize.toString()) {
						val i = ignoreAndNull { it.toInt() }
						if (i == null) {
							JOptionPane.showMessageDialog(frame, "Please enter a number\n(currently $imageSize)", "Error", JOptionPane.ERROR_MESSAGE)
							return@requestString
						}
						imageSize = i
					}
				}
				menuCheckboxItem("Download Images").onValueUpdate { download = it }.applyOnMenuCheckboxItem { isSelected = download }
				
			}
			
			menuList("Stats") {
				
				menuItem("Line count").onAction { JOptionPane.showMessageDialog(frame, "There are ${textArea.text.lines().size} lines") }
				
			}
			
			menuList("Info") {
				
				menuItem("Contact / Help")
				menuItem("Check for update").onAction { checkForUpdate() }
				menuItem("About")
				
			}
			
		}
		
		frame.jMenuBar = menuBar
		
		// wrap the textArea in a scroll pane that always shows the scrollbars
		frame.add(JScrollPane(textArea).apply { 
			verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
			horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		}, BorderLayout.CENTER)
		frame.add(go, BorderLayout.SOUTH)
		
		frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
		frame.pack()
		frame.isVisible = true
		frame.setLocationRelativeTo(null) // center
		
		textArea.document.addDocumentListener(object : DocumentListener {
			override fun changedUpdate(e: DocumentEvent) = invalidateSave()
			override fun insertUpdate(e: DocumentEvent) = invalidateSave()
			override fun removeUpdate(e: DocumentEvent) = invalidateSave()
		})
		
		frame.addWindowListener(object : WindowListener {
			override fun windowDeiconified(e: WindowEvent) {}
			override fun windowActivated(e: WindowEvent) {}
			override fun windowDeactivated(e: WindowEvent) {}
			override fun windowIconified(e: WindowEvent) {}
			override fun windowClosing(e: WindowEvent) { if (isSaveInvalidated()) askForSave() }
			override fun windowClosed(e: WindowEvent) {}
			override fun windowOpened(e: WindowEvent) {}
		})
		
		go.addActionListener { goButton(it) }
		
	}
	
	/**
	 * When the go button is pressed
	 * */
	private fun goButton(e: ActionEvent) {
		
		val shouldStart = go.text.equals("start", ignoreCase = true)
		
		if (shouldStart) {
			// start
			// shouldn't be triggered, but its good to check
			stopThread()
			
			val outputDir = openDirectoryChooser(frame, "Where to save the quiz?") ?: return
			
			val model = ImageQuizzerModel(prefix, suffix, imageSize, download, outputDir, textArea.text)
			
			val progressWindow = ProgressWindow(this)
			thread = ImageDlThread(model, 
					progressCallback = {
						i, t -> progressWindow.update(i, t)
					}, 
					doneCallback = {
						updateGoButtonStatus(false)
						progressWindow.dispose()
					})
					.apply { start() }
			
		}else {
			// stop
			stopThread()
		}
		
		updateGoButtonStatus(thread?.isAlive ?: false)
		
	}
	
	/**
	 * Stops the ImageDlThread
	 * */
	internal fun stopThread() {
		thread ?: return // if its null, leave
		if (!(thread?.isAlive ?: false)) return // if its dead or null, leave
		pstAndUnit { 
			thread?.apply { 
				cancel()
				join()
			}
		}
	}
	
	/**
	 * sets the go button to stop or start based on if the downloader is
	 * running or not
	 * */
	internal fun updateGoButtonStatus(isRunning: Boolean) {
		go.text = if (isRunning) "Stop" else "Start"
	}
	
	/**
	 * Add the star to the frame
	 * */
	private fun invalidateSave() {
		frame.title = "$DEFAULT_TITLE - ${lastSave?.name ?: "Unsaved"}*"
	}
	
	/**
	 * Remove the star from the frame
	 * */
	private fun validateSave() {
		frame.title = "$DEFAULT_TITLE - ${lastSave?.name ?: "Unsaved"}"
	}
	
	/**
	 * Reads data from a text file and sets it in the textArea
	 * */
	private fun open() {
		
		if (isSaveInvalidated()) askForSave() // we want to save the data before overwriting it with this new stuff
		
		val file = open(openOpenDialog(frame, "Open list") ?: return, "r") // open with only read access
		val text = file.readAll() // read everything
		
		textArea.text = text
		
		file.close()
		
	}
	
	/**
	 * Saves the data in the textArea to a file
	 * */
	private fun save(f: File?) {
		
		lastSave = f ?: openSaveDialog(frame, "Where to save?") ?: return // if f is null, open the save dialog. If the save dialog is null return
		
		val rawFile = lastSave ?: return // return if the file is null
		val file = if (rawFile.name.contains(".")) rawFile else File(rawFile.parentFile, rawFile.name + ".txt") // add a .txt if it isn't present
		open(file, "w").write(textArea.text).close() // save to said file
		
		validateSave()
		
	}
	
	/**
	 * Asks the user if they want to save their current work
	 * */
	private fun askForSave() {
		
		val response = JOptionPane.showConfirmDialog(frame, "Do you want to save?", "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
		if (response != JOptionPane.YES_OPTION) return
		save(lastSave)
		
	}
	
	/**
	 * Has the user saved their data. true = no, false = yes
	 * */
	private fun isSaveInvalidated() = frame.title.endsWith("*", ignoreCase = true)
	
}
