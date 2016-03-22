package com.n9mtq4.imagequizzer.ui

import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JProgressBar
import javax.swing.WindowConstants

/**
 * Created by will on 3/21/16 at 10:10 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ProgressWindow(val parent: ListEditorWindow) {
	
	private val frame: JFrame
	private val progressBar: JProgressBar
//	private val stop: JButton
	
	init {
		
		this.frame = JFrame("Progress")
		frame.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
		
		this.progressBar = JProgressBar()
//		this.stop = JButton("Stop")
		
		frame.add(progressBar, BorderLayout.CENTER)
//		frame.add(stop, BorderLayout.SOUTH)
		
		frame.pack()
		frame.isVisible = true
		frame.setSize(300, frame.height);
		frame.setLocationRelativeTo(parent.frame)
		
//		stop.addActionListener { parent.stopThread() }
//		frame.rootPane.defaultButton = stop
		
//		frame.isAlwaysOnTop = true
		
	}
	
	internal fun dispose() {
		frame.dispose()
	}
	
	/**
	 * @param i the current progress
	 * @param t the total steps
	 * */
	internal fun update(i: Int, t: Int) {
		
		progressBar.maximum = t
		progressBar.value = i
		
	}
	
}
