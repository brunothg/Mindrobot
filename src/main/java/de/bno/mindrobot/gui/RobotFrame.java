package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.String;
import static de.bno.mindrobot.gui.Strings.TITLE;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.importer.FileSpielfeldImporter;
import de.bno.mindrobot.data.importer.SpielfeldImporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public class RobotFrame extends JFrame implements WindowListener,
		SignalListener {

	private static final Logger LOG = MindRobot.getLogger(RobotFrame.class);
	private static final long serialVersionUID = -2366663477203061018L;

	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;
	private JPanel mainPanel;

	private boolean isFullscreen;

	public RobotFrame(boolean fullscreen) {
		super();
		setTitle(String(TITLE));
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));

		setIcon();

		if (!fullscreen) {
			setSize();
			isFullscreen = false;
		} else {
			setFullscreen();
		}

		addWindowListener(this);

		addMainPanel();

		setView(new MainMenu());

		Signals.addListener(this);
	}

	private void addMainPanel() {
		mainPanel = new Background();
		mainPanel.setLayout(new BorderLayout(0, 0));
		add(mainPanel, BorderLayout.CENTER);

	}

	private void setView(JComponent comp) {
		mainPanel.removeAll();
		mainPanel.add(comp, BorderLayout.CENTER);
		revalidate();
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

		isFullscreen = true;
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
		if (signal.equalsIgnoreCase("exit")) {
			return signalExit();
		} else if (signal.equalsIgnoreCase("start")) {
			return signalStart(values);
		}

		return false;
	}

	private boolean signalStart(Object[] values) {
		if (values.length > 0) {
			String mapPath = "./maps/" + values[0].toString();
			SpielfeldImporter spielfeldImporter = new FileSpielfeldImporter(
					Paths.get(mapPath));
			LOG.info("Load Map Data from: " + mapPath);

			try {
				SpielfeldData spielfeld = spielfeldImporter.importSpielfeld();
				setView(new Playground(spielfeld));
				revalidate();
			} catch (IOException e) {
				LOG.warning("Fehler beim Laden der Map: " + e.getMessage());
			}
			return true;
		}
		LOG.warning("Versuche Spiel ohne Map zu starten. Mache nichts.");
		return false;
	}

	private boolean signalExit() {
		storeSize();
		dispose();
		return true;
	}

	// WINDOW LISTENER
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		storeSize();
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

	private void storeSize() {
		if (!isFullscreen) {
			MindRobot.userPrefs.putInt("Width", getWidth());
			MindRobot.userPrefs.putInt("Height", getHeight());
		}
	}

}
