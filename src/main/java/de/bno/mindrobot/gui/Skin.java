package de.bno.mindrobot.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Skin {

	private BufferedImage img;

	public Skin(BufferedImage img) {
		this.img = img;
	}

	public BufferedImage getImage() {
		return img;
	}

	public BufferedImage getImage(int width, int height, ImageObserver observer) {

		BufferedImage ret = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics g = ret.getGraphics();

		g.drawImage(img, 0, 0, width, height, 0, 0, img.getWidth(),
				img.getHeight(), observer);

		return ret;
	}

	public BufferedImage getImage(double scale, ImageObserver observer) {
		return getImage((int) (img.getWidth() * scale),
				(int) (img.getHeight() * scale), observer);
	}

	public BufferedImage getImage(int width, ImageObserver observer) {
		int height = (int) (img.getHeight() * ((double) width / (double) img
				.getWidth()));

		return getImage(width, height, observer);
	}

}
