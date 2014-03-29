package de.bno.mindrobot.feld;

public class Feld {

	private FeldTyp feldtyp;

	public Feld(FeldTyp feldtyp) {
		this.feldtyp = feldtyp;
	}

	public Feld() {
		this.feldtyp = FeldTyp.NORMAL;
	}

	public FeldTyp getTyp() {
		return feldtyp;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Feld) {
			return ((Feld) obj).getTyp().equals(feldtyp);
		}

		return false;
	}

	public char toChar() {
		return feldtyp.getCharRepresentation();
	}
}
