package com.cschockaert.kcomet.golui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;

/**
 * Life Class that simulates the game of life on a JFrame
 * 
 * @author anuccio
 *
 */
public class Life extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1277663095265878118L;

	/** stores the alive status */
	private World world;

	private God god;

	/** boolean value */
	private boolean toggle = false;

	/**
	 * Constructor that initializes the JFrame
	 */
	public Life() { // constructor initiates GUI
		super("Game of Life");

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(jsp);
		world = new World();
		JPanel bottomContainer = new JPanel();
		jsp.add(world);
		jsp.add(bottomContainer);

		god = new God(world);
		bottomContainer.add(new JButton(new AbstractAction("run/stop") {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8033851335486507009L;

			@Override
			public void actionPerformed(ActionEvent e) {
				toggle = !toggle;

				if (toggle) {
					beginGame();
				} else {
					endGame();
				}
			}
		}));

		bottomContainer.add(new JButton(new AbstractAction("next generation") {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8033851335486507009L;

			@Override
			public void actionPerformed(ActionEvent e) {
				god.generateNextGeneration();
			}
		}));

	}

	private Timer timer = new Timer(500, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			god.generateNextGeneration();
		}
	});

	/**
	 * This starts the game
	 */
	public void beginGame() {
		timer.start();
	}

	/**
	 * end the game, clears the screen
	 */
	public void endGame() {
		timer.stop();
		god.killAllCells();
	}

}
