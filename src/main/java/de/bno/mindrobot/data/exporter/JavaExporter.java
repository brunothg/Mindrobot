package de.bno.mindrobot.data.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaExporter implements ScriptExporter {

	public void exportAs(String s, Path out) throws IOException {

		if (s == null || out == null || s.isEmpty()) {
			return;
		}

		if (!Files.exists(out)) {
			Files.createFile(out);
		}

		String exportstring = s;

		// TODO: konvertieren

		OutputStream outs = Files.newOutputStream(out);

		outs.write(exportstring.getBytes("UTF-8"));

		outs.close();
	}

}
