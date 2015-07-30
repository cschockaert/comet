package com.cschockaert.kcomet.golui;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.cschockaert.kcomet.bus.Bus;
import com.cschockaert.kcomet.bus.PublishEvent;
import com.cschockaert.kcomet.bus.ReceivedEvent;
import com.cschockaert.kcomet.bus.SimpleEvent;

import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
/**
 * Represente la vie
 *
 */
@Listener
public class Cell extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9150145006502273739L;

	private boolean alive = false;
	
	private boolean nextAliveState = false;
	
	private List<Cell> neightbor = new ArrayList<Cell>();

	private int row;
	private int col;
		
	public Cell(int row, int col) {
		Bus.BUS.subscribe(this);
		setBorder(new LineBorder(Color.BLACK, 1));
		this.row = row;
		this.col = col;
		setToolTipText("");
	}
	
	
	
	public String getChannel() {
		return "/gol/" + row + "/" + col;
	}
	
	public int getRow() {
		return row;
	}


	public int getCol() {
		return col;
	}


	public boolean isNeighbor(Cell cell) {
		return Math.abs(row - cell.getRow()) <= 1 && Math.abs(col - cell.getCol()) <= 1;
	}
		
	public void addNeightBorIfNeeded(Cell cell) {
		if (cell != this && isNeighbor(cell)) {
			this.neightbor.add(cell);
		}
	}
	
	@Handler
	public void stateChanged(ReceivedEvent event) {
		if (getChannel().equals(event.getChannel())) {
			alive = (boolean) event.getValues().get("alive");
			repaint();
		}
	}
	
	public void die() {
		if (alive) {
			alive = false;
			Bus.BUS.publish(new PublishEvent(Collections.singletonMap("alive", alive), getChannel()));
			repaint();
		}
	}

	public void live() {
		if (!alive) {
			alive = true;
			Bus.BUS.publish(new PublishEvent(Collections.singletonMap("alive", alive), getChannel()));
			repaint();
		}
	}

	public boolean isAlive() {
		return alive;
	}



	public int getNeighborAliveCount() {
		int ncount = 0;
		for (Cell cell : neightbor ) {
			if (cell.isAlive()) {
				ncount++;
			}
		}
		return ncount;
	}
	
	


	public boolean isNextAliveState() {
		return nextAliveState;
	}


	public void setNextAliveState(boolean nextAliveState) {
		this.nextAliveState = nextAliveState;
	}

	public void applyNextState() {
		if (nextAliveState != this.alive) {
		this.alive = nextAliveState;
		Bus.BUS.publish(new PublishEvent(Collections.singletonMap("alive", alive), getChannel()));
		this.repaint();
		}
	}

	@Override
	public Color getBackground() {
		if (isAlive()) {
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}
	
	@Override
	public String getToolTipText(MouseEvent event) {
		return String.valueOf(getNeighborAliveCount());
	}


}
