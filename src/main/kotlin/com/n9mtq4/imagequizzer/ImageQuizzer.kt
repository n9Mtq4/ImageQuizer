package com.n9mtq4.imagequizzer

import com.n9mtq4.imagequizzer.ui.ListEditorWindow
import com.n9mtq4.kotlin.extlib.pstAndUnit
import javax.swing.UIManager

/**
 * Created by will on 3/17/16 at 9:23 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun main(args: Array<String>) {
	
	pstAndUnit { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()) }
	
	ListEditorWindow()
	
}
