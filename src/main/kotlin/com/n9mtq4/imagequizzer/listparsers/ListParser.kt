package com.n9mtq4.imagequizzer.listparsers

import com.n9mtq4.imagequizzer.listparsers.scioly.RocksAndMinerals2018

/**
 * Created by will on 12/9/2017 at 4:28 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

val IDENTITY_PARSER = IdentityParser()

/**
 * A list of all the scioly link parsers
 * */
val LIST_PARSER_SCIOLY = listOf(RocksAndMinerals2018())

typealias ParserOutput = List<Pair<String, String>>

/**
 * An interface for creating classes that
 * can parse what is put into the main ui window.
 * 
 * Must have a name and a function to parse the list
 * that returns a list of strings.
 * */
interface ListParser {
	
	val name: String
	
	fun parseList(string: String): ParserOutput
	
}
