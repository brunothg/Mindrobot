package de.bno.mindrobot.gui;

import java.util.Vector;

public class Signals {

	private static Vector<SignalListener> listener;

	static {
		listener = new Vector<SignalListener>();
	}

	private Signals() {

	}

	public static void sendSignal(String signal, final Object... values) {
		for (int i = 0; i < Signals.listener.size(); i++) {
			SignalListener lis = Signals.listener.get(i);
			if (lis.Signal(signal, values)) {
				break;
			}
		}
	}

	public static void addListener(SignalListener listener) {
		Signals.listener.add(listener);
	}

	public static void removeListener(SignalListener listener) {
		Signals.listener.remove(listener);
	}

}
