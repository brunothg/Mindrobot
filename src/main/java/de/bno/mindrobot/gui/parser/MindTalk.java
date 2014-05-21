package de.bno.mindrobot.gui.parser;

import static de.bno.mindrobot.gui.Strings.CMD_LINKS;
import static de.bno.mindrobot.gui.Strings.CMD_RECHTS;
import static de.bno.mindrobot.gui.Strings.CMD_RUECKWAERTS;
import static de.bno.mindrobot.gui.Strings.CMD_SPEED;
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
import de.bno.mindrobot.gui.Signals;

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

		Signals.sendSignal(Signals.SIGNAL_KONSOLE_LOG,
				String.format("Block -> %s%n%n", Arrays.toString(words)));
		LOG.info("Block -> " + Arrays.toString(words));

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
			case Ignorieren:
				break;
			default:
				LOG.warning("Unknown Command -> " + words[i]);
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

	/**
	 * Trennt einen <b>block</b> an der Stelle des <b>divider</b>. Sollte ein
	 * <b>starter</b> auftauchen muss ein weiterer <b>divider</b> auftauchen.
	 */
	public static String[][] splitBlock(String[] block, String divider,
			String starter) {

		List<String> first = new LinkedList<String>();
		List<String> second = new LinkedList<String>();

		int counter = -1;

		boolean foundDivider = false;
		for (int i = 0; i < block.length; i++) {
			String now = block[i];

			if (now.equals(divider)) {
				if (counter == 0) {
					foundDivider = true;
				} else {
					counter--;
				}
			} else if (now.equals(starter)) {
				counter++;
			}

			if (foundDivider) {
				second.add(now);
			} else {
				first.add(now);
			}
		}

		return new String[][] { first.toArray(new String[0]),
				second.toArray(new String[0]) };
	}

	public static String[] subBlock(String[] ar, int start, int stop) {
		String[] ret = new String[stop - start];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = ar[start + i];
		}

		return ret;
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

		String[] block = getBlock(s, i + 1);
		int index = block.length;

		while (running && askQU(s[i + 1], ctrl)) {
			runBlock(block, ctrl);
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

		int index = 0;
		String[] block = getBlock(s, i + 1);
		index = block.length;

		if (_case) {
			block = splitBlock(block, String(SYNTAX_SONST), String(SYNTAX_DANN))[0];
		} else {
			block = splitBlock(block, String(SYNTAX_SONST), String(SYNTAX_DANN))[1];
		}

		if (block != null
				&& block.length > 0
				&& (block[0].equals(String(SYNTAX_DANN)) || block[0]
						.equals(String(SYNTAX_SONST)))) {

			block = subBlock(block, 1, block.length);

			runBlock(block, ctrl);
		}

		return i + 2 + index;
	}

	private void makeCMD(String cmd, RobotControl ctrl) {
		Signals.sendSignal(Signals.SIGNAL_KONSOLE_LOG,
				String.format("CMD -> %s%n%n", cmd));
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
			} else if (cc.startsWith(String(CMD_SPEED))) {
				setSpeedCmd(cc);
			}

		}
	}

	private void setSpeedCmd(String cc) {
		if (cc == null) {
			return;
		}

		int startArgIndex = cc.indexOf('(') + 1;
		int stopArgIndex = cc.indexOf(')');

		String arg = cc.substring(startArgIndex, stopArgIndex);
		Integer arg_i = Integer.valueOf(arg);

		Signals.sendSignal(Signals.SIGNAL_SET_WAIT, arg_i);
	}

	private boolean askQU(String cmd, RobotControl ctrl) {
		if (cmd == null || ctrl == null) {
			return false;
		}

		boolean ret = false;

		if (cmd.endsWith("?")) {
			boolean invert = false;

			String cc = cmd.substring(0, cmd.length() - 1);
			LOG.info("?->" + cc);

			if (cc.startsWith("!")) {
				cc = cc.substring(1);
				invert = true;
			}

			if (cc.equals(String(QU_HINDERNIS))) {
				ret = (!invert) ? !ctrl.isFieldInFrontAccessible() : ctrl
						.isFieldInFrontAccessible();
			} else if (cc.equals(String(QU_VERWIRRT))) {
				ret = (invert) ? !ctrl.isConfused() : ctrl.isConfused();
			}

		}

		Signals.sendSignal(Signals.SIGNAL_KONSOLE_LOG,
				String.format("? -> %s = %b%n%n", cmd, ret));

		return ret;
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

	public static Schleife getSchleifenTyp(String[] words, int i) {

		if (words[i].equals(String(SYNTAX_SOLANGE))) {
			return Schleife.Solange;
		}

		if (words[i].equals(String(SYNTAX_WIEDERHOLE))) {
			return Schleife.Wiederholung;
		}

		return null;
	}

	public static CommandTyp getCommandTyp(String[] words, int i) {

		if (words[i].matches("!?[a-zA-Z]+[0-9a-zA-ZäüöÄÜÖß()]*[\\.\\?]")) {
			return CommandTyp.Befehl;
		}

		if (i + 1 < words.length && words[i].equals(String(SYNTAX_WENN))) {
			return CommandTyp.Verzweigung;
		}

		if (i + 1 < words.length && words[i].equals(String(SYNTAX_SOLANGE))
				|| words[i].equals(String(SYNTAX_WIEDERHOLE))) {
			return CommandTyp.Schleife;
		}

		if (words[i].isEmpty()) {
			return CommandTyp.Ignorieren;
		}

		return CommandTyp.Unbekannt;
	}

	@Override
	public void stop() {
		running = false;
	}

	public enum CommandTyp {
		Befehl, Verzweigung, Schleife, Unbekannt, Ignorieren;
	}

	public enum Schleife {
		Wiederholung, Solange;
	}

}
