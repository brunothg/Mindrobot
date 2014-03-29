package de.bno.mindrobot.data.importer;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import de.bno.mindrobot.data.importer.SpielfeldImporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public class FileSpielfeldImporter implements SpielfeldImporter {

	Path path;

	public FileSpielfeldImporter(Path path) {
		this.path = path;
	}

	public FileSpielfeldImporter(String path) {
		this(FileSystems.getDefault().getPath(path));
	}

	@Override
	public SpielfeldData importSpielfeld() {
		return null;
	}

}
