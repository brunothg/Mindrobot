package de.bno.mindrobot.gui;

public interface RobotControl
{

	/**
	 * Turn robot 45° anti clockwise
	 */
	public void turnLeft();

	/**
	 * Turn robot 45° clockwise
	 */
	public void turnRight();

	/**
	 * Move robot one field forwards
	 * 
	 * @return true if this action was successful
	 */
	public boolean moveForwards();

	/**
	 * Move robot one field backwards
	 * 
	 * @return true if this action was successful
	 */
	public boolean moveBackwards();

	/**
	 * Is a blocked field in front of the robot
	 * 
	 * @return true if there is a blocked field in front
	 */
	public boolean isBlockedFieldInFront();

	/**
	 * If the field in front, if there is one, is accessible
	 * 
	 * @return true if the field in front is accessible
	 */
	public boolean isFieldInFrontAccessible();

	/**
	 * Is a confusing field under the robot
	 * 
	 * @return true if there is a confusing field
	 */
	public boolean standOnConfusingField();

	/**
	 * Is the robot confused
	 * 
	 * @return true if the robot is confused
	 */
	public boolean isConfused();

	/**
	 * If the robot stands on a goal field and which goal.
	 * 
	 * @return number of goal or -1 if not a goal
	 */
	public int standOnGoalField();

	/**
	 * Returns the number of the last goal field. Can be used with actual goal field to check
	 * finished state.
	 * 
	 * @return number of last goal
	 */
	public int getLastGoalNumber();

}
