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

	@Test
	public void compareToBlockedFeld() throws Exception {
		feld = new Feld();
		Feld bf = new Feld(FeldTyp.BLOCKED);

		assertFalse(feld.equals(bf));

		feld = new Feld(FeldTyp.BLOCKED);

		assertTrue(feld.equals(bf));
	}

	@Test
	public void compareToConfusingFeld() throws Exception {
		feld = new Feld();
		Feld bf = new Feld(FeldTyp.CONFUSE);

		assertFalse(feld.equals(bf));

		feld = new Feld(FeldTyp.CONFUSE);

		assertTrue(feld.equals(bf));
	}

	@Test
	public void compareFeldToOtheObject() throws Exception {
		feld = new Feld();
		Object obj = new Object();

		assertFalse(feld.equals(obj));
	}

}
