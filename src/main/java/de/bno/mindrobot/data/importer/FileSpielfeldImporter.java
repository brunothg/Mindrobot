package de.bno.mindrobot.data.importer;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.exporter.FileSpielfeldExporter;
import de.bno.mindrobot.data.importer.SpielfeldImporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.Feld;

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

		SpielfeldData sp = null;

		BufferedReader reader = Files.newBufferedReader(path,
				FileSpielfeldExporter.CHARSET);

		String header = getNextNotEmptyLine(reader);
		if (!acceptHeader(header)) {
			LOG.warning("Beim Import eines Spielfeldes ist ein Fehler aufgetreten. Unbekannter Header: "
					+ header);
			throw new IOException("Unknown Mime-Type: " + header);
		}

		String size = getNextNotEmptyLine(reader);
		Dimension dimension = toDimesnion(size);
		if (dimension == null) {
			LOG.warning("Beim Import eines Spielfeldes ist ein Fehler aufgetreten. Unbekannte Größe: "
					+ size);
			throw new IOException("Can't find size. Found: " + size);
		}

		String start = getNextNotEmptyLine(reader).trim();
		if (!start.equalsIgnoreCase("START")) {
			LOG.warning("Beim Import eines Spielfeldes ist ein Fehler aufgetreten. START Tag erwartet. Gefunden: "
					+ start);
			throw new IOException("Could not find START Tag. Found: " + start);
		}

		char[][] feld = getFelder(reader, dimension.width, dimension.height);

		sp = new SpielfeldData(dimension.width, dimension.height);
		for (int y = 0; y < feld.length; y++) {
			for (int x = 0; x < feld[y].length; x++) {
				sp.setFeld(x, y, Feld.getFeldFromChar(feld[y][x]));
			}
		}

		return sp;
	}

	private char[][] getFelder(BufferedReader reader, int width, int height)
			throws IOException {

		String tmp;
		List<String> rows = new LinkedList<String>();

		for (int i = 0; i < height && (tmp = reader.readLine()) != null; i++) {
			if (tmp.trim().equalsIgnoreCase("END")) {
				break;
			}
			rows.add(new String(tmp));
		}

		char[][] ret = new char[height][width];

		int y = 0;
		for (String row : rows) {

			for (int x = 0; x < width; x++) {
				ret[y][x] = (x < row.length()) ? row.charAt(x) : ' ';
			}

			y++;
		}

		for (; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ret[y][x] = ' ';
			}
		}

		return ret;
	}

	private Dimension toDimesnion(String size) {

		int width = -1;
		int height = -1;

		String widthString = size.substring(0, size.indexOf(',')).trim();
		String heightString = size.substring(size.indexOf(',') + 1,
				size.indexOf(':')).trim();

		width = Integer.valueOf(widthString).intValue();
		height = Integer.valueOf(heightString).intValue();

		if (width < 0 || height < 0) {
			return null;
		} else {
			return new Dimension(width, height);
		}
	}

	private boolean acceptHeader(String header) {
		return header.trim().equalsIgnoreCase(FileSpielfeldExporter.MIME_TYPE);
	}

	private String getNextNotEmptyLine(BufferedReader reader)
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
