package com.n9mtq4.imagequizzer

import com.n9mtq4.imagequizzer.ui.ListEditorWindow
import com.n9mtq4.kotlin.extlib.pst
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import javax.swing.UIManager

/**
 * Created by will on 12/8/2017 at 10:21 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * The main function. Runs on application
 * start.
 * */
fun main(args: Array<String>) {
	
	// the cli options
	val options = Options().apply {
		
		/*
		* will have options for a list of
		* things to find pictures of and
		* a output directory
		* */
		
	}
	
	// parse the cli input
	val parser: CommandLineParser = GnuParser()
	val line = parser.parse(options, args)
	
	// print out the help message
	if (line.hasOption("help")) {
		val helpFormatter = HelpFormatter()
		helpFormatter.printHelp("java -jar ImageQuizzer [OPTIONS]", options)
		return
	}
	
	// what should we do?
	// TODO: use command line flags to figure out which one
	val cli = false
	
	if (cli) launchCommandLine()
	else launchUi()
	
}

/**
 * Function for starting the command line
 * version of this program
 * */
private fun launchCommandLine() {
	
}

/**
 * Function for starting the gui
 * version of this program
 * */
private fun launchUi() {
	
	pst { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()) }
	
	ListEditorWindow()
	
}
