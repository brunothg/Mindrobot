package de.bno.mindrobot.feld;

import static de.bno.mindrobot.gui.Avatar.EAST;

public class StartFeld extends Feld {

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

}
