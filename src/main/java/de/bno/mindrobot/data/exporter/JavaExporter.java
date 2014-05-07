package de.bno.mindrobot.data.exporter;

import static de.bno.mindrobot.gui.Strings.BEENDET;
import static de.bno.mindrobot.gui.Strings.String;

import java.io.IOException;
import java.io.InputStream;
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

		String exportstring;
		String javastring = s;

		if (!s.isEmpty()) {
			javastring = parse(s);
		}

		exportstring = String.format(loadDefault("rahmen_java"), javastring);

		OutputStream outs = Files.newOutputStream(out);

		outs.write(exportstring.getBytes("UTF-8"));

		outs.close();

		Signals.sendSignal(Signals.SIGNAL_KONSOLE_LOG,
				String.format("JavaExport -> %s%n", String(BEENDET)));
	}

	private String loadDefault(String name) {

		String ret = "";

		InputStream in = getClass().getClassLoader().getResourceAsStream(
				"de/bno/mindrobot/data/exporter/" + name);

		byte[] buf = new byte[20];

		int num = -1;

		try {
			while ((num = in.read(buf)) != -1) {
				ret += new String(buf, 0, num, "UTF-8");
			}
		} catch (IOException e) {
			ret = "%s";
		}

		return ret;
	}

	private String parse(String source) {
		// TODO parse to java code
		return source;
	}

}
