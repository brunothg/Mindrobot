package de.bno.mindrobot.data.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.Feld;
import de.bno.mindrobot.feld.ZielFeld;

public class FileSpielfeldExporter implements SpielfeldExporter {

	private static final Logger LOG = MindRobot
			.getLogger(FileSpielfeldExporter.class);

	Path path;
	public static final String MIME_TYPE = "text/mindrobot";
	public static final Charset CHARSET = StandardCharsets.UTF_8;

	public FileSpielfeldExporter(Path path) {
		this.path = path;
	}

	public FileSpielfeldExporter(String path) {
		this(Paths.get(path));
	}

	@Override
	public void exportSpielfeld(SpielfeldData spielfeld) throws IOException {

		LOG.info("Lade Spielfelddaten von " + path.toAbsolutePath());

		BufferedWriter writer = Files.newBufferedWriter(path, CHARSET);

		writer.append(MIME_TYPE);
		writer.newLine();

		writer.append(spielfeld.getWidth() + ", " + spielfeld.getHeight() + ":");
		writer.newLine();

		writer.append("START");
		writer.newLine();

		for (int y = 0; y < spielfeld.getHeight(); y++) {
			for (int x = 0; x < spielfeld.getWidth(); x++) {
				Feld feld = spielfeld.getFeld(x, y);
				writer.append(feld.toChar());

				if (feld instanceof ZielFeld) {
					writer.append("" + ((ZielFeld) feld).getNumber());
				}
			}
			writer.newLine();
		}

		writer.append("END");

		writer.flush();
		writer.close();

	}
}
