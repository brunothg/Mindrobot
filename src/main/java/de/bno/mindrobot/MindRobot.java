package de.bno.mindrobot;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import de.bno.mindrobot.gui.RobotFrame;
import de.bno.mindrobot.gui.StartDialog;

public class MindRobot {

	private static Logger LOG;

	public static Handler handler;
	public static RobotFrame display;

	public static final String MAP_SEARCH_STRING = "./maps";
	public static final String IMAGE_SEARCH_STRING = "Images";

	public static Preferences userPrefs;

	private static boolean logDetailed;

	private static boolean fullscreen;

	public static void main(String[] args) {

		if (args != null) {
			logDetailed = isDetailedLogEnabled(args);
		}

		setLookAndFeel();
		try {
			setupLogging();
		} catch (Exception e) {
		}
		LOG = getLogger(MindRobot.class);

		setupPreferences();

		Path myDir = null;
		try {
			myDir = Paths.get("./").toAbsolutePath();
		} catch (Exception e) {
		}
		LOG.info("Starte Applikation "
				+ ((myDir != null) ? myDir.toString() : "N/A"));
		showGUI();

	}

	private static boolean isDetailedLogEnabled(String[] args) {
		boolean ret = false;

		for (String s : args) {
			if (s.equalsIgnoreCase("logfine")) {
				ret = true;
				break;
			}
		}

		return ret;
	}

	private static void setupPreferences() {
		userPrefs = Preferences.userNodeForPackage(MindRobot.class);
	}

	private static void showGUI() {
		StartDialog.start();
	}

	private static void setupLogging() throws SecurityException, IOException {
		handler = new FileHandler("log.txt");
		handler.setEncoding("UTF-8");

		if (logDetailed) {
			handler.setLevel(Level.INFO);
		} else {
			handler.setLevel(Level.WARNING);
		}
	}

	public static void setLookAndFeel() {

		boolean failed = true;

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					failed = false;
					break;
				}
			}
		} catch (Exception e) {
			failed = true;
		}

		if (failed) {

			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
			}

		}
	}

	public static Logger getLogger(Class<?> klasse) {

		Logger log = Logger.getLogger(klasse.getName());

		if (handler != null) {
			log.addHandler(handler);
		} else {
			log.setLevel(Level.WARNING);
		}

		return log;

	}

	public static boolean isFullscreen() {
		return fullscreen;
	}

	public static void setFullscreen(boolean fullscreen) {
		MindRobot.fullscreen = fullscreen;
	}

}
