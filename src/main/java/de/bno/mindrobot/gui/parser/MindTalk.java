package de.bno.mindrobot.gui.parser;

import de.bno.mindrobot.gui.RobotControl;

public class MindTalk implements Parser {

	boolean running;

	@Override
	public void run(RobotControl ctrl, String script) {
		running = true;

		running = false;
	}

	@Override
	public void stop() {
		running = false;
	}

}
