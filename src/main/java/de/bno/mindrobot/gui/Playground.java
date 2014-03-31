package de.bno.mindrobot.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public class Playground extends JComponent {

	private static final long serialVersionUID = -5702637217520674503L;

	private static final Dimension MINIMAL_FELD_SIZE = new Dimension(5, 5);

	private SpielfeldData spielfeld;

	public Playground(SpielfeldData spielfeld) {
		super();
		this.spielfeld = spielfeld;
	}

	@Override
	public void paint(Graphics g) {
		System.out.println(String.format("Width: %d (%d) Height: %d (%d)",
				getWidth(), getWidthPt(), getHeight(), getHeightPt()));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public int getWidthPt() {
		return Pixel.pixelToPoints(getWidth());
	}

	public int getHeightPt() {
		return Pixel.pixelToPoints(getHeight());
	}

}
