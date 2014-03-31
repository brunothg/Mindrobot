package de.bno.mindrobot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public class Playground extends JComponent {

	private static final long serialVersionUID = -5702637217520674503L;

	private static final Dimension MINIMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(50), Pixel.pointsToPixel(50));

	private static final Dimension NORMAL_FELD_SIZE = new Dimension(
			Pixel.pointsToPixel(150), Pixel.pointsToPixel(150));

	private SpielfeldData spielfeld;

	private ImageIcon defFloor;

	public Playground(SpielfeldData spielfeld) {
		super();
		this.spielfeld = spielfeld;

		loadImages();
	}

	private void loadImages() {

		defFloor = loadIcon("Floor_Normal.jpg");

	}

	public Dimension getMinimalSize() {
		return new Dimension(MINIMAL_FELD_SIZE.width * spielfeld.getWidth(),
				MINIMAL_FELD_SIZE.height * spielfeld.getHeight());
	}

	public Dimension getPrefferedSize() {
		return new Dimension(NORMAL_FELD_SIZE.width * spielfeld.getWidth(),
				NORMAL_FELD_SIZE.height * spielfeld.getHeight());
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
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
