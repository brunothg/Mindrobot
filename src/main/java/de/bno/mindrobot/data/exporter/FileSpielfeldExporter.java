package de.bno.mindrobot.data.exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public class FileSpielfeldExporter implements SpielfeldExporter {

	private static final Logger LOG = MindRobot
			.getLogger(FileSpielfeldExporter.class);

	Path path;

	public FileSpielfeldExporter(Path path) {
		this.path = path;
	}

	public FileSpielfeldExporter(String path) {
		this(Paths.get(path));
	}

	@Override
	public void exportSpielfeld(SpielfeldData spielfeld) throws IOException {

		LOG.info("Lade Spielfelddaten von " + path.toAbsolutePath());

		StreamSpielfeldExporter streamexport = new StreamSpielfeldExporter(Files.newOutputStream(path));
		streamexport.exportSpielfeld(spielfeld);

	}
}
