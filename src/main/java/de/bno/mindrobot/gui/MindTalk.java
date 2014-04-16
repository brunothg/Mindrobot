package de.bno.mindrobot.gui;

public class MindTalk implements Parser {

	boolean running;

	@Override
	public void run(RobotControl ctrl) {
		running = true;

		running = false;
	}

	@Override
	public void stop() {
		running = false;
	}

}
