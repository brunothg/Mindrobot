package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.String;
import static de.bno.mindrobot.gui.Strings.TITLE;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import de.bno.mindrobot.MindRobot;

public class RobotFrame extends JFrame implements WindowListener,
		SignalListener {

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
		LOG.info("Versuche in den Vollbildmodus zu wechseln.");
		GraphicsEnvironment graphEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice graphDevice = graphEnvironment.getDefaultScreenDevice();

		if (!graphDevice.isFullScreenSupported()) {
			GraphicsDevice[] screenDevices = graphEnvironment
					.getScreenDevices();

			boolean failed = true;

			for (GraphicsDevice device : screenDevices) {
				if (device.isFullScreenSupported()) {
					graphDevice = device;
					failed = false;
				}
			}

			if (failed) {
				LOG.info("Vollbildmodus nicht verfügbar. Gehe zurück zum normalen Modus.");
				setSize();
				return;
			}
		}

		setUndecorated(true);
		graphDevice.setFullScreenWindow(this);

		validate();
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

	// SIGNAL LISTENER
	@Override
	public boolean Signal(String signal, Object... values) {

		return false;
	}

	// WINDOW LISTENER
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
