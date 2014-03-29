package de.bno.mindrobot;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import de.bno.mindrobot.gui.RobotFrame;

public class MindRobot {

	private static Logger LOG;

	public static Handler handler;
	public static RobotFrame display;

	public static void main(String[] args) {

		setToSystemLookAndFeel();
		try {
			setupLogging();
		} catch (Exception e) {
		}
		LOG = getLogger(MindRobot.class);

		LOG.info("Starte Applikation");
		showGUI();

	}

	private static void showGUI() {
		display = new RobotFrame();
		display.setSize(800, 600);
		display.setVisible(true);
	}

	private static void setupLogging() throws SecurityException, IOException {
		handler = new FileHandler("log.txt");
		handler.setEncoding("UTF-8");
		handler.setLevel(Level.CONFIG);
	}

	public static void setToSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
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
