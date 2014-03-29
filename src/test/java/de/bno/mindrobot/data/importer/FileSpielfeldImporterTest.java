package de.bno.mindrobot.data.importer;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import de.bno.mindrobot.data.exporter.FileSpielfeldExporter;
import de.bno.mindrobot.data.exporter.SpielfeldExporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.BlockedFeld;
import de.bno.mindrobot.feld.ConfusingFeld;

public class FileSpielfeldImporterTest {

	SpielfeldImporter importer;

	@Test
	public void exportAndImport() throws Exception {

		SpielfeldData sp = new SpielfeldData(3, 3);
		sp.setFeld(0, 0, new BlockedFeld());
		sp.setFeld(1, 1, new ConfusingFeld());

		Files.createDirectory(Paths.get("./test"));

		SpielfeldExporter export = new FileSpielfeldExporter("./test/Feld.sp");
		export.exportSpielfeld(sp);

		importer = new FileSpielfeldImporter("./test/Feld.sp");
		SpielfeldData sp2 = importer.importSpielfeld();

		assertEquals(sp.getHeight(), sp2.getHeight());
		assertEquals(sp.getWidth(), sp2.getWidth());

		for (int y = 0; y < sp.getHeight(); y++) {
			for (int x = 0; x < sp.getWidth(); x++) {
				sp.getFeld(x, y).equals(sp2.getFeld(x, y));
			}
		}
	}

}
