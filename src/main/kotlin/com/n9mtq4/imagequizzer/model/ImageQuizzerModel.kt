package com.n9mtq4.imagequizzer.model

import java.io.File
import java.io.Serializable

/**
 * Created by will on 3/21/16 at 8:55 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
data class ImageQuizzerModel(var prefix: String = "", var suffix: String = "", var imageCount: Int = -1, var download: Boolean = false, var dir: File, var text: String) : Serializable {
	companion object {
		private val serialVersionUID = 2342148767777376637L;
	}
}
