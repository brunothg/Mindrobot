package de.bno.mindrobot.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;

public class Strings {

	private static final Logger LOG = MindRobot.getLogger(Strings.class);
	private static final Charset CHARSET = StandardCharsets.UTF_8;

	public static final String TITLE = "title";
	public static final String ONE_INSTANZ_ONLY_TITLE = "one_instanz_only_title";
	public static final String ONE_INSTANZ_ONLY_MESSAGE = "one_instanz_only_message";
	public static final String START_APP = "start_app";
	public static final String CANCEL = "cancel";
	public static final String LANGUAGE = "lang";
	public static final String DEFAULT = "dft";
	public static final String TOOLTIP_SELECT_LANG = "tp_sel_lng";
	public static final String DSIPLAY_SET = "disp_set";
	public static final String WINDOWED = "windowed";
	public static final String FULLSCREEN = "fullscreen";
	public static final String EXIT = "exit";
	public static final String START = "start";
	public static final String PLAY = "play";
	public static final String STOP = "stop";
	public static final String EDIT = "edit";
	public static final String EXIT_QUESTION = "exit_question";
	public static final String MENU = "menu";
	public static final String ROBOT_WALL = "robot_wall_speech";
	public static final String ROBOT_OUT_OF_MAP = "robot_out_of_map_speech";
	public static final String ROBOT_ZIEL_X = "robot_goal_x_speech";
	public static final String ROBOT_ZIEL_SUC = "robot_goal_last_speech";

	public static final String SYNTAX_ENDE = "syntax_end";
	public static final String SYNTAX_WENN = "syntax_if";
	public static final String SYNTAX_DANN = "syntax_then";
	public static final String SYNTAX_SONST = "syntax_else";
	public static final String SYNTAX_WIEDERHOLE = "syntax_repeat";
	public static final String SYNTAX_SOLANGE = "syntax_while";

	private static Map<String, String> values;

	static {
		values = new HashMap<String, String>();
		loadDefault();
	}

	public static void loadDefault() {
		values.put(TITLE, "MindRobot");
		values.put(ONE_INSTANZ_ONLY_TITLE, "Programm wurde bereits gestartet");
		values.put(ONE_INSTANZ_ONLY_MESSAGE,
				"Es läuft bereits eine Instanz des Programms.");
		values.put(START_APP, "Start");
		values.put(CANCEL, "Abbrechen");
		values.put(LANGUAGE, "Sprache");
		values.put(DEFAULT, "Standart");
		values.put(TOOLTIP_SELECT_LANG,
				"Weitere Sprachen müssen im Ordner '%s' abgelegt werden.");
		values.put(DSIPLAY_SET, "Grafikeinstellung");
		values.put(FULLSCREEN, "Vollbild");
		values.put(WINDOWED, "Fenster");
		values.put(EXIT, "Verlassen");
		values.put(START, "Starten");
		values.put(STOP, "Stop");
		values.put(PLAY, "Start");
		values.put(EDIT, "Konsole");
		values.put(EXIT_QUESTION, "Wollen Sie das Spiel wirklich verlassen?");
		values.put(MENU, "Menü");
		values.put(SYNTAX_ENDE, "Ende");
		values.put(SYNTAX_WENN, "Wenn");
		values.put(SYNTAX_DANN, "Dann");
		values.put(SYNTAX_SONST, "Sonst");
		values.put(SYNTAX_WIEDERHOLE, "Wiederhole");
		values.put(SYNTAX_SOLANGE, "Solange");
		values.put(ROBOT_WALL, "Aua! Dort kann ich nicht lang!");
		values.put(ROBOT_ZIEL_X, "Yeah! Das %d. Ziel erreicht.");
		values.put(ROBOT_OUT_OF_MAP, "Hey! Hier geblieben.");
		values.put(ROBOT_ZIEL_SUC, "Puh! Geschafft.");
	}

	public static String String(String key) {

		String ret = values.get(key);

		return (ret != null) ? ret : "N/A";
	}

	public static void writeStrings(Path loc) throws IOException {
		BufferedWriter writer = Files.newBufferedWriter(loc, CHARSET);

		Set<Entry<String, String>> entrySet = values.entrySet();

		for (Entry<String, String> entry : entrySet) {
			writer.append(entry.getKey());
			writer.append('=');
			writer.append(entry.getValue());
			writer.newLine();
		}
		writer.append("END");

		writer.flush();
		writer.close();
	}

	public static void loadStrings(Path loc) throws IOException {
		BufferedReader reader = null;
		try {
			reader = Files.newBufferedReader(loc, CHARSET);

			String tmp;

			while ((tmp = reader.readLine()) != null) {
				tmp = tmp.trim();
				if (tmp.equalsIgnoreCase("END")) {
					break;
				} else if (!tmp.isEmpty() && tmp.indexOf('=') != -1) {

					String key = tmp.substring(0, tmp.indexOf('='));
					String value = (tmp.length() - 1 >= tmp.indexOf('=' + 1)) ? tmp
							.substring(tmp.indexOf('=') + 1, tmp.length())
							: null;

					key = key.trim();
					value = value.trim();

					if (key != null && value != null && !key.isEmpty()
							&& !value.isEmpty()) {
						values.put(key, value);
					}
				}
			}

		} catch (IOException e) {
			LOG.warning("Fehler beim Laden von Übersetzungen. Path: "
					+ loc.toAbsolutePath());
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
