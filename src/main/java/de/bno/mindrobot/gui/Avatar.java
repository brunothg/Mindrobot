package de.bno.mindrobot.gui;

public class Avatar {

	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public static final int NORTH = 4;

	public static boolean isValidDirection(int direction) {
		if (direction == EAST || direction == WEST || direction == SOUTH
				|| direction == NORTH) {
			return true;
		}

		return false;
	}

	public static int leftOf(int direction) {
		int ret = direction;

		if (direction == EAST) {
			ret = NORTH;
		} else if (direction == NORTH) {
			ret = WEST;
		} else if (direction == WEST) {
			ret = SOUTH;
		} else if (direction == SOUTH) {
			ret = EAST;
		}

		return ret;
	}

	public static int rightOf(int direction) {
		int ret = direction;

		if (direction == EAST) {
			ret = SOUTH;
		} else if (direction == NORTH) {
			ret = EAST;
		} else if (direction == WEST) {
			ret = NORTH;
		} else if (direction == SOUTH) {
			ret = WEST;
		}

		return ret;
	}

	public static Location fieldInFront(Location location, int direction) {
		if (!isValidDirection(direction)) {
			return null;
		}

		Location ret = null;

		int x = 0, y = 0;

		if (direction == NORTH) {
			y = -1;
		} else if (direction == SOUTH) {
			y = +1;
		} else if (direction == WEST) {
			x = -1;
		} else if (direction == EAST) {
			x = +1;
		}

		ret = new Location(location.getX() + x, location.getY() + y);

		return ret;
	}

	public static Location fieldBehind(Location location, int direction) {
		if (!isValidDirection(direction)) {
			return null;
		}

		Location ret = null;

		int x = 0, y = 0;

		if (direction == NORTH) {
			y = +1;
		} else if (direction == SOUTH) {
			y = -1;
		} else if (direction == WEST) {
			x = +1;
		} else if (direction == EAST) {
			x = -1;
		}

		ret = new Location(location.getX() + x, location.getY() + y);

		return ret;
	}
}
