package de.bno.mindrobot.data.exporter;

import static de.bno.mindrobot.gui.Strings.BEENDET;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.gui.Signals;
import de.bno.mindrobot.gui.parser.MindTalk;

public class JavaExporter implements ScriptExporter {

	private static final Logger LOG = MindRobot.getLogger(JavaExporter.class);

	public void exportAs(String s, Path out) throws IOException {

		if (s == null || out == null) {
			return;
		}

		LOG.info("JavaExport -> " + out.toString());
		Signals.sendSignal(Signals.SIGNAL_KONSOLE_LOG,
				String.format("JavaExport -> %s%n", out.toString()));

		if (!Files.exists(out)) {
			Files.createFile(out);
		}

		String exportstring;
		String javastring = s;

		if (!s.isEmpty()) {
			javastring = parse(s);
		}

		exportstring = String.format(loadDefault("rahmen_java"), javastring);

		OutputStream outs = Files.newOutputStream(out);

		outs.write(exportstring.getBytes("UTF-8"));

		outs.close();

		Signals.sendSignal(Signals.SIGNAL_KONSOLE_LOG,
				String.format("JavaExport -> %s%n", String(BEENDET)));
	}

	private String loadDefault(String name) {

		String ret = "";

		InputStream in = getClass().getClassLoader().getResourceAsStream(
				"de/bno/mindrobot/data/exporter/" + name);

		byte[] buf = new byte[20];

		int num = -1;

		try {
			while ((num = in.read(buf)) != -1) {
				ret += new String(buf, 0, num, "UTF-8");
			}
		} catch (IOException e) {
			ret = "%s";
		}

		return ret;
	}

	private String parse(String source) {

		String ret = source;

		String[] words = source.split("\\s+");

		ret = parseBlock(words, "");

		return ret;
	}

	private String parseBlock(String[] words, String prefix) {
		String ret = "";
		if (words == null || words.length < 1) {
			return ret;
		}

		for (int i = 0; i < words.length; i++) {

			switch (MindTalk.getCommandTyp(words, i)) {
			case Befehl:
				ret += "\n" + prefix + parseCMD(words[i], "\t\t");
				break;
			case Verzweigung:
				JumpReturn<String> case_ret = followCase(words, i, "\t\t");
				i = case_ret.getJump();
				ret += "\n\n" + prefix + case_ret.getReturnValue() + "\n";
				break;
			case Schleife:
				JumpReturn<String> loop_ret = repeat(words, i, "\t\t");
				i = loop_ret.getJump();
				ret += "\n\n" + prefix + loop_ret.getReturnValue() + "\n";
				break;
			default:
				break;
			}

		}

		return ret;
	}

	private JumpReturn<String> repeat(String[] s, int i, String prefix) {
		String ret = "";
		int index = 0;

		switch (MindTalk.getSchleifenTyp(s, i)) {
		case Solange:
			JumpReturn<String> rep_ret = repeatWhile(s, i, prefix);
			index = rep_ret.getJump();
			ret = rep_ret.getReturnValue();
			break;
		case Wiederholung:
			JumpReturn<String> repx_ret = repeatXTimes(s, i, prefix);
			index = repx_ret.getJump();
			ret = repx_ret.getReturnValue();
			break;
		default:
			index = i;
			break;
		}

		return new JumpReturn<String>(index, ret);
	}

	private JumpReturn<String> repeatXTimes(String[] s, int i, String prefix) {
		String ret = "";

		long times = Long.valueOf(s[i + 1]).longValue();

		String[] block = getBlock(s, i + 1);
		int index = block.length;

		String prog = parseBlock(block, prefix + "\t");

		ret = String.format(
				"%sfor(long times = %d; times > 0; times--){%s%n%s", prefix,
				times, prog, prefix);

		return new JumpReturn<String>(i + 2 + index, ret);
	}

	private JumpReturn<String> repeatWhile(String[] s, int i, String prefix) {
		String ret = "";

		String[] block = getBlock(s, i + 1);
		int index = block.length;

		String question = askQU(s[i + 1]);
		String prog = parseBlock(block, prefix + "\t");

		ret = String.format("%swhile(%s){%s%n%s}", prefix, question, prog,
				prefix);

		// runBlock(block, ctrl);

		return new JumpReturn<String>(i + 2 + index, ret);
	}

	private JumpReturn<String> followCase(String[] s, int i, String prefix) {
		String ret = "";

		int index = 0;
		String[] block = getBlock(s, i + 1);
		index = block.length;

		// [WENN, DANN]
		String[][] wenn_dann = MindTalk.splitBlock(block, String(SYNTAX_SONST));

		String bedingung = askQU(s[i + 1]);
		String dann_block = "";

		if (wenn_dann[0].length > 0
				&& wenn_dann[0][0].equals(String(SYNTAX_DANN))) {
			block = MindTalk.subBlock(wenn_dann[0], 1, wenn_dann[0].length);
			dann_block = "\n" + parseBlock(block, prefix + "\t");
		} else {
			dann_block = "\n" + prefix + "//Error translating then block";
		}

		ret = String.format("%sif( %s ){%s%n%s}", prefix, bedingung,
				dann_block, prefix);

		String sonst_block = "";

		if (wenn_dann[1].length > 0
				&& wenn_dann[1][0].equals(String(SYNTAX_SONST))) {
			block = MindTalk.subBlock(wenn_dann[1], 1, wenn_dann[1].length);
			sonst_block = "\n" + prefix + parseBlock(block, "\t");
		} else if (wenn_dann[1].length > 0) {
			sonst_block = "\n" + prefix + "//Error translating else block";
		}

		if (sonst_block != null && !sonst_block.isEmpty()) {
			ret += String.format("else{%s%n%s}", sonst_block, prefix);
		}

		return new JumpReturn<String>(i + 2 + index, ret);
	}

	private String[] getBlock(String[] words, int i) {

		List<String> ret = new LinkedList<String>();

		int end = 1;

		int index = 0;
		while (i + 1 + index < words.length && end > 0) {

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

	private String askQU(String cmd) {
		String ret = "";

		if (cmd == null) {
			return ret;
		}

		if (cmd.endsWith("?")) {

			String cc = cmd.substring(0, cmd.length() - 1);

			if (cc.startsWith("!")) {
				cc = cc.substring(1);
				ret = "!";
			}

			if (cc.equals(String(QU_HINDERNIS))) {
				ret += "obstacleInFront()";
			} else if (cc.equals(String(QU_VERWIRRT))) {
				ret += "isConfused()";
			}

		}

		return ret;
	}

	private String parseCMD(String cmd, String prefix) {
		String ret = prefix;

		if (cmd == null) {
			return ret;
		}

		if (cmd.endsWith(".")) {
			String cc = cmd.substring(0, cmd.length() - 1);

			if (cc.equals(String(CMD_LINKS))) {
				ret += "turnLeft()";
			} else if (cc.equals(String(CMD_RECHTS))) {
				ret += "turnRight()";
			} else if (cc.equals(String(CMD_RUECKWAERTS))) {
				ret += "backwards()";
			} else if (cc.equals(String(CMD_VORWAERTS))) {
				ret += "forwards()";
			}

		}

		return ret + ";";
	}

}
