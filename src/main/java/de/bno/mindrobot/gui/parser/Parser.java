package de.bno.mindrobot.gui.parser;

import de.bno.mindrobot.gui.RobotControl;

public interface Parser
{

	public void run(RobotControl ctrl, String script);

	public void stop();

	public double getPoints();
}
