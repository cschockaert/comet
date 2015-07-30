package com.cschockaert.kcomet.golui;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class World extends JPanel {

	public static final int WORLD_Y = 50;

	public static final int WORLD_X = 40;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157970275395308036L;

	private Cell[][] cellGrid;
	private List<Cell> cellList = new ArrayList<Cell>();
	
	public World() {
		super();
		setLayout(new GridLayout(WORLD_X,WORLD_Y));	
		setBackground(Color.WHITE);

		cellGrid = new Cell[WORLD_X][WORLD_Y];
		int row;
		int col;

		for (row = 0; row < cellGrid.length; row++) {
			for (col = 0; col < cellGrid[row].length; col++) {
				Cell cell = new Cell(row, col);
				cellGrid[row][col] = cell; // create cell
				this.add(cell);
				cell.addMouseListener(handler);
				cell.addMouseMotionListener(handler);
				cellList.add(cell);
			}
		}
		//ajout des voisins
		for (row = 0; row < cellGrid.length; row++) {
			for (col = 0; col < cellGrid[row].length; col++) {
				for (Cell cell : cellList) {
					cellGrid[row][col].addNeightBorIfNeeded(cell);
				}
			}
		}

	}
	
	public List<Cell> getAllCells() {
		return cellList;
	}

	private MouseHandler handler = new MouseHandler(); // initializes handlers

	private boolean candrag;
	
	
	/**
	 * Private mouse handler class that uses the mouse adapter
	 * 
	 * @author anuccio
	 *
	 */
	private class MouseHandler extends MouseAdapter {

		

		/**
		 * if mouse is pressed
		 */
		public void mousePressed(MouseEvent event) {
			candrag = true; // is true when the user is holding down a click

			Cell cellPanel = (Cell) event.getComponent();
			cellPanel.live();
		}

		/**
		 * if mouse is released
		 */
		public void mouseReleased(MouseEvent event) {
			candrag = false; // makes it false when click is released
		}

		/**
		 * if mouse is clicked
		 */
		public void mouseClicked(MouseEvent event) {
			Cell cellPanel = (Cell) event.getComponent();
			cellPanel.live();
		}

		/**
		 * if mouse enters the panel
		 */
		public void mouseEntered(MouseEvent event) {
			if (candrag) {
				Cell cellPanel = (Cell) event.getComponent();
				cellPanel.live();
			}

		}

	}


}
