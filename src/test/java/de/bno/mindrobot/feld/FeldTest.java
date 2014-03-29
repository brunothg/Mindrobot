package de.bno.mindrobot.feld;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FeldTest {

	Feld feld;

	@Test
	public void createInstance() throws Exception {
		feld = new Feld();
	}

	@Test
	public void compareFeld() throws Exception {
		feld = new Feld();
		Feld f2 = new Feld();

		assertTrue(feld.equals(f2));

		f2 = new Feld(FeldTyp.BLOCKED);

		assertFalse(feld.equals(f2));
	}

}
