package de.bno.mindrobot.gui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class StringsTest {

	@Test
	public void writeAndReadStrings() throws Exception {

		Path dir = Paths.get("./test");
		if (!Files.exists(dir)) {
			Files.createDirectory(dir);
		}

		Path file = Paths.get("./test/values.txt");

		Strings.writeStrings(file);

		Strings.loadStrings(file);
	}

}
