package de.bno.mindrobot.data.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.bno.mindrobot.data.spielfeld.SpielfeldData;

public class FileSpielfeldExporter implements SpielfeldExporter {

	Path path;
	public static final String MIME_TYPE = "text/mindrobot";
	private static final Charset CHARSET = StandardCharsets.UTF_8;

	public FileSpielfeldExporter(Path path) {
		this.path = path;
	}

	public FileSpielfeldExporter(String path) {
		this(Paths.get(path));
	}

	@Override
	public void exportSpielfeld(SpielfeldData spielfeld) throws IOException {

		BufferedWriter writer = Files.newBufferedWriter(path, CHARSET);

		writer.append(MIME_TYPE + "\n");
		writer.append(spielfeld.getWidth() + ", " + spielfeld.getHeight()
				+ ":\n");

		for (int y = 0; y < spielfeld.getHeight(); y++) {
			for (int x = 0; x < spielfeld.getWidth(); x++) {
				writer.append(spielfeld.getFeld(x, y).toChar());
			}
			writer.append('\n');
		}

		writer.flush();
		writer.close();

	}

}
