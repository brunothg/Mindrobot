package de.bno.mindrobot.data.spielfeld;

import de.bno.mindrobot.feld.Feld;

public class SpielfeldData {

	private int width;
	private int height;

	Feld[][] felder;

	public SpielfeldData(int width, int height) {
		this.width = width;
		this.height = height;
		felder = new Feld[this.height][this.width];
		iniFelder();
	}

	public SpielfeldData() {
		this(5, 7);
	}

	private void iniFelder() {
		for (int y = 0; y < felder.length; y++) {
			for (int x = 0; x < felder[y].length; x++) {
				felder[y][x] = new Feld();
			}
		}
	}

}
