package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.*;

import javax.swing.JDialog;

public class StartDialog extends JDialog {

	private static final long serialVersionUID = -8806641182590281978L;

	private StartDialog() {
		super();
		setTitle(String(TITLE));
		setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		setLocationByPlatform(true);

	}

	public static void start() {

		StartDialog start = new StartDialog();
		start.setVisible(true);

	}

}
