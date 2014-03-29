package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.String;
import static de.bno.mindrobot.gui.Strings.TITLE;

import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import de.bno.mindrobot.MindRobot;

public class RobotFrame extends JFrame {

	private static final Logger LOG = MindRobot.getLogger(RobotFrame.class);
	private static final long serialVersionUID = -2366663477203061018L;

	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;

	public RobotFrame() {
		super();
		setTitle(String(TITLE));
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		setIcon();

	}

	private void setIcon() {
		try {
			setIconImage(loadIcon("MindRobot.png").getImage());
		} catch (Exception e) {
			LOG.warning("Beim Startdialog konnte das Icon nicht erzeugt werden. Cause: "
					+ e.getMessage());
		}
	}

	private ImageIcon loadIcon(String s) {
		return new ImageIcon(getClass().getClassLoader().getResource(
				"de/bno/mindrobot/gui/" + s));
	}

}
