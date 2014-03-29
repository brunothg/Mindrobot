package de.bno.mindrobot;

import javax.swing.UIManager;

public class MindRobot {

	public static void main(String[] args) {

		setToSystemLookAndFeel();

	}

	public static void setToSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
	}

}
