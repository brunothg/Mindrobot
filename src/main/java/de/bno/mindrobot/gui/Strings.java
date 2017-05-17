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

public class Strings
{

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
	public static final String LOGGING = "logging";
	public static final String EXPORT = "export";
	public static final String JAVA = "java";
	public static final String BEENDET = "finished";
	public static final String NUMBER = "number";

	public static final String ROBOT_WALL = "robot_wall_speech";
	public static final String ROBOT_OUT_OF_MAP = "robot_out_of_map_speech";
	public static final String ROBOT_ZIEL_X = "robot_goal_x_speech";
	public static final String ROBOT_ZIEL_SUC = "robot_goal_last_speech";

	public static final String MENU = "menu";
	public static final String MENU_BEFEHL = "menu_command";
	public static final String MENU_SCHLEIFE = "menu_loop";
	public static final String MENU_VERZWEIGUNG = "menu_branch";
	public static final String MENU_RECHTSDREHEN = "menu_turn_right";
	public static final String MENU_LINKSDREHEN = "menu_turn_left";
	public static final String MENU_VORWAERTS = "menu_move_forwards";
	public static final String MENU_RUECKWAERTS = "menu_move_backwards";
	public static final String MENU_WIEDERHOLUNG = "menu_repeat_x";
	public static final String MENU_SOLANGEWIE = "menu_while_do";
	public static final String MENU_FRAGE = "menu_question";
	public static final String MENU_FRAGE_VERWIRRT = "menu_question_confused";
	public static final String MENU_FRAGE_HINDERNIS = "menu_question_obstacle";
	public static final String MENU_GESCHWINDIGKEIT = "menu_set_speed";

	public static final String SYNTAX_ENDE = "syntax_end";
	public static final String SYNTAX_WENN = "syntax_if";
	public static final String SYNTAX_DANN = "syntax_then";
	public static final String SYNTAX_SONST = "syntax_else";
	public static final String SYNTAX_WIEDERHOLE = "syntax_repeat";
	public static final String SYNTAX_SOLANGE = "syntax_while";

	public static final String CMD_VORWAERTS = "cmd_forwards";
	public static final String CMD_RUECKWAERTS = "cmd_backwards";
	public static final String CMD_LINKS = "cmd_left";
	public static final String CMD_RECHTS = "cmd_right";
	public static final String CMD_SPEED = "cmd_speed";

	public static final String QU_VERWIRRT = "qu_confused";
	public static final String QU_HINDERNIS = "qu_blocked";

	public static final String MSG_POINTS = "msg_points";
	public static final String MSG_RESULT = "msg_result";
	public static final String MSG_FAILED = "msg_failed";

	private static Map<String, String> values;

	static
	{
		values = new HashMap<String, String>();
		loadDefault();
	}

	public static void loadDefault()
	{
		values.put(TITLE, "MindRobot");
		values.put(ONE_INSTANZ_ONLY_TITLE, "Programm wurde bereits gestartet");
		values.put(ONE_INSTANZ_ONLY_MESSAGE, "Es läuft bereits eine Instanz des Programms.");
		values.put(START_APP, "Start");
		values.put(CANCEL, "Abbrechen");
		values.put(LANGUAGE, "Sprache");
		values.put(DEFAULT, "Standard");
		values.put(TOOLTIP_SELECT_LANG, "Weitere Sprachen müssen im Ordner '%s' abgelegt werden.");
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
		values.put(CMD_LINKS, "linksDrehen");
		values.put(CMD_RECHTS, "rechtsDrehen");
		values.put(CMD_RUECKWAERTS, "rückwärts");
		values.put(CMD_VORWAERTS, "vorwärts");
		values.put(CMD_SPEED, "setzeGeschwindigkeit");
		values.put(QU_HINDERNIS, "steheVorHindernis");
		values.put(QU_VERWIRRT, "binVerwirrt");
		values.put(MSG_POINTS, "Dein Ergebnis: %.2f");
		values.put(MSG_RESULT, "Ergebnis");
		values.put(MSG_FAILED, "Schade, du hast das Ziel nicht erreicht. \nVersuche es doch noch einmal.");
		values.put(MENU_BEFEHL, "Befehl");
		values.put(MENU_SCHLEIFE, "Schleife");
		values.put(MENU_VERZWEIGUNG, "Verzweigung");
		values.put(MENU_RECHTSDREHEN, "90° im Uhrzeigersinn drehen");
		values.put(MENU_LINKSDREHEN, "90° gegen den Uhrzeigersinn drehen");
		values.put(MENU_GESCHWINDIGKEIT, "Ändere Geschwindigkeit");
		values.put(MENU_VORWAERTS, "Einen Schritt vorwärts");
		values.put(MENU_RUECKWAERTS, "Einen Schritt rückwärts");
		values.put(MENU_WIEDERHOLUNG, "Wiederhole x-mal");
		values.put(MENU_SOLANGEWIE, "Wiederhole solange wie");
		values.put(MENU_FRAGE, "Frage");
		values.put(MENU_FRAGE_VERWIRRT, "Bin ich verwirrt?");
		values.put(MENU_FRAGE_HINDERNIS, "Stehe ich vor einem Hindernis?");
		values.put(LOGGING, "Logging");
		values.put(EXPORT, "Exportieren");
		values.put(JAVA, "Java");
		values.put(BEENDET, "beendet");
		values.put(NUMBER, "Zahl");
	}

	public static String String(String key)
	{

		String ret = values.get(key);

		return (ret != null) ? ret : "N/A";
	}

	public static void writeStrings(Path loc) throws IOException
	{
		BufferedWriter writer = Files.newBufferedWriter(loc, CHARSET);

		Set<Entry<String, String>> entrySet = values.entrySet();

		for (Entry<String, String> entry : entrySet)
		{
			writer.append(entry.getKey());
			writer.append('=');
			writer.append(entry.getValue());
			writer.newLine();
		}
		writer.append("END");

		writer.flush();
		writer.close();
	}

	public static void loadStrings(Path loc) throws IOException
	{
		BufferedReader reader = null;
		try
		{
			reader = Files.newBufferedReader(loc, CHARSET);

			String tmp;

			while ((tmp = reader.readLine()) != null)
			{
				tmp = tmp.trim();
				if (tmp.equalsIgnoreCase("END"))
				{
					break;
				}
				else if (!tmp.isEmpty() && tmp.indexOf('=') != -1)
				{

					String key = tmp.substring(0, tmp.indexOf('='));
					String value = (tmp.length() - 1 >= tmp.indexOf('=' + 1))
						? tmp.substring(tmp.indexOf('=') + 1, tmp.length()) : null;

					key = key.trim();

					if (value != null)
					{
						value = value.trim();
					}

					if (key != null && value != null && !key.isEmpty() && !value.isEmpty())
					{
						values.put(key, value);
					}
				}
			}

		}
		catch (IOException e)
		{
			LOG.warning("Fehler beim Laden von Übersetzungen. Path: " + loc.toAbsolutePath());
			throw e;
		}
		finally
		{
			if (reader != null)
			{
				reader.close();
			}
		}
	}
}
