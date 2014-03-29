package de.bno.mindrobot.feld;

public class ZielFeld extends Feld {

	private static int numbers = 0;

	int number;

	public ZielFeld(int number) {
		super(FeldTyp.ZIEL);
		this.number = number;
		ZielFeld.numbers++;
	}

	public ZielFeld() {
		this(numbers);
	}

	public int getNumber() {
		return number;
	}
}
