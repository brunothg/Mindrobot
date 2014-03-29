package de.bno.mindrobot.data.importer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public class FileSpielfeldImporter implements SpielfeldImporter {

	private static final Logger LOG = MindRobot
			.getLogger(FileSpielfeldImporter.class);

	Path path;

	public FileSpielfeldImporter(Path path) {
		this.path = path;
	}

	public FileSpielfeldImporter(String path) {
		this(FileSystems.getDefault().getPath(path));
	}

	@Override
	public SpielfeldData importSpielfeld() throws IOException {
		LOG.info("Lade Spielfelddaten von " + path.toAbsolutePath());

		StreamSpielfeldImporter streamimport = new StreamSpielfeldImporter(Files.newInputStream(path));
		
		return streamimport.importSpielfeld();
	}

}
