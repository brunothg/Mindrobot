package de.bno.mindrobot.data.importer;

import java.io.IOException;

import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public interface SpielfeldImporter {

	public SpielfeldData importSpielfeld() throws IOException;

}
