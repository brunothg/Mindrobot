package de.bno.mindrobot.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import de.bno.mindrobot.data.importer.SkinImporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.ZielFeld;

public class PlaygroundPreview extends JComponent {

	private static final long serialVersionUID = 7488982726954004202L;
	private SpielfeldData spielfeld;

	private final GraphicsConfiguration gfxConf = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();

	BufferedImage img;
	SkinImporter skinImporter;

	public PlaygroundPreview(SkinImporter skinImporter) {
		this.spielfeld = null;
		this.skinImporter = skinImporter;
	}

	public void setSpielfeld(SpielfeldData spielfeld) {
		this.spielfeld = spielfeld;
		repaint();
	}

	protected void paintComponent(Graphics g) {
		if (spielfeld != null && skinImporter != null) {
			if (img == null || img.getWidth() != getWidth()
					|| img.getHeight() != getHeight()) {
				img = gfxConf.createCompatibleImage(getWidth(), getHeight(),
						Transparency.TRANSLUCENT);
			}

			bigPaint(img.createGraphics(), img.getWidth(), img.getHeight());

			g.drawImage(img, 0, 0, this);
		} else {
			g.setColor(Color.RED);
			g.drawLine(0, 0, getWidth(), getHeight());
			g.drawLine(getWidth(), 0, 0, getHeight());
		}
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

				if (actLocation.equals(spielfeld.getStartPoint())) {
					drawTile(g, size, inset, offsetWidth, offsetHeight,
							getAvatar(spielfeld.getStartDirection()), y, x);
				}

			}
		}

	}

	private void paintFloor(Graphics g) {

		int size = (int) Math.floor(Math.min(
				getWidth() / ((double) spielfeld.getWidth()), getHeight()
						/ ((double) spielfeld.getHeight())));

		int inset = Pixel.pointsToPixel(2);

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

}
