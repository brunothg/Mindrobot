package de.bno.mindrobot.gui;

public class MindTalk implements Parser {

	boolean running;

	@Override
	public void run(RobotControl ctrl) {
		running = true;

		ctrl.moveForwards();
		ctrl.moveForwards();

		ctrl.turnRight();
		ctrl.moveForwards();
		ctrl.moveForwards();
		ctrl.moveBackwards();
		ctrl.moveBackwards();
		ctrl.moveBackwards();
		ctrl.moveBackwards();

		ctrl.turnRight();
		ctrl.moveBackwards();
		ctrl.moveBackwards();

		ctrl.moveForwards();
		ctrl.moveForwards();

		ctrl.turnRight();
		ctrl.moveBackwards();
		ctrl.moveBackwards();

		ctrl.turnLeft();
		ctrl.moveBackwards();
		ctrl.moveBackwards();

		if (running) {
			ctrl.turnRight();
			ctrl.moveBackwards();
			ctrl.moveBackwards();
			ctrl.moveForwards();
			ctrl.moveForwards();

			ctrl.turnLeft();
			ctrl.moveForwards();
			ctrl.moveForwards();

			ctrl.turnLeft();
			ctrl.moveForwards();
			ctrl.moveForwards();
			ctrl.moveBackwards();
		}
		running = false;
	}

	@Override
	public void stop() {
		running = false;
	}

}
