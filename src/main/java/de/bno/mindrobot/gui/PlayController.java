package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.EDIT;
import static de.bno.mindrobot.gui.Strings.EXIT;
import static de.bno.mindrobot.gui.Strings.PLAY;
import static de.bno.mindrobot.gui.Strings.STOP;
import static de.bno.mindrobot.gui.Strings.String;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class PlayController extends JComponent implements ActionListener {

	private static final long serialVersionUID = 6957828122433592878L;
	private JButton btnOpen;
	private JPanel openedPanel;
	private JButton btnPlay;
	private JButton btnStop;
	private JButton btnEdit;
	private JButton btnExit;

	public PlayController() {
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		createOpenButton();
		createOpenedPanel();
	}

	private void createOpenedPanel() {

		openedPanel = new JPanel();
		openedPanel.setOpaque(false);
		openedPanel.setLayout(new BoxLayout(openedPanel, BoxLayout.X_AXIS));
		addPlayButton();
		addStopButton();
		addEditButton();
		addExitButton();
		add(openedPanel);
	}

	private void addExitButton() {
		btnExit = new JButton(String(EXIT));
		btnExit.addActionListener(this);
		openedPanel.add(btnExit);
	}

	private void addEditButton() {
		btnEdit = new JButton(String(EDIT));
		btnEdit.addActionListener(this);
		openedPanel.add(btnEdit);
	}

	private void addStopButton() {
		btnStop = new JButton(String(STOP));
		btnStop.addActionListener(this);
		openedPanel.add(btnStop);
	}

	private void addPlayButton() {
		btnPlay = new JButton(String(PLAY));
		btnPlay.addActionListener(this);
		openedPanel.add(btnPlay);
	}

	private void createOpenButton() {

		btnOpen = new JButton("<");
		btnOpen.addActionListener(this);
		add(btnOpen);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == btnPlay) {
			Signals.sendSignal(Signals.SIGNAL_PLAY_BTN);
		} else if (arg0.getSource() == btnStop) {
			Signals.sendSignal(Signals.SIGNAL_STOP_BTN);
		} else if (arg0.getSource() == btnEdit) {
			Signals.sendSignal(Signals.SIGNAL_EDIT_BTN);
		} else if (arg0.getSource() == btnExit) {
			Signals.sendSignal(Signals.SIGNAL_EXIT);
		} else if (arg0.getSource() == btnOpen) {
			btnOpen.setText((btnOpen.getText().equalsIgnoreCase(">")) ? "<"
					: ">");
			if (btnOpen.getText().equalsIgnoreCase(">")) {
				openedPanel.setVisible(false);
			} else {
				openedPanel.setVisible(true);
			}
		}

	}

}
