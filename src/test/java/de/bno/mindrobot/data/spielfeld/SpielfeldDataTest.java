package de.bno.mindrobot.data.spielfeld;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.bno.mindrobot.feld.BlockedFeld;

public class SpielfeldDataTest {

	SpielfeldData sp;

	@Before
	public void setUp() throws Exception {
		sp = new SpielfeldData();
	}

	@Test
	public void setAndGetFeld() throws Exception {
		BlockedFeld feld = new BlockedFeld();
		sp.setFeld(0, 0, feld);

		assertTrue(sp.getFeld(0, 0) == feld);
	}

}
