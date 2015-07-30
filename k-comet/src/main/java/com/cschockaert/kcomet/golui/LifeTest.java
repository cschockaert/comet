package com.cschockaert.kcomet.golui;


import javax.swing.JFrame;

import com.cschockaert.kcomet.bus.ClientStartup;


/**
 * main Class that creates and resizes the JFrame
 * @author anuccio
 *
 */
public class LifeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientStartup cs = new ClientStartup();
		
		Life box = new Life();
		
		box.setSize(500,400);
		box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		box.pack();
		box.setVisible(true);

	}

}
