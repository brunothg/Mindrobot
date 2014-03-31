package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.EXIT;
import static de.bno.mindrobot.gui.Strings.START;
import static de.bno.mindrobot.gui.Strings.String;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.bno.mindrobot.data.importer.FileSpielfeldImporter;

public class MainMenu extends JPanel implements ActionListener {

	private static final long serialVersionUID = 8596091572742102180L;

	private static final String MAP_SEARCH_STRING = "./maps";

	String[] maps;

	private GridBagLayout gbL;

	private StartButton exitButton;

	private StartButton startButton;

	private JComboBox<String> mapSelect;

	private Path mapSearchPath;

	public MainMenu() {

		maps = FileSpielfeldImporter.getSpielfelder(Paths.get("./maps"))
				.toArray(new String[0]);

		setOpaque(false);

		gbL = new GridBagLayout();
		setLayout(gbL);

		mapSearchPath = Paths.get(MAP_SEARCH_STRING);

		createExitButton();
		createStartButton();
		createMapSelect();

	}

	private void createMapSelect() {
		String[] values = getMaps();
		mapSelect = new JComboBox<String>(values);
		add(mapSelect, 0);
	}

	private String[] getMaps() {

		return FileSpielfeldImporter.getSpielfelder(mapSearchPath).toArray(
				new String[0]);
	}

	private void createStartButton() {
		startButton = new StartButton(String(START));
		startButton.setBackground(Color.LIGHT_GRAY);
		startButton.setForeground(Color.YELLOW);
		add(startButton, 1);
		startButton.addActionListener(this);
	}

	private void createExitButton() {
		exitButton = new StartButton(String(EXIT));
		exitButton.setBackground(new Color(255, 0, 0));
		exitButton.setForeground(Color.YELLOW);
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
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == exitButton) {
			Signals.sendSignal("exit");
		} else if (arg0.getSource() == startButton) {
			Object selectedMap = mapSelect.getSelectedItem();
			if (selectedMap != null) {
				Signals.sendSignal("start", selectedMap.toString());
			} else {
				Signals.sendSignal("start");
			}
		}

	}
}
