package de.bno.mindrobot.feld;

public enum FeldTyp {
	NORMAL('#'), BLOCKED('X'), CONFUSE('?');

	char c;

	FeldTyp(char c) {
		this.c = c;
	}

	public char getCharRepresentation() {
		return c;
	}

}
