package com.n9mtq4.imagequizzer.ui

import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File
import javax.swing.*

/**
 * Created by will on 12/9/2017 at 1:51 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

private const val DEFAULT_TITLE = "Image Quizzer"

internal class ListEditorWindow {
	
	internal val frame: JFrame
	internal val textArea: JTextArea
	internal val goButton: JButton
	
	private var lastSave: File? = null
	
	// start user preferences / options
	internal var prefix = ""
		private set
	internal var suffix = ""
		private set
	internal var numImages = -2
		private set
	internal var shouldDownload = false
		private set
	
	init {
		
		// frame and major components
		this.frame = JFrame(DEFAULT_TITLE).apply { 
			preferredSize = Dimension(400, 400)
		}
		
		this.textArea = JTextArea()
		this.goButton = JButton("Start")
		
		// generate the menu bar
		val menuBar = menuBar {
			
			menuList("File") {
				
				menuItem("Open").shortcut('o')
				menuItem("Save").shortcut('s')
				menuItem("Save As").shortcut('s', shift = true)
				menuItem("Clear").shortcut('l', shift = true)
				menuItem("Exit")
				
			}
			
			menuList("Options") {
				
				menuItem("Prefix")
				menuItem("Suffix")
				menuItem("Number of images")
				menuCheckboxItem("Download Images")
						.onValueUpdate { shouldDownload = it } // update our value based on what user clicks
						.applyOnMenuCheckboxItem { isSelected = shouldDownload } // default value of shouldDownload
				
			}
			
			menuList("Stats") {
				
				menuItem("Line Count")
				
			}
			
			menuList("Info") {
				
				menuItem("Submit Bug")
				menuItem("Check for update")
				menuItem("About")
				
			}
			
		}
		
		// stick it all in the window
		frame.run { 
			
			jMenuBar = menuBar
			
			// wrap the textArea in a scroll pane that always shows the scrollbars
			add(JScrollPane(textArea).apply { 
				verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
				horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
			}, BorderLayout.CENTER)
			
			add(goButton, BorderLayout.SOUTH)
			
			defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
			pack()
			isVisible = true
			setLocationRelativeTo(null) // center of the screen
			
		}
		
	}
	
}
