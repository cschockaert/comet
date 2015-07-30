package com.cschockaert.kcomet.golui;

/**
 * repr�sente dieu
 * 
 */
public class God { 
	private World world;

	public God(World world) {
		this.world = world;
	}
	
	
	public void killAllCells() {
		for (Cell cell :world.getAllCells()) {
			cell.die();
		}
	}


	public void generateNextGeneration() {
		for (Cell cell :world.getAllCells()) {
			executeRule1(cell);
			executeRule2(cell);
			executeRule3(cell);
			executeRule4(cell);
		}
		for (Cell cell :world.getAllCells()) {
			cell.applyNextState();
		}
		
	}

	public void executeRule1(Cell cell) {
		if (cell.getNeighborAliveCount() < 2) {
			cell.setNextAliveState(false);
		}
	}
	
	public void executeRule2(Cell cell) {
		if (cell.isAlive() && (cell.getNeighborAliveCount() == 2 || cell.getNeighborAliveCount() ==3)) {
			cell.setNextAliveState(true);
		}
	}
	
	public void executeRule3(Cell cell) {
		if (cell.getNeighborAliveCount() > 3) {
			cell.setNextAliveState(false);
		}
	}
	
	public void executeRule4(Cell cell) {
		if (cell.getNeighborAliveCount() == 3) {
			cell.setNextAliveState(true);
		}
	}
}
