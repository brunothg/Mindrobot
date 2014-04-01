package de.bno.mindrobot;

import java.io.IOException;
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

	public static void main(String[] args) {

		setLookAndFeel();
		try {
			setupLogging();
		} catch (Exception e) {
		}
		LOG = getLogger(MindRobot.class);

		setupPreferences();

		LOG.info("Starte Applikation");
		showGUI();

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
		handler.setLevel(Level.CONFIG);
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

}
