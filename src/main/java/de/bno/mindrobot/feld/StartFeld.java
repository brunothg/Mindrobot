package de.bno.mindrobot.feld;

public class StartFeld extends Feld {

	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public static final int NORTH = 4;

	int direction;

	public StartFeld() {
		this(EAST);
	}

	public StartFeld(int direction) {
		if (direction >= 1 && direction <= 4) {
			this.direction = direction;
		} else {
			this.direction = EAST;
		}
	}

	public int getDirection() {
		return this.direction;
	}

}
