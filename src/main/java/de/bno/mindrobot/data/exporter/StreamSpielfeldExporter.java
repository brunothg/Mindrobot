package de.bno.mindrobot.data.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.feld.Feld;
import de.bno.mindrobot.feld.StartFeld;
import de.bno.mindrobot.feld.ZielFeld;

public class StreamSpielfeldExporter implements SpielfeldExporter {

	public static final String MIME_TYPE = "text/mindrobot";
	public static final Charset CHARSET = StandardCharsets.UTF_8;

	OutputStream out;

	public StreamSpielfeldExporter(OutputStream out) {
		this.out = out;
	}

	@Override
	public void exportSpielfeld(SpielfeldData spielfeld) throws IOException {

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,
				CHARSET));

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
				} else if (feld instanceof StartFeld) {
					writer.append("" + ((StartFeld) feld).getDirection());
				}
			}
			writer.newLine();
		}

		writer.append("END");

		writer.flush();
		out.flush();
		out.close();
		writer.close();
	}

}
