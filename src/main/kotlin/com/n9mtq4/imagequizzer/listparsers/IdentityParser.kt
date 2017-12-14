package com.n9mtq4.imagequizzer.listparsers

/**
 * Created by will on 12/13/2017 at 8:52 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Regex that matches if the identity parser has been applied before.
 * Basically this matches [Pair.toString] with <A, B> as strings.
 * ```(String1, String2)```
 * */
private val PAIR_REGEX = Regex("\\(.+,\\s.+\\)", RegexOption.IGNORE_CASE)

/**
 * Regex that matches the first part of the [Pair.toString] with <A, B> as strings.
 * 
 * Ex: In ```(String1, String2)```, it matches ```String1```
 * */
private val COMPONENT1_REGEX = Regex("(?<=\\().+(?=,)", RegexOption.IGNORE_CASE)

/**
 * Regex that matches the second part of the [Pair.toString] with <A, B> as strings.
 *
 * Ex: In ```(String1, String2)```, it matches ```String2```
 * */
private val COMPONENT2_REGEX = Regex("(?<=,\\s).+(?=\\))", RegexOption.IGNORE_CASE)

/**
 * A [ListParser] that turns
 * an already parsed list, in the form
 * of text back into the parser output.
 * */
class IdentityParser : ListParser {
	
	override val name: String = "Identity Parser"
	
	override fun parseList(string: String): ParserOutput {
		
		// TODO: make the errors a bit more obvious
		
		val out = string
				.lines()
				.map(String::trim)
				.filter(String::isNotBlank)
				.map {
					if (it.matches(PAIR_REGEX)) {
						val component1 = COMPONENT1_REGEX.find(it)?.value ?: "Error in parsing! (IdentityParser COMPONENT1_REGEX)"
						val component2 = COMPONENT2_REGEX.find(it)?.value ?: "Error in parsing! (IdentityParser COMPONENT2_REGEX)"
						component1 to component2
					}else {
						it to it
					}
				}
		
		return out
		
	}
	
}
