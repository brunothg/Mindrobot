package de.bno.mindrobot.data.spielfeld;

import de.bno.mindrobot.feld.Feld;

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

}
