package de.bno.mindrobot.data.exporter;

import java.io.IOException;
import java.nio.file.Path;

public interface ScriptExporter {
	public void exportAs(String script, Path output) throws IOException;
}
