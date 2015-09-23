package de.bno.mindrobot.feld;

public enum FeldTyp {
	NORMAL('#'), BLOCKED('X'), CONFUSE('Q'), START('S'), ZIEL('Z'), UNDEFINIERT('$');

	char c;
	char[] alias;

	FeldTyp(char c, char... alias) {
		this.c = c;
		this.alias = alias;
	}

	public char getCharRepresentation() {
		return c;
	}

	public static FeldTyp getTyp(char c) {

		FeldTyp[] types = values();

		for (FeldTyp typ : types) {
			if (typ.c == c) {
				return typ;
			}
		}

		FeldTyp ret = UNDEFINIERT;
		ret.alias = new char[] { c };

		return ret;
	}

}
