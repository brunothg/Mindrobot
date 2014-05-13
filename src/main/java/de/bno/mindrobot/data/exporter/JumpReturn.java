package de.bno.mindrobot.data.exporter;

public class JumpReturn<R> {

	private int jump;
	private R returnValue;

	public JumpReturn(int jump, R returnValue) {
		this.jump = jump;
		this.returnValue = returnValue;
	}

	public int getJump() {
		return jump;
	}

	public R getReturnValue() {
		return returnValue;
	}

}
