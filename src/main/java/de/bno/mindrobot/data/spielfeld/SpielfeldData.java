package de.bno.mindrobot.data.spielfeld;

import de.bno.mindrobot.feld.Feld;
import de.bno.mindrobot.feld.FeldTyp;
import de.bno.mindrobot.feld.StartFeld;
import de.bno.mindrobot.gui.Location;

public class SpielfeldData {

	private Feld[][] felder;

	public SpielfeldData(int width, int height) {
		felder = new Feld[height][width];
		iniFelder();
	}

	private void iniFelder() {
		for (int y = 0; y < felder.length; y++) {
			for (int x = 0; x < felder[y].length; x++) {
				felder[y][x] = new Feld();
			}
		}
	}

	public Feld getFeld(int posX, int posY) {
		return felder[posY][posX];
	}

	public void setFeld(int posX, int posY, Feld feld) {
		felder[posY][posX] = feld;
	}

	public int getWidth() {
		return felder[0].length;
	}

	public int getHeight() {
		return felder.length;
	}

	public Location getStartPoint() {
		Location ret = null;

		loop: for (int y = 0; y < felder.length; y++) {
			for (int x = 0; x < felder[y].length; x++) {
				Feld tmp = felder[y][x];
				if (tmp.getTyp() == FeldTyp.START) {
					ret = new Location(x, y);
					break loop;
				}
			}
		}

		if (ret == null) {
			ret = new Location(0, 0);
		}

		return ret;
	}

	public int getStartDirection() {
		int ret = 1;

		loop: for (int y = 0; y < felder.length; y++) {
			for (int x = 0; x < felder[y].length; x++) {
				Feld tmp = felder[y][x];
				if (tmp.getTyp() == FeldTyp.START && tmp instanceof StartFeld) {
					ret = ((StartFeld) tmp).getDirection();
					break loop;
				}
			}
		}

		return ret;
	}

	public boolean canMoveTo(int x, int y) {
		boolean ret = false;

		if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
			if (felder[y][x].getTyp() != FeldTyp.BLOCKED) {
				ret = true;
			}
		}

		return ret;
	}

}
