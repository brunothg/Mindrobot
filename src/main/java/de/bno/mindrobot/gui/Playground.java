package de.bno.mindrobot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.FeldTyp;
import de.bno.mindrobot.feld.ZielFeld;

public class Playground extends JComponent {

	private static final long serialVersionUID = -5702637217520674503L;

	private static final Dimension MINIMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(50), Pixel.pointsToPixel(50));

	private static final Dimension NORMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(100), Pixel.pointsToPixel(100));

	private SpielfeldData spielfeld;

	private Image defFloor;
	private Image defBlocked;
	private Image defConfuse;
	private Image defZiel;
	private Image defColored;
	private Image[] usrZiel;

	public Playground(SpielfeldData spielfeld) {
		super();
		this.spielfeld = spielfeld;

		loadImages();
	}

	private void loadImages() {

		defFloor = loadIcon("Floor_Normal.jpg").getImage();
		defBlocked = loadIcon("Floor_Closed.jpg").getImage();
		defConfuse = loadIcon("Floor_Normal.jpg").getImage();
		defZiel = loadIcon("Floor_Normal.jpg").getImage();
		defColored = loadIcon("Floor_Normal.jpg").getImage();

		usrZiel = null;
		tryLoadusrImages();
	}

	private void tryLoadusrImages() {

	}

	@Override
	public void paint(Graphics g) {

		paintFloor(g);

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

				g.drawImage(tile, offsetWidth + x * size
						+ ((x == 0) ? inset : 0), offsetHeight + y * size
						+ ((y == 0) ? inset : 0), offsetWidth + x * size + size
						- inset, offsetHeight + y * size + size - inset, 0, 0,
						tile.getWidth(this), tile.getHeight(this), this);
			}
		}

	}

	private Image getTile(int x, int y) {
		Image ret = defFloor;

		if (spielfeld.getFeld(x, y).getTyp() == FeldTyp.BLOCKED) {
			ret = defBlocked;
		} else if (spielfeld.getFeld(x, y).getTyp() == FeldTyp.COLORED) {
			ret = defColored;
		} else if (spielfeld.getFeld(x, y).getTyp() == FeldTyp.CONFUSE) {
			ret = defConfuse;
		} else if (spielfeld.getFeld(x, y).getTyp() == FeldTyp.ZIEL) {
			if (usrZiel != null
					&& spielfeld.getFeld(x, y) instanceof ZielFeld
					&& usrZiel.length >= ((ZielFeld) spielfeld.getFeld(x, y))
							.getNumber()) {

				ret = usrZiel[((ZielFeld) spielfeld.getFeld(x, y)).getNumber() - 1];

			} else {
				ret = defZiel;
			}
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

	private ImageIcon loadIcon(String s) {
		return new ImageIcon(getClass().getClassLoader().getResource(
				"de/bno/mindrobot/gui/" + s));
	}

}
