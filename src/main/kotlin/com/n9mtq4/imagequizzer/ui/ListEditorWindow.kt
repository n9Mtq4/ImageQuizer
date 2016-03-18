package com.n9mtq4.imagequizzer.ui

import javax.swing.JFrame
import javax.swing.WindowConstants

/**
 * Created by will on 3/18/16 at 12:59 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ListEditorWindow {
	
	private val frame: JFrame
	
	init {
		
		this.frame = JFrame("Image Quizzer")
		
		val menuBar = menuBar {
			
			menuList("File") {
				
				menuItem("Open").onAction { println(it.source) }
				menuItem("Save").onAction { println(it.source) }
				
			}
			
		}
		
		frame.jMenuBar = menuBar
		
		frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
		frame.pack()
		frame.isVisible = true
		frame.setLocationRelativeTo(null) // center
		
	}
	
}
