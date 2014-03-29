package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.String;
import static de.bno.mindrobot.gui.Strings.TITLE;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import de.bno.mindrobot.MindRobot;

public class RobotFrame extends JFrame implements WindowListener {

	private static final Logger LOG = MindRobot.getLogger(RobotFrame.class);
	private static final long serialVersionUID = -2366663477203061018L;

	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;

	public RobotFrame(boolean fullscreen) {
		super();
		setTitle(String(TITLE));
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setIcon();

		if (!fullscreen) {
			setSize();
		} else {
			setFullscreen();
		}

		addWindowListener(this);
	}

	private void setFullscreen() {
		// TODO Auto-generated method stub

	}

	private void setSize() {
		setSize(MindRobot.userPrefs.getInt("Width", DEFAULT_WIDTH),
				MindRobot.userPrefs.getInt("Height", DEFAULT_HEIGHT));
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

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		MindRobot.userPrefs.putInt("Width", getWidth());
		MindRobot.userPrefs.putInt("Height", getHeight());
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
