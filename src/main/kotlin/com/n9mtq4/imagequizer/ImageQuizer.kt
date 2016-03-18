package com.n9mtq4.imagequizer

import java.io.File

/**
 * Created by will on 3/17/16 at 9:23 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun main(args: Array<String>) {
	
	downloadImages(getImageLinks(encodeSearchUrl("rocks"), 5), File("imgs"), "rock")
	
}
