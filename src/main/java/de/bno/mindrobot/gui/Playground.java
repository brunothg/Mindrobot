package de.bno.mindrobot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public class Playground extends JComponent {

	private static final long serialVersionUID = -5702637217520674503L;

	private static final Dimension MINIMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(50), Pixel.pointsToPixel(50));

	private static final Dimension NORMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(100), Pixel.pointsToPixel(100));

	private SpielfeldData spielfeld;

	private Image defFloor;

	public Playground(SpielfeldData spielfeld) {
		super();
		this.spielfeld = spielfeld;

		loadImages();
	}

	private void loadImages() {

		defFloor = loadIcon("Floor_Normal.jpg").getImage();

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

		for (int y = 0; y < spielfeld.getHeight(); y++) {
			for (int x = 0; x < spielfeld.getWidth(); x++) {
				g.setColor(Color.BLACK);
				g.fillRect(offsetWidth + x * size, offsetHeight + y * size,
						size, size);

				g.drawImage(defFloor, offsetWidth + x * size
						+ ((x == 0) ? inset : 0), offsetHeight + y * size
						+ ((y == 0) ? inset : 0), offsetWidth + x * size + size
						- inset, offsetHeight + y * size + size - inset, 0, 0,
						defFloor.getWidth(this), defFloor.getHeight(this), this);
			}
		}

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
