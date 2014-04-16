package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.bno.mindrobot.MindRobot;

public class Konsole extends JPanel {

	private static final Logger LOG = MindRobot.getLogger(Konsole.class);

	private static final long serialVersionUID = -6470828939746541026L;

	private JLabel title;

	private JPanel topPanel;

	public Konsole() {
		super();

		setLayout(new BorderLayout());

		createTopPanel();
		createLabel();

	}

	private void createTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(topPanel, BorderLayout.NORTH);
	}

	private void createLabel() {
		title = new JLabel(String(EDIT));
		setTitleSize(20);
		topPanel.add(title);
	}

	public void setTitleSize(int size) {
		title.setFont(new Font(title.getFont().getName(), Font.BOLD, size));
	}

}
