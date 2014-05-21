package de.bno.mindrobot.gui;

import java.util.Vector;
import java.util.logging.Logger;

import de.bno.mindrobot.MindRobot;

public class Signals {

	private static final Logger LOG = MindRobot.getLogger(Signals.class);

	public static final String SIGNAL_PLAY_BTN = "playbutton";
	public static final String SIGNAL_STOP_BTN = "stopbutton";
	public static final String SIGNAL_EDIT_BTN = "editbutton";
	public static final String SIGNAL_EXIT = "exit";
	public static final String SIGNAL_START = "start";
	public static final String SIGNAL_MENU = "mainmenu";
	public static final String SIGNAL_FINISHED = "finished";
	public static final String SIGNAL_RUN_FINISHED = "run_finfished";
	public static final String SIGNAL_KONSOLE_INSERT = "konsole_insert";
	public static final String SIGNAL_KONSOLE_LOG = "konsole_log";
	public static final String SIGNAL_EXPORT_AS = "exp_as";
	public static final String SIGNAL_SET_WAIT = "set_wait";

	private static Vector<SignalListener> listener;

	static {
		listener = new Vector<SignalListener>();
	}

	private Signals() {

	}

	public static void sendSignal(String signal, final Object... values) {
		LOG.info("Signal: " + signal + " -> " + toArgString(values));
		for (int i = 0; i < Signals.listener.size(); i++) {
			SignalListener lis = Signals.listener.get(i);
			if (lis.Signal(signal, values)) {
				break;
			}
		}
	}

	private static String toArgString(Object[] values) {
		String ret = "";

		if (values == null) {
			ret = null;
		} else if (values.length == 0) {
			ret = "<Empty>";
		} else {
			for (int i = 0; i < values.length; i++) {

				ret += (values[i] == null) ? "<NULL>" : ("'"
						+ values[i].toString() + "'");
				if (i < values.length - 1) {
					ret += ", ";
				}
			}
		}

		return ret;
	}

	public static void addListener(SignalListener listener) {
		Signals.listener.add(listener);
	}

	public static void removeListener(SignalListener listener) {
		Signals.listener.remove(listener);
	}

}
