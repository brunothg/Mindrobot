package de.bno.mindrobot.gui;

import java.awt.Toolkit;

public class Pixel {

	public static int pointsToPixel(int points) {
		int ret = points;

		try {
			int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
			ret = Math.max((int) (((1 / 72.0) * dpi) * points), 1);
		} catch (Exception e) {
		}

		return ret;
	}

	public static int pixelToPoints(int pixel) {
		int ret = pixel;

		try {
			int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
			ret = Math.max((int) ((72.0 / dpi) * pixel), 1);
		} catch (Exception e) {
		}

		return ret;
	}

}
