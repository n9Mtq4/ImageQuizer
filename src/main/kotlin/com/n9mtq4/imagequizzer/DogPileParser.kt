package com.n9mtq4.imagequizzer

import com.n9mtq4.kotlin.extlib.pstAndNull
import org.jsoup.Jsoup
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by will on 3/17/16 at 9:25 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
private const val SEARCH_URL = "http://www.dogpile.com/dogpl.bko.y/search/images?q=%s"
private const val IMAGE_LINK_SELECTOR = "#webResults > div > div.resultThumbnailPane > a"

/**
 * Takes a query and returns the dogpile image search
 * link
 * */
internal fun encodeSearchUrl(query: String): String {
	val encodedQuery = URLEncoder.encode(query, "UTF-8") // encode the query
	val url = SEARCH_URL.replace("%s", encodedQuery) // add the query into the url
	return url
}

/**
 * Takes a dogpile image search link and returns
 * size number of direct links to images that are displayed
 * from the url
 * */
@Suppress("UNCHECKED_CAST")
internal fun getImageLinks(url: String, size: Int): List<String> {
	
	try {
		val doc = Jsoup.connect(url).userAgent(USER_AGENT).get() // get the dom for the results
		val elements = doc.select(IMAGE_LINK_SELECTOR) // find all image links
		
		val links = elements.map { it.attr("href") }.map { pstAndNull { decodeImageHandler(it) } } // decode all the hrefs
		
		// return only a specific size - we can cast cause we filtered by it != null
		if (size != -1) return links.subList(0, size) as List<String>
		else return links as List<String>
		
	}catch (e: Exception) {
		e.printStackTrace()
	}
	
	return listOf()
	
}

/**
 * Removes dogpile's image tracker link that surrounds the direct
 * link to the image
 * */
private fun decodeImageHandler(url: String): String {
	
	// I wrote this on 3/10/15 - one year later and I have no idea what this code does :(
	var rawUrl = "http${url.split("http")[2]}"
	rawUrl = rawUrl.substring(0, rawUrl.length - 4)
	val decode = URLDecoder.decode(rawUrl, "UTF-8")
	println(decode)
	return decode
	
}
