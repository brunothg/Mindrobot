package de.bno.mindrobot.feld;

public class ZielFeld extends Feld implements Comparable<ZielFeld> {

	private static int numbers = 0;

	int number;

	public ZielFeld(int number) {
		super(FeldTyp.ZIEL);
		this.number = number;
		ZielFeld.numbers = number + 1;
	}

	public ZielFeld() {
		this(numbers);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ZielFeld) {
			return getNumber() == ((ZielFeld) obj).getNumber();
		}

		return false;
	}

	@Override
	public int compareTo(ZielFeld arg0) {

		if (arg0.getNumber() < getNumber()) {
			return -1;
		} else if (arg0.getNumber() > getNumber()) {
			return 1;
		} else {
			return 0;
		}

	}
}
