package com.n9mtq4.imagequizzer.ui

import com.n9mtq4.imagequizzer.listparsers.IDENTITY_PARSER
import com.n9mtq4.imagequizzer.listparsers.LIST_PARSER_SCIOLY
import com.n9mtq4.imagequizzer.listparsers.ListParser
import com.n9mtq4.imagequizzer.worker.queryListToDatabaseAndImages
import com.n9mtq4.imagequizzer.worker.readFromJar
import com.n9mtq4.kotlin.extlib.ignoreAndNull
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.Dimension
import java.io.File
import java.net.URI
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
	private var prefix = ""
	private var suffix = ""
	private var numImages = -1
	private var shouldDownload = false
	
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
				
				menuItem("Open")
						.shortcut('o')
				menuItem("Save")
						.shortcut('s')
				menuItem("Save As")
						.shortcut('s', shift = true)
				menuItem("Clear")
						.shortcut('l', shift = true)
				menuItem("Exit")
						.onActionSafeIgnore { frame.dispose() }
				
			}
			
			menuList("Options") {
				
				menuItem("Prefix")
						.onActionSafePst { requestString(frame, "Please enter a prefix\nCurrently: '$prefix'", prefix) { prefix = it } }
				menuItem("Suffix")
						.onActionSafePst {requestString(frame, "Please enter a suffix\nCurrently: '$suffix'", suffix) { suffix = it } }
				menuItem("Number of images").onActionSafePst {
					requestString(frame, "Please enter the number of images (-1 for all)", numImages.toString()) {
						val i = ignoreAndNull { it.toInt() }
						numImages = i ?: run {
							JOptionPane.showMessageDialog(frame, "Please enter a number\n(currently $numImages)", "Error", JOptionPane.ERROR_MESSAGE)
							return@requestString
						}
					}
				}
				menuCheckboxItem("Download Images")
						.onValueUpdate { shouldDownload = it } // update our value based on what user clicks
						.applyOnMenuCheckboxItem { isSelected = shouldDownload } // default value of shouldDownload
				
			}
			
			menuList("Parser") {
				
				menuList("Scioly Parsers") {
					LIST_PARSER_SCIOLY.forEach { parser ->
						menuItem(parser.name).onActionSafePst { applyParser(parser) }
					}
				}
				
				menuItem(IDENTITY_PARSER.name).onActionSafePst { applyParser(IDENTITY_PARSER) }
				
			}
			
			menuList("Stats") {
				
				menuItem("Line Count")
						.onActionSafePst { JOptionPane.showMessageDialog(frame, "There are ${textArea.text.lines().size} lines.") }
				
			}
			
			menuList("Info") {
				
				menuItem("Submit Bug")
						.onActionSafeIgnore { Desktop.getDesktop().browse(URI.create("https://github.com/n9Mtq4/ImageQuizzer/issues")) }
				menuItem("Check for update")
				menuItem("About")
						.onActionSafePst { readFromJar("/about.txt").run(::println) } // TODO: some gui for this
				
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
		
		goButton.addActionListener { e ->
			val outputDir = openDirectoryChooser(frame, "Where to save?") ?: return@addActionListener
			println("Started")
			// generate the database for them
			queryListToDatabaseAndImages(IDENTITY_PARSER.parseList(textArea.text), outputDir, prefix, suffix, numImages, shouldDownload)
			println("Done")
		}
		
	}
	
	private fun applyParser(parser: ListParser) {
		textArea.text = parser.parseList(textArea.text).joinToString(separator = "\n")
	}
	
}
