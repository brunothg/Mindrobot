package de.bno.mindrobot.data.importer;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.exporter.StreamSpielfeldExporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.Feld;
import de.bno.mindrobot.feld.FeldTyp;
import de.bno.mindrobot.feld.StartFeld;
import de.bno.mindrobot.feld.ZielFeld;

public class StreamSpielfeldImporter implements SpielfeldImporter {

	private static final Logger LOG = MindRobot
			.getLogger(StreamSpielfeldImporter.class);

	InputStream in;

	public StreamSpielfeldImporter(InputStream in) {
		this.in = in;
	}

	@Override
	public SpielfeldData importSpielfeld() throws IOException {
		SpielfeldData sp = null;

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

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

		Feld[][] feld = getFelder(reader, dimension.width, dimension.height);

		sp = new SpielfeldData(dimension.width, dimension.height);
		for (int y = 0; y < feld.length; y++) {
			for (int x = 0; x < feld[y].length; x++) {
				sp.setFeld(x, y, feld[y][x]);
			}
		}

		return sp;
	}

	private Feld[][] getFelder(BufferedReader reader, int width, int height)
			throws IOException {

		String tmp;
		List<String> rows = new LinkedList<String>();

		for (int i = 0; i < height && (tmp = reader.readLine()) != null; i++) {
			if (tmp.trim().equalsIgnoreCase("END")) {
				break;
			}
			rows.add(new String(tmp));
		}

		Feld[][] ret = new Feld[height][width];

		int y = 0;
		for (String row : rows) {

			int i = 0;
			for (int x = 0; x < width; x++) {

				if (row.charAt(i) == FeldTyp.ZIEL.getCharRepresentation()) {

					ZielFeld zf = new ZielFeld();

					i++;
					int start = i;
					while (i < row.length() && Character.isDigit(row.charAt(i))) {
						i++;
					}

					String number = row.substring(start, i);
					zf.setNumber(Integer.valueOf(number).intValue());
					ret[y][x] = zf;

				} else if (row.charAt(i) == FeldTyp.START
						.getCharRepresentation()) {

					StartFeld sf = new StartFeld();

					i++;
					int start = i;
					while (i < row.length() && Character.isDigit(row.charAt(i))) {
						i++;
					}

					String number = row.substring(start, i);
					if (number != null && !number.isEmpty()) {
						sf.setDirection(Integer.valueOf(number).intValue());
					}
					ret[y][x] = sf;
				} else {
					ret[y][x] = (i < row.length()) ? Feld.getFeldFromChar(row
							.charAt(i)) : new Feld();
					i++;
				}

			}

			y++;
		}

		for (; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ret[y][x] = new Feld();
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
		return header.trim()
				.equalsIgnoreCase(StreamSpielfeldExporter.MIME_TYPE);
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
