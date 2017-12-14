package com.n9mtq4.imagequizzer.listparsers

/**
 * Created by will on 12/9/2017 at 4:29 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

private val splitRegex = Regex("(\\(\\d+\\)|\\s-\\s|,|group)")
private val familyRegex = Regex("(.+(ates|ides|xene))|(native|Elements)", RegexOption.IGNORE_CASE)
private val rockFamilyRegex = Regex("(igneous|sedimentary|metamorphic)", RegexOption.IGNORE_CASE)
private val dontSpaceSplitRegex = Regex("(quartz|schist|satin)", RegexOption.IGNORE_CASE)
private val otherBadStuffRegex = Regex("(potassium|plagioclase)", RegexOption.IGNORE_CASE)

private val nativeElementRegex = Regex("(gold|silver|copper|sulfur)", RegexOption.IGNORE_CASE)
private val gemstoneNeedsRoughRegex = Regex("(tourmaline|diamond|topaz|opal|rose\\squarts|labradorite)", RegexOption.IGNORE_CASE)

/**
 * A class for parsing the Rocks and Minerals Science
 * Olympiad event for 2018.
 * 
 * Separates out the minerals and rocks onto their 
 * own line and cleans it up for correct search
 * */
class RocksAndMinerals2018 : ListParser {
	
	override val name = "Rocks and Minerals 2018"
	
	// this is very bad code. its just whatever worked to filter it down
	override fun parseList(string: String): ParserOutput {
		
		val lines = string.lines()
		
		// the terms
		val minerals = lines
				.filter { it.contains(splitRegex) }
				.flatMap { it.split(splitRegex) }
				.flatMap { it.split("group", ignoreCase = true) }
				.flatMap { it.split(": ") }
				.filterNot { it.equals("feldspar", ignoreCase = true) }
				.filterNot { it.contains(":") }
				.filterNot { it.contains("varieties", ignoreCase = true) }
				.flatMap { if (it.contains(dontSpaceSplitRegex)) listOf(it) else it.split(" ") }
				.filterNot { it.equals("feldspar", ignoreCase = true) }
				.filterNot { it.contains("[") || it.contains("]") }
				.filterNot { it.contains(familyRegex) }
				.filterNot { it.contains(rockFamilyRegex) }
				.filterNot { it.contains(otherBadStuffRegex) }
				.map { it.replace("/Microcline", "") }
				.map { it.replaceIfEq("Crystal", "Quartz Crystals") }
				.map(String::trim).filterNot(String::isBlank)
		
		// the queries
		val queries = minerals
				.map { it.replaceIfEq("Marble", "Marble Rock") }
				.map { it.replaceIfEq("Selenite", "Selenite Gypsum Crystal") }
				.map { if (!it.matches(nativeElementRegex)) it else "Native $it" }
				.map { if (!it.matches(gemstoneNeedsRoughRegex)) it else "Rough $it" }
		
		return minerals.zip(queries)
		
	}
	
}

private fun String.replaceIfEq(equals: String, newString: String) = if (this == equals) newString else this
