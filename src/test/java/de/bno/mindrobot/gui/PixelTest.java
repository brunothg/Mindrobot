package de.bno.mindrobot.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class PixelTest {

	@Test
	public void convertPointsToPixel() throws Exception {
		int points = 12;
		int pixel = Pixel.pointsToPixel(points);
		int points2 = Pixel.pixelToPoints(pixel);

		assertEquals(points, points2);
	}
}
