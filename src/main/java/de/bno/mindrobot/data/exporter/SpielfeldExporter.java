package de.bno.mindrobot.data.exporter;

import java.io.IOException;

import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public interface SpielfeldExporter {

	public void exportSpielfeld(SpielfeldData spielfeld) throws IOException;

}
