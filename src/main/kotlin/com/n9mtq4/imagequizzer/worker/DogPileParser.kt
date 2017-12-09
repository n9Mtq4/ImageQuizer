package com.n9mtq4.imagequizzer.worker

import com.n9mtq4.imagequizzer.USER_AGENT
import com.n9mtq4.kotlin.extlib.pstAndNull
import org.jsoup.Jsoup
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by will on 12/8/2017 at 10:38 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
private const val SEARCH_URL = "http://www.dogpile.com/dogpl.bko.y/search/images?q=%s"
private const val IMAGE_LINK_SELECTOR = "#webResults > div > div.resultThumbnailPane > a"

/**
 * returns a list of image links from a
 * search query
 * */
internal fun getImageLinksFromQuery(query: String, size: Int = -1) = getImageLinks(encodeSearchUrl(query), size)

/**
 * Takes a query and returns the dogpile image search
 * link
 * */
private fun encodeSearchUrl(query: String): String {
	val encodedQuery = URLEncoder.encode(query, "UTF-8") // encode the query
	val url = SEARCH_URL.replace("%s", encodedQuery) // add the query into the url
	return url
}

/**
 * Takes a dogpile image search link and returns
 * size number of direct links to images that are displayed
 * from the url
 * */
private fun getImageLinks(url: String, size: Int): List<String> {
	
	val doc = Jsoup.connect(url).userAgent(USER_AGENT).get() // get the dom for the results
	val elements = doc.select(IMAGE_LINK_SELECTOR) // find all the image links
	
	// now decode the hrefs from dogpile's thumbnails to full images
	val links = elements
			.map { it.attr("href") }
			.mapNotNull { pstAndNull { decodeImageHandler(it) } }
	
	// return only a specific size
	// if size == -1, then return the whole thing
	return if (size == -1) links else links.take(size)
	
}

/**
 * Removes dogpile's image tracker link that surrounds the direct
 * link to the image
 * */
private fun decodeImageHandler(url: String): String {
	
	var rawUrl = "http${url.split("http")[2]}" // find the second http. the first is the dogpile, the second is the image url
	rawUrl = rawUrl.substring(0, rawUrl.length - 5) // trim off dogpile params on the end
	val decode = decodeUrl(decodeUrl(rawUrl)) // decode the url - have to do it twice. not sure why, it just works
	val adjUrl = decode.substring(0, decode.length - 1) // leaves a '&' on the end, have to remove it
	return adjUrl
	
}

/**
 * A shorter version of URLDecoder.decode
 * that doesn't require a string encoding and just
 * defaults to uft-8
 * */
private fun decodeUrl(urlString: String) = URLDecoder.decode(urlString, "UTF-8")
