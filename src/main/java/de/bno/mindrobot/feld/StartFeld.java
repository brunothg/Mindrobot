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
		super(FeldTyp.START);

		if (direction >= 1 && direction <= 4) {
			this.direction = direction;
		} else {
			this.direction = EAST;
		}
	}

	public int getDirection() {
		return this.direction;
	}

	public void setDirection(int direction) {
		if (direction >= 1 && direction <= 4) {
			this.direction = direction;
		} else {
			this.direction = EAST;
		}
	}

	public static boolean isValidDirection(int direction) {
		if (direction == EAST || direction == WEST || direction == SOUTH
				|| direction == NORTH) {
			return true;
		}

		return false;
	}

}
