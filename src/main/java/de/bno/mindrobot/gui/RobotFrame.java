package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.String;
import static de.bno.mindrobot.gui.Strings.TITLE;

import javax.swing.JFrame;

public class RobotFrame extends JFrame {

	private static final long serialVersionUID = -2366663477203061018L;

	public RobotFrame() {
		super();
		setTitle(String(TITLE));
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

}
