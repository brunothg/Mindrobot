package de.bno.mindrobot.gui.parser;

import static de.bno.mindrobot.gui.Strings.*;
import de.bno.mindrobot.gui.RobotControl;

public class MindTalk implements Parser {

	boolean running;

	@Override
	public void run(RobotControl ctrl, String script) {
		running = true;

		String[] words = script.split("\\s+");

		for (int i = 0; running && i < words.length; i++) {

			System.out.println(words[i] + "->" + getCommandTyp(words, i));
			switch (getCommandTyp(words, i)) {
			case Befehl:
				makeCMD(words[i], ctrl);
				break;
			case Verzweigung:
				i = followCase(words, i, ctrl);
				break;
			case Schleife:
				i = repeat(words, i, ctrl);
				break;
			default:
				System.out.println("Undefinierte Situation");
				running = false;
				return;
			}

		}

		running = false;
	}

	private int repeat(String[] s, int i, RobotControl ctrl) {

		switch (getSchleifenTyp(s, i)) {
		case Solange:
			return repeatWhile(s, i, ctrl);
		case Wiederholung:
			return repeatXTimes(s, i, ctrl);
		default:
			return i;
		}

	}

	private int repeatWhile(String[] s, int i, RobotControl ctrl) {

		int index = 0;
		System.out.println(askQU(s[i + 1], ctrl));
		while (askQU(s[i + 1], ctrl)) {
			index = 0;
			System.out.println("Repeat2->" + index);
			while (i + 2 + index < s.length
					&& !s[i + 2 + index].equals(String(SYNTAX_ENDE))) {
				makeCMD(s[i + 2 + index], ctrl);
				index++;
			}
		}

		return i + 2 + index;
	}

	private int repeatXTimes(String[] s, int i, RobotControl ctrl) {
		long times = Long.valueOf(s[i + 1]).longValue();

		int index = 0;
		for (; times > 0; times--) {
			index = 0;
			System.out.println("Repeat1->" + index);
			while (i + 2 + index < s.length
					&& !s[i + 2 + index].equals(String(SYNTAX_ENDE))) {
				makeCMD(s[i + 2 + index], ctrl);
				index++;
			}
		}

		return i + 2 + index;
	}

	private int followCase(String[] s, int i, RobotControl ctrl) {
		boolean _case = askQU(s[i + 1], ctrl);

		System.out.println("Case->" + _case);
		if (!s[i + 2].equals(String(SYNTAX_DANN))) {
			return i + 1;
		}

		int index = 3;
		boolean invert = false;
		while (i + index < s.length) {
			if (s[i + index].equals(String(SYNTAX_SONST))) {
				invert = true;
			} else if (s[i + index].equals(String(SYNTAX_ENDE))) {
				break;
			} else {

				if (_case && !invert) {
					makeCMD(s[i + index], ctrl);
				}

			}

			index++;
		}

		return i + index;
	}

	private void makeCMD(String cmd, RobotControl ctrl) {
		if (cmd.endsWith(".")) {
			String cc = cmd.substring(0, cmd.length() - 1);

			if (cc.equals(String(CMD_LINKS))) {
				System.out.println("makeCMD L");
				ctrl.turnLeft();
			} else if (cc.equals(String(CMD_RECHTS))) {
				System.out.println("makeCMD R");
				ctrl.turnRight();
			} else if (cc.equals(String(CMD_RUECKWAERTS))) {
				System.out.println("makeCMD<-");
				ctrl.moveBackwards();
			} else if (cc.equals(String(CMD_VORWAERTS))) {
				System.out.println("makeCMD->");
				ctrl.moveForwards();
			}

		}
	}

	private boolean askQU(String cmd, RobotControl ctrl) {

		if (cmd.endsWith("?")) {
			boolean invert = false;

			String cc = cmd.substring(0, cmd.length() - 1);
			if (cc.startsWith("!")) {
				cc = cc.substring(1);
				invert = true;
			}

			if (cc.equals(String(QU_HINDERNIS))) {
				return (invert) ? !ctrl.isBlockedFieldInFront() : ctrl
						.isBlockedFieldInFront();
			} else if (cc.equals(String(QU_VERWIRRT))) {
				return (invert) ? !ctrl.standOnConfusingField() : ctrl
						.isConfused();
			}

		}

		return false;
	}

	private Schleife getSchleifenTyp(String[] words, int i) {

		if (words[i].equals(String(SYNTAX_SOLANGE))) {
			return Schleife.Solange;
		}

		if (words[i].equals(String(SYNTAX_WIEDERHOLE))) {
			return Schleife.Wiederholung;
		}

		return null;
	}

	private CommandTyp getCommandTyp(String[] words, int i) {

		if (words[i].matches("!?[a-zA-Z]+[0-9a-zA-ZäüöÄÜÖß]*[\\.\\?]")) {
			return CommandTyp.Befehl;
		}

		if (i + 1 < words.length && words[i].equals(String(SYNTAX_WENN))) {
			return CommandTyp.Verzweigung;
		}

		if (i + 1 < words.length && words[i].equals(String(SYNTAX_SOLANGE))
				|| words[i].equals(String(SYNTAX_WIEDERHOLE))) {
			return CommandTyp.Schleife;
		}

		return null;
	}

	@Override
	public void stop() {
		running = false;
	}

	private enum CommandTyp {
		Befehl, Verzweigung, Schleife;
	}

	private enum Schleife {
		Wiederholung, Solange;
	}

}
