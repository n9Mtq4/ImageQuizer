package com.n9mtq4.kotlin.doesntwork;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Created by will on 3/18/16 at 10:43 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public class AddMenuAccelerator {
	private AddMenuAccelerator() {}
	
	public static void addKey(JMenuItem menuItem, char key, int mask) {
		menuItem.setAccelerator(KeyStroke.getKeyStroke(Character.toUpperCase(key), mask));
	}
	
}
