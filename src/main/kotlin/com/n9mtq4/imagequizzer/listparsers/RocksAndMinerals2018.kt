package com.n9mtq4.imagequizzer.listparsers

/**
 * Created by will on 12/9/2017 at 4:29 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

private val numRegex = Regex("(\\(\\d+\\)|\\s-\\s|,|group)")
private val familyRegex = Regex("(.+(ates|ides|xene))|(native|Elements)", RegexOption.IGNORE_CASE)
private val rockFamilyRegex = Regex("(igneous|sedimentary|metamorphic)", RegexOption.IGNORE_CASE)
private val dontSpaceSplitRegex = Regex("(quartz|schist|satin)", RegexOption.IGNORE_CASE)
private val otherBadStuffRegex = Regex("(potassium|plagioclase)", RegexOption.IGNORE_CASE)

class RocksAndMinerals2018 : ListParser("Rocks and Minerals 2018") {
	
	// this is very bad code. its just whatever worked to filter it down
	override fun parseList(string: String): List<String> {
		
		val lines = string.lines()
		val minerals = lines
				.filter { it.contains(numRegex) }
				.flatMap { it.split(numRegex) }
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
				.map { if (it == "Crystal") "Crystal Quartz" else it }
				.map(String::trim).filterNot(String::isBlank)
		
		return minerals
		
	}
	
}
