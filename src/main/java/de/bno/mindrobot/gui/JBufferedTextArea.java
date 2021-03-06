package de.bno.mindrobot.gui;

import javax.swing.JTextArea;

public class JBufferedTextArea extends JTextArea {

	private static final long serialVersionUID = 5868405903766318209L;

	private int buffersize;

	public JBufferedTextArea(int buffersize) {
		this.buffersize = buffersize;
	}

	public JBufferedTextArea() {
		this(5000);
	}

	@Override
	public void setText(String s) {
		if (s.length() <= buffersize) {
			super.setText(s);
		} else {
			super.setText(s.substring(0, buffersize - 1));
		}
	}

	public void appendText(String s) {
		setText(s + getText());
		setCaretPosition(0);
	}

}
