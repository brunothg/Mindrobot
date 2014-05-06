package de.bno.mindrobot.data.exporter;

import static de.bno.mindrobot.gui.Strings.BEENDET;
import static de.bno.mindrobot.gui.Strings.String;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.gui.Signals;

public class JavaExporter implements ScriptExporter {

	private static final Logger LOG = MindRobot.getLogger(JavaExporter.class);

	public void exportAs(String s, Path out) throws IOException {

		if (s == null || out == null) {
			return;
		}

		LOG.info("JavaExport -> " + out.toString());
		Signals.sendSignal(Signals.SIGNAL_KONSOLE_LOG,
				String.format("JavaExport -> %s%n", out.toString()));

		if (!Files.exists(out)) {
			Files.createFile(out);
		}

		String exportstring = s;

		if (!s.isEmpty()) {
			exportstring = parse(s);
		}

		OutputStream outs = Files.newOutputStream(out);

		outs.write(exportstring.getBytes("UTF-8"));

		outs.close();

		Signals.sendSignal(Signals.SIGNAL_KONSOLE_LOG,
				String.format("JavaExport -> %s&n", String(BEENDET)));
	}

	private String parse(String s) {
		// TODO Auto-generated method stub
		return s;
	}

}
