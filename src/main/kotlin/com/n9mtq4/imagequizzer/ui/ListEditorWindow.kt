package com.n9mtq4.imagequizzer.ui

import com.n9mtq4.kotlin.extlib.io.open
import java.awt.BorderLayout
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
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
 * @author Will "n9Mtq4" Bresnahan
 */
class ListEditorWindow {
	
	companion object {
		private const val DEFAULT_TITLE = "Image Quizzer"
	}
	
	private val frame: JFrame
	private val textArea: JTextArea
	private val go: JButton
	
	private var lastSave: File? = null
	
	init {
		
		this.frame = JFrame(DEFAULT_TITLE)
		
		this.textArea = JTextArea()
		this.go = JButton("Start")
		
		val menuBar = menuBar {
			
			menuList("File") {
				
				menuItem("Open").onAction { println(it.source) }.shortcut('o')
				menuItem("Save").onAction { save(lastSave) }.shortcut('s')
				menuItem("Save As").onAction { save(null) }.shortcut('s', shift = true)
				menuItem("Clear").onAction { textArea.text = "" }.shortcut('c', shift = true)
				menuItem("Exit").onAction { frame.dispose() }
				
			}
			
			menuList("Options") {
				
				menuItem("Prefix")
				menuItem("Suffix")
				menuItem("Number of images")
				
			}
			
			menuList("Stats") {
				
				menuItem("Line count").onAction { JOptionPane.showMessageDialog(frame, "There are ${textArea.text.lines().size} lines") }
				
			}
			
			menuList("Info") {
				
				menuItem("Contact / Help")
				menuItem("Check for update")
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
			override fun windowClosing(e: WindowEvent) {
				if (!frame.title.endsWith("*", ignoreCase = true)) return
//				TODO: ask them if they want to save
			}
			override fun windowClosed(e: WindowEvent) {}
			override fun windowOpened(e: WindowEvent) {}
		})
		
	}
	
	private fun invalidateSave() {
		frame.title = "$DEFAULT_TITLE - ${lastSave?.name ?: "Unsaved"}*"
	}
	
	private fun validateSave() {
		frame.title = "$DEFAULT_TITLE - ${lastSave?.name ?: "Unsaved"}"
	}
	
	private fun save(f: File?) {
		
		lastSave = f ?: openSaveDialog() ?: return // if f is null, open the save dialog. If the save dialog is null return
		
		open(lastSave ?: return, "w").write(textArea.text).close() // save to said file
		
		validateSave()
		
	}
	
	private fun openSaveDialog(): File? {
		
		val chooser = JFileChooser()
		chooser.dialogTitle = "Where to save?"
		val result = chooser.showSaveDialog(frame)
		return if (result == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
		
	}
	
}
