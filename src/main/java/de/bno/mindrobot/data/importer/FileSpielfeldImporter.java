package de.bno.mindrobot.data.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.exporter.StreamSpielfeldExporter;
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

		StreamSpielfeldImporter streamimport = new StreamSpielfeldImporter(
				Files.newInputStream(path));

		return streamimport.importSpielfeld();
	}

	public static List<String> getSpielfelder(Path search) {

		if (!Files.exists(search) || !Files.isDirectory(search)) {
			return null;
		}

		List<String> ret = new LinkedList<String>();

		try {
			DirectoryStream<Path> dirs = Files.newDirectoryStream(search);
			for (Path file : dirs) {
				if (Files.isReadable(file) && Files.isRegularFile(file)) {
					if (isSpielfeld(file)) {
						ret.add(file.getName(file.getNameCount() - 1)
								.toString());
					}
				}
			}
		} catch (IOException e) {
			LOG.warning("Fehler beim Suchen von Spielfeldern: "
					+ e.getMessage());
		}

		return ret;
	}

	private static boolean isSpielfeld(Path file) throws IOException {

		BufferedReader reader = Files.newBufferedReader(file,
				StreamSpielfeldExporter.CHARSET);

		String header = getNextNotEmptyLine(reader).trim();

		if (header == null) {
			return false;
		}

		return header.equalsIgnoreCase(StreamSpielfeldExporter.MIME_TYPE);
	}

	private static String getNextNotEmptyLine(BufferedReader reader)
			throws IOException {

		String ret = null;

		String tmp;

		loop: while ((tmp = reader.readLine()) != null) {
			if (tmp.length() > 0) {
				for (int i = 0; i < tmp.length(); i++) {
					if (tmp.charAt(i) != ' ') {
						ret = tmp;
						break loop;
					}
				}
			}
		}

		return ret;
	}

}
