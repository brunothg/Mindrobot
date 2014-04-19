package de.bno.mindrobot.gui.parser;

import static de.bno.mindrobot.gui.Strings.CMD_LINKS;
import static de.bno.mindrobot.gui.Strings.CMD_RECHTS;
import static de.bno.mindrobot.gui.Strings.CMD_RUECKWAERTS;
import static de.bno.mindrobot.gui.Strings.CMD_VORWAERTS;
import static de.bno.mindrobot.gui.Strings.QU_HINDERNIS;
import static de.bno.mindrobot.gui.Strings.QU_VERWIRRT;
import static de.bno.mindrobot.gui.Strings.SYNTAX_DANN;
import static de.bno.mindrobot.gui.Strings.SYNTAX_ENDE;
import static de.bno.mindrobot.gui.Strings.SYNTAX_SOLANGE;
import static de.bno.mindrobot.gui.Strings.SYNTAX_SONST;
import static de.bno.mindrobot.gui.Strings.SYNTAX_WENN;
import static de.bno.mindrobot.gui.Strings.SYNTAX_WIEDERHOLE;
import static de.bno.mindrobot.gui.Strings.String;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.gui.RobotControl;

public class MindTalk implements Parser {

	private static final Logger LOG = MindRobot.getLogger(MindTalk.class);

	volatile boolean running;

	@Override
	public void run(RobotControl ctrl, String script) {
		running = true;

		String[] words = script.split("\\s+");

		runBlock(words, ctrl);

		running = false;
	}

	private void runBlock(String[] words, RobotControl ctrl) {
		if (words == null) {
			return;
		}

		LOG.info("Blok->" + Arrays.toString(words));

		for (int i = 0; running && i < words.length; i++) {

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
				running = false;
				return;
			}

		}

	}

	private String[] getBlock(String[] words, int i) {

		List<String> ret = new LinkedList<String>();

		int end = 1;

		int index = 0;
		while (running && i + 1 + index < words.length && end > 0) {

			String tmp = words[i + 1 + index];

			if (tmp.equals(String(SYNTAX_ENDE))) {
				end--;
			} else if (isStartBlock(tmp)) {
				end++;
			}

			if (end > 0) {
				ret.add(tmp);
			}
			index++;
		}

		if (!running) {
			return null;
		}
		return ret.toArray(new String[0]);
	}

	private boolean isStartBlock(String tmp) {

		String[] starter = new String[] { String(SYNTAX_WENN),
				String(SYNTAX_WIEDERHOLE), String(SYNTAX_SOLANGE) };

		for (String s : starter) {
			if (tmp.equals(s)) {
				return true;
			}
		}

		return false;
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
		while (running && askQU(s[i + 1], ctrl)) {
			index = 0;
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

		String[] block = getBlock(s, i + 1);
		int index = block.length;

		for (; running && times > 0; times--) {

			runBlock(block, ctrl);
		}

		return i + 2 + index;
	}

	private int followCase(String[] s, int i, RobotControl ctrl) {
		boolean _case = askQU(s[i + 1], ctrl);

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
				} else if (!_case && invert) {
					makeCMD(s[i + index], ctrl);
				}

			}

			index++;
		}

		return i + index;
	}

	private void makeCMD(String cmd, RobotControl ctrl) {
		if (cmd == null || ctrl == null) {
			return;
		}

		if (cmd.endsWith(".")) {
			String cc = cmd.substring(0, cmd.length() - 1);
			LOG.info("CMD->" + cc);

			if (cc.equals(String(CMD_LINKS))) {
				ctrl.turnLeft();
			} else if (cc.equals(String(CMD_RECHTS))) {
				ctrl.turnRight();
			} else if (cc.equals(String(CMD_RUECKWAERTS))) {
				ctrl.moveBackwards();
			} else if (cc.equals(String(CMD_VORWAERTS))) {
				ctrl.moveForwards();
			}

		}
	}

	private boolean askQU(String cmd, RobotControl ctrl) {
		if (cmd == null || ctrl == null) {
			return false;
		}

		if (cmd.endsWith("?")) {
			boolean invert = false;

			String cc = cmd.substring(0, cmd.length() - 1);
			LOG.info("?->" + cc);

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
