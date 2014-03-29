package de.bno.mindrobot.data.spielfeld;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.bno.mindrobot.feld.Feld;
import de.bno.mindrobot.feld.FeldTyp;

public class SpielfeldDataTest {

	SpielfeldData sp;

	@Before
	public void setUp() throws Exception {
		sp = new SpielfeldData(5, 5);
	}

	@Test
	public void setAndGetFeld() throws Exception {
		Feld feld = new Feld(FeldTyp.BLOCKED);
		sp.setFeld(0, 0, feld);

		assertTrue(sp.getFeld(0, 0) == feld);
	}

}
