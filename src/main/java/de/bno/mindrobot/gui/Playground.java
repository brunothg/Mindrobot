package de.bno.mindrobot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

import de.bno.mindrobot.data.importer.CustomFileSkinImporter;
import de.bno.mindrobot.data.importer.SkinImporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.StartFeld;
import de.bno.mindrobot.feld.ZielFeld;

public class Playground extends JComponent {

	private static final long serialVersionUID = -5702637217520674503L;

	private static final Dimension MINIMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(50), Pixel.pointsToPixel(50));

	private static final Dimension NORMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(100), Pixel.pointsToPixel(100));

	private SpielfeldData spielfeld;

	private SkinImporter skinImporter;

	private String map;

	private PlayController playController;

	private Location posAvatar;
	private int directionAvatar;

	public Playground(SpielfeldData spielfeld, String map) {
		super();
		this.spielfeld = spielfeld;
		this.map = map;
		this.posAvatar = this.spielfeld.getStartPoint();
		this.directionAvatar = this.spielfeld.getStartDirection();

		setLayout(null);

		loadImages();

		createControl();

	}

	private void createControl() {
		playController = new PlayController();
		playController.setSize(playController.getPreferredSize());
		playController.setLocation(5, 5);
		add(playController);
	}

	private void loadImages() {
		skinImporter = new CustomFileSkinImporter(this.map);
	}

	@Override
	public void paint(Graphics g) {

		paintFloor(g);

		updateControllerSize();

		paintChildren(g);
	}

	private void updateControllerSize() {
		Dimension prefSize = playController.getPreferredSize();
		Dimension size = new Dimension(Math.min(prefSize.width, getWidth()),
				Math.min(prefSize.height, getHeight()));
		playController.setSize(size);
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
		Location actLocation;
		for (int y = 0; y < spielfeld.getHeight(); y++) {
			for (int x = 0; x < spielfeld.getWidth(); x++) {
				actLocation = new Location(x, y);
				tile = getTile(x, y);
				g.setColor(Color.BLACK);
				g.fillRect(offsetWidth + x * size, offsetHeight + y * size,
						size, size);

				drawTile(g, size, inset, offsetWidth, offsetHeight, tile, y, x);

				if (actLocation.equals(posAvatar)) {
					drawTile(g, size, inset, offsetWidth, offsetHeight,
							getAvatar(directionAvatar), y, x);
				}

			}
		}

	}

	private Image getAvatar(int direction) {
		return skinImporter.getAvatar(direction);
	}

	private void drawTile(Graphics g, int size, int inset, int offsetWidth,
			int offsetHeight, Image tile, int y, int x) {
		g.drawImage(tile, offsetWidth + x * size + ((x == 0) ? inset : 0),
				offsetHeight + y * size + ((y == 0) ? inset : 0), offsetWidth
						+ x * size + size - inset, offsetHeight + y * size
						+ size - inset, 0, 0, tile.getWidth(this),
				tile.getHeight(this), this);
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
	}

	public void setAvatarsDirection(int direction) {
		if (!StartFeld.isValidDirection(direction)) {
			return;
		}

		this.directionAvatar = direction;
	}

}
