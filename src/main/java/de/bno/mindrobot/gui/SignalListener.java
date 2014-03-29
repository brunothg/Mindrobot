package de.bno.mindrobot.gui;

public interface SignalListener {

	public void Signal(String signal, Object... values);

	public boolean consumeSignal(String signal);

}
