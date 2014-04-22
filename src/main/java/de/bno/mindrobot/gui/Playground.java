package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.SwingWorker;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.importer.CustomFileSkinImporter;
import de.bno.mindrobot.data.importer.SkinImporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.Feld;
import de.bno.mindrobot.feld.FeldTyp;
import de.bno.mindrobot.feld.ZielFeld;

public class Playground extends JComponent implements RobotControl,
		SignalListener {

	private static final long serialVersionUID = -5702637217520674503L;
	private static final Logger LOG = MindRobot.getLogger(Playground.class);

	private static final Dimension MINIMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(50), Pixel.pointsToPixel(50));

	private static final Dimension NORMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(100), Pixel.pointsToPixel(100));

	private static final int WAIT = 1000;

	private SpielfeldData spielfeld;

	private SkinImporter skinImporter;

	private String map;

	private PlayController playController;
	private Konsole konsole;

	private Location posAvatar;
	private int directionAvatar;

	private int nextGoal;
	private int lastGoal;

	private String speech;

	private boolean isConfused;

	private final GraphicsConfiguration gfxConf = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();

	BufferedImage img;

	public Playground(SpielfeldData spielfeld, String map) {
		super();

		setSpielfeld(spielfeld, map);

		setLayout(null);

		createControl();
		createKonsole();

		Signals.addListener(this);
	}

	public void setSpielfeld(SpielfeldData spielfeld, String map) {
		this.spielfeld = spielfeld;
		this.map = map;

		if (spielfeld != null) {
			this.lastGoal = spielfeld.getLastGoal();
			this.posAvatar = this.spielfeld.getStartPoint();
			this.directionAvatar = this.spielfeld.getStartDirection();
		}

		if (map != null) {
			loadImages();
		}
	}

	private void createKonsole() {
		Konsole konsole = new Konsole();
		konsole.setVisible(false);
		konsole.setLocation(0, 0);
		konsole.setSize(getWidth(), getHeight());
		konsole.setTitleSize(playController.getHeight() + playController.getY());

		this.konsole = konsole;
		add(this.konsole);
	}

	private void createControl() {
		playController = new PlayController();
		playController.setSize(playController.getPreferredSize());
		playController.setLocation(5, 5);
		add(playController);
	}

	private void loadImages() {
		if (skinImporter == null) {
			skinImporter = new CustomFileSkinImporter(this.map);
		} else {
			skinImporter.setMap(this.map);
		}
	}

	@Override
	protected void paintChildren(Graphics g) {
		updateControllerSize();
		updateEditorSize();

		super.paintChildren(g);
	}

	private void updateControllerSize() {
		Dimension prefSize = playController.getPreferredSize();
		Dimension size = new Dimension(Math.min(prefSize.width, getWidth()),
				Math.min(prefSize.height, getHeight()));
		playController.setSize(size);
	}

	private void updateEditorSize() {
		konsole.setSize(getSize());
	}

	@Override
	protected void paintComponent(Graphics g) {

		if (konsole.isVisible()) {
			return;
		}

		if (img == null || img.getWidth() != getWidth()
				|| img.getHeight() != getHeight()) {
			img = gfxConf.createCompatibleImage(getWidth(), getHeight(),
					Transparency.TRANSLUCENT);
		}

		bigPaint(img.createGraphics(), img.getWidth(), img.getHeight());

		g.drawImage(img, 0, 0, this);
	}

	private void bigPaint(Graphics2D g, int width, int height) {
		g.setBackground(new Color(0, 0, 0, 0));
		g.clearRect(0, 0, width, height);

		if (spielfeld != null) {
			paintFloor(g);
			paintAvatar(g);
		} else {
			g.setColor(Color.RED);
			g.drawLine(0, 0, width, height);
			g.drawLine(width, 0, 0, height);
		}
	}

	private void paintAvatar(Graphics g) {
		int size = (int) Math.floor(Math.min(
				getWidth() / ((double) spielfeld.getWidth()), getHeight()
						/ ((double) spielfeld.getHeight())));

		int inset = Pixel.pointsToPixel(3);

		int fullWidth = size * spielfeld.getWidth();
		int fullHeight = size * spielfeld.getHeight();

		int offsetWidth = (getWidth() - fullWidth) / 2;
		int offsetHeight = (getHeight() - fullHeight) / 2;

		Location actLocation;
		for (int y = 0; y < spielfeld.getHeight(); y++) {
			for (int x = 0; x < spielfeld.getWidth(); x++) {
				actLocation = new Location(x, y);

				if (actLocation.equals(posAvatar)) {
					drawTile(g, size, inset, offsetWidth, offsetHeight,
							getAvatar(directionAvatar), y, x);
					if (speech != null && !speech.isEmpty()) {
						g.setColor(new Color(255, 255, 255, 150));
						FontMetrics fmt = g.getFontMetrics();
						int xt = getPosForPainting(size, inset, offsetWidth, x);
						int yt = getPosForPainting(size, inset, offsetHeight, y);
						g.fill3DRect(xt, yt, fmt.stringWidth(speech) + 5,
								fmt.getHeight() + 5, true);
						g.setColor(Color.BLACK);
						g.drawString(speech, xt + 2, yt + fmt.getHeight() + 2);
					}
				}

			}
		}

	}

	private void paintFloor(Graphics g) {

		int size = (int) Math.floor(Math.min(
				getWidth() / ((double) spielfeld.getWidth()), getHeight()
						/ ((double) spielfeld.getHeight())));

		int inset = Pixel.pointsToPixel(3);

		int fullWidth = size * spielfeld.getWidth();
		int fullHeight = size * spielfeld.getHeight();

		int offsetWidth = (getWidth() - fullWidth) / 2;
		int offsetHeight = (getHeight() - fullHeight) / 2;

		Image tile;
		for (int y = 0; y < spielfeld.getHeight(); y++) {
			for (int x = 0; x < spielfeld.getWidth(); x++) {
				tile = getTile(x, y);
				g.setColor(Color.BLACK);
				g.fillRect(offsetWidth + x * size, offsetHeight + y * size,
						size, size);

				drawTile(g, size, inset, offsetWidth, offsetHeight, tile, y, x);

			}
		}

	}

	private Image getAvatar(int direction) {
		return skinImporter.getAvatar(direction);
	}

	private void drawTile(Graphics g, int size, int inset, int offsetWidth,
			int offsetHeight, Image tile, int y, int x) {
		g.drawImage(tile, getPosForPainting(size, inset, offsetWidth, x),
				getPosForPainting(size, inset, offsetHeight, y), offsetWidth
						+ x * size + size - inset, offsetHeight + y * size
						+ size - inset, 0, 0, tile.getWidth(this),
				tile.getHeight(this), this);
	}

	private int getPosForPainting(int size, int inset, int offset, int xORy) {
		return offset + xORy * size + ((xORy == 0) ? inset : 0);
	}

	private Image getTile(int x, int y) {
		Image ret = null;

		if (spielfeld.getFeld(x, y) instanceof ZielFeld) {
			ZielFeld zf = (ZielFeld) spielfeld.getFeld(x, y);

			ret = skinImporter.getImage(zf.getNumber());

		} else {
			ret = skinImporter.getImage(spielfeld.getFeld(x, y).getTyp());
		}

		return ret;
	}

	public Dimension getMinimalSize() {
		return new Dimension(MINIMAL_FELD_SIZE.width * spielfeld.getWidth(),
				MINIMAL_FELD_SIZE.height * spielfeld.getHeight());
	}

	public Dimension getPrefferedSize() {
		return new Dimension(NORMAL_FELD_SIZE.width * spielfeld.getWidth(),
				NORMAL_FELD_SIZE.height * spielfeld.getHeight());
	}

	public int getWidthPt() {
		return Pixel.pixelToPoints(getWidth());
	}

	public int getHeightPt() {
		return Pixel.pixelToPoints(getHeight());
	}

	public Location getAvatarPosition() {
		return new Location(posAvatar);
	}

	public void moveAvatarToLocation(Location l) {
		if (l == null) {
			return;
		}

		if (l.getX() >= 0 && l.getY() >= 0 && l.getX() < spielfeld.getWidth()
				&& l.getY() < spielfeld.getHeight()) {
			if (spielfeld.canMoveTo(l.getX(), l.getY())) {
				posAvatar.setX(l.getX());
				posAvatar.setY(l.getY());
			}
		}
		repaint();
	}

	public void setAvatarsDirection(int direction) {
		if (!Avatar.isValidDirection(direction)) {
			return;
		}

		this.directionAvatar = direction;
		repaint();
	}

	public int getAvatarsDirection() {
		return this.directionAvatar;
	}

	private void waitAfterMovement() {
		try {
			Thread.sleep(WAIT);
		} catch (final InterruptedException e) {
		} finally {
			speech = null;
		}
	}

	private void say(String s) {
		LOG.info("Sprich: '" + s + "'");
		speech = s;
		repaint();
	}

	@Override
	public void turnLeft() {
		if (isConfused) {
			turnRight_();
			return;
		}

		turnLeft_();
	}

	private void turnLeft_() {
		int actualDirection = getAvatarsDirection();
		setAvatarsDirection(Avatar.leftOf(actualDirection));

		waitAfterMovement();
	}

	@Override
	public void turnRight() {
		if (isConfused) {
			turnLeft_();
			return;
		}

		turnRight_();
	}

	private void turnRight_() {
		int actualDirection = getAvatarsDirection();
		setAvatarsDirection(Avatar.rightOf(actualDirection));

		waitAfterMovement();
	}

	@Override
	public boolean moveForwards() {
		if (isConfused) {
			return moveBackwards_();
		}

		return moveForwards_();
	}

	private boolean moveForwards_() {
		if (isBlockedFieldInFront()) {
			say(String(ROBOT_WALL));
			finishedGameFailed();
			return false;
		}

		Location actualLocation = getAvatarPosition();
		Location newLocation = Avatar.fieldInFront(actualLocation,
				getAvatarsDirection());

		if (!isLocationInBoundsOfSpielfeld(newLocation)) {
			say(String(ROBOT_OUT_OF_MAP));
			finishedGameFailed();
			return false;
		}

		moveAvatarToLocation(newLocation);

		if (standOnConfusingField()) {
			isConfused = !isConfused;
		}

		checkAndSayGoal();

		waitAfterMovement();

		return true;
	}

	private void checkAndSayGoal() {
		int answer = checkIfGoalIsFinished();

		if (answer > 0) {
			say(String.format(String(ROBOT_ZIEL_X), answer));
		}

		if (answer == 0) {
			say(String(ROBOT_ZIEL_SUC));
			finishdGameSuccessful();
		}
	}

	private int checkIfGoalIsFinished() {
		int goal;
		if ((goal = standOnGoalField()) == nextGoal) {
			nextGoal = goal + 1;

			if (nextGoal > lastGoal) {
				return 0;
			} else {
				return nextGoal - 1;
			}
		}

		return -1;
	}

	private void finishdGameSuccessful() {
		Signals.sendSignal(Signals.SIGNAL_STOP_BTN);
		Signals.sendSignal(Signals.SIGNAL_FINISHED, Boolean.TRUE);
	}

	private void finishedGameFailed() {
		Signals.sendSignal(Signals.SIGNAL_STOP_BTN);
		Signals.sendSignal(Signals.SIGNAL_FINISHED, Boolean.FALSE);
	}

	@Override
	public boolean moveBackwards() {
		if (isConfused) {
			return moveForwards_();
		}

		return moveBackwards_();
	}

	private boolean moveBackwards_() {
		if (isBlockedFieldBehind()) {
			say(String(ROBOT_WALL));
			finishedGameFailed();
			return false;
		}

		Location actualLocation = getAvatarPosition();
		Location newLocation = Avatar.fieldBehind(actualLocation,
				getAvatarsDirection());

		if (!isLocationInBoundsOfSpielfeld(newLocation)) {
			say(String(ROBOT_OUT_OF_MAP));
			finishedGameFailed();
			return false;
		}

		moveAvatarToLocation(newLocation);

		if (standOnConfusingField()) {
			isConfused = !isConfused;
		}

		checkAndSayGoal();

		waitAfterMovement();

		return true;
	}

	public boolean isLocationInBoundsOfSpielfeld(Location location) {
		return spielfeld.isLocationInBounds(location);
	}

	@Override
	public boolean isFieldInFrontAccessible() {
		boolean ret = true;

		if (isBlockedFieldInFront()) {
			ret = false;
		} else {
			Location actualLocation = getAvatarPosition();
			Location newLocation = Avatar.fieldInFront(actualLocation,
					getAvatarsDirection());
			if (!isLocationInBoundsOfSpielfeld(newLocation)) {
				ret = false;
			}
		}

		return ret;
	}

	@Override
	public boolean isBlockedFieldInFront() {
		boolean ret = false;

		Location actualLocation = getAvatarPosition();
		Location newLocation = Avatar.fieldInFront(actualLocation,
				getAvatarsDirection());

		if (!isLocationInBoundsOfSpielfeld(newLocation)) {
			return false;
		}

		Feld fieldInFront = spielfeld.getFeld(newLocation.getX(),
				newLocation.getY());

		if (fieldInFront.getTyp().equals(FeldTyp.BLOCKED)) {
			ret = true;
		}

		return ret;
	}

	private boolean isBlockedFieldBehind() {
		boolean ret = false;

		Location actualLocation = getAvatarPosition();
		Location newLocation = Avatar.fieldBehind(actualLocation,
				getAvatarsDirection());

		if (!isLocationInBoundsOfSpielfeld(newLocation)) {
			return false;
		}

		Feld fieldInFront = spielfeld.getFeld(newLocation.getX(),
				newLocation.getY());

		if (fieldInFront.getTyp().equals(FeldTyp.BLOCKED)) {
			ret = true;
		}

		return ret;
	}

	@Override
	public boolean standOnConfusingField() {
		boolean ret = false;

		Location actualLocation = getAvatarPosition();

		Feld field = spielfeld.getFeld(actualLocation.getX(),
				actualLocation.getY());

		if (field.getTyp().equals(FeldTyp.CONFUSE)) {
			ret = true;
		}

		return ret;
	}

	@Override
	public boolean isConfused() {

		return isConfused;
	}

	@Override
	public int standOnGoalField() {
		int ret = -1;

		Location actualLocation = getAvatarPosition();

		Feld field = spielfeld.getFeld(actualLocation.getX(),
				actualLocation.getY());

		if (field instanceof ZielFeld) {
			ret = ((ZielFeld) field).getNumber();
		}

		return ret;
	}

	@Override
	public boolean Signal(String signal, Object... values) {

		switch (signal) {
		case Signals.SIGNAL_EDIT_BTN:
			switchVisibleStateOfKonsole();
			return true;
		case Signals.SIGNAL_PLAY_BTN:
			playSignal();
			return true;
		case Signals.SIGNAL_STOP_BTN:
			konsole.stopProgram();
			return true;
		}

		return false;
	}

	private void playSignal() {
		setVisibleStateOfKonsole(false);
		konsole.stopProgram();
		this.isConfused = false;
		this.nextGoal = 1;
		this.speech = null;
		this.directionAvatar = this.spielfeld.getStartDirection();
		moveAvatarToLocation(spielfeld.getStartPoint());
		playController.minimizeSwitch();

		new SwingWorker<Void, Object>() {

			@Override
			protected Void doInBackground() throws Exception {
				waitAfterMovement();
				konsole.runProgram(thisg());
				return null;
			}

		}.execute();
	}

	private void switchVisibleStateOfKonsole() {
		setVisibleStateOfKonsole(!konsole.isVisible());
	}

	private void setVisibleStateOfKonsole(boolean visible) {
		konsole.setVisible(visible);
		playController.setKonsoleButtonAktiv(konsole.isVisible());
	}

	private Playground thisg() {
		return this;
	}
}
