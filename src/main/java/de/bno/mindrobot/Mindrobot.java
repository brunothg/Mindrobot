package de.bno.mindrobot;

import javax.swing.UIManager;

public class Mindrobot {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}

	}

}
