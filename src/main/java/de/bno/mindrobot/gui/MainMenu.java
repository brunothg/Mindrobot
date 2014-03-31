package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.EXIT;
import static de.bno.mindrobot.gui.Strings.START;
import static de.bno.mindrobot.gui.Strings.String;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.bno.mindrobot.data.importer.FileSpielfeldImporter;

public class MainMenu extends JPanel implements ActionListener, SignalListener {

	private static final long serialVersionUID = 8596091572742102180L;

	String[] maps;

	private GridBagLayout gbL;

	private JButton exitButton;

	private JButton startButton;

	public MainMenu() {

		maps = FileSpielfeldImporter.getSpielfelder(Paths.get("./maps"))
				.toArray(new String[0]);

		setOpaque(false);

		gbL = new GridBagLayout();
		setLayout(gbL);

		createExitButton();
		createStartButton();

		Signals.addListener(this);
	}

	private void createStartButton() {
		startButton = new StartButton(String(START));
		add(startButton, 1);
		startButton.addActionListener(this);
	}

	private void createExitButton() {
		exitButton = new JButton(String(EXIT));
		add(exitButton, 3);
		exitButton.addActionListener(this);
	}

	private void add(JComponent comp, int index) {

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 1;
		gbc.gridy = index;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10);

		gbL.setConstraints(comp, gbc);
		add(comp);

	}

	@Override
	public boolean Signal(String signal, Object... values) {
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == exitButton) {
			Signals.sendSignal("exit");
		}

	}

}
