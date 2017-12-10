package com.n9mtq4.imagequizzer.listparsers

/**
 * Created by will on 12/9/2017 at 4:28 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

val LIST_PARSER_SCIOLY = listOf(RocksAndMinerals2018())

abstract class ListParser(val name: String) {
	
	abstract fun parseList(string: String): List<String>
	
}
