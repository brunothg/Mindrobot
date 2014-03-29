package de.bno.mindrobot.feld;

import static org.junit.Assert.*;

import org.junit.Test;

public class ZielFeldTest {

	@Test
	public void compareToOtherZielFeld() throws Exception {
		ZielFeld zf1 = new ZielFeld(1);
		ZielFeld zf2 = new ZielFeld(2);

		assertTrue(zf1.compareTo(zf2) > 0);
		assertTrue(zf2.compareTo(zf1) < 0);
		assertEquals(0, zf1.compareTo(zf1));
	}

}
