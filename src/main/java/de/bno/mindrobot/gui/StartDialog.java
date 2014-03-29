package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.CANCEL;
import static de.bno.mindrobot.gui.Strings.LANGUAGE;
import static de.bno.mindrobot.gui.Strings.START_APP;
import static de.bno.mindrobot.gui.Strings.String;
import static de.bno.mindrobot.gui.Strings.TITLE;
import static de.bno.mindrobot.gui.Strings.TOOLTIP_SELECT_LANG;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.bno.mindrobot.MindRobot;

public class StartDialog extends JDialog implements ActionListener,
		ItemListener {

	private static final String LANG_DIR = "./lang";
	private static final long serialVersionUID = -8806641182590281978L;
	private static final Logger LOG = MindRobot.getLogger(StartDialog.class);
	private static final String DEF_LAN = "Default";
	private JLabel logo;
	private JPanel buttonsPanel;
	private JButton okButton;
	private JButton cancelButton;
	private JPanel mainPanel;
	private JPanel settingsPanel;
	private GridBagLayout gridBagLayout;
	private JComboBox<String> selectLang;
	private JLabel langLabel;

	private StartDialog() {
		super();
		setTitle(String(TITLE));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);
		setIcon();
		setLayout(new BorderLayout(5, 5));

		createLogoLabel();
		createMainPanel();
		createButtonsPanel();
		createSettingsPanel();

	}

	private void createSettingsPanel() {

		settingsPanel = new JPanel();
		gridBagLayout = new GridBagLayout();
		settingsPanel.setLayout(gridBagLayout);
		mainPanel.add(settingsPanel, BorderLayout.CENTER);

		addLanguageRow();

	}

	private void addLanguageRow() {
		String[] langs = getLanguageStrings();
		selectLang = new JComboBox<String>(langs);
		selectLang.setToolTipText(String.format(String(TOOLTIP_SELECT_LANG),
				LANG_DIR));
		selectLang.addItemListener(this);

		langLabel = new JLabel(String(LANGUAGE));
		addRow(langLabel, selectLang, 0);
	}

	private String[] getLanguageStrings() {

		List<String> list = new LinkedList<String>();

		list.add(DEF_LAN);

		Path dir = Paths.get(LANG_DIR);

		if (Files.exists(dir) && Files.isDirectory(dir)) {

			LOG.info("Versuche weitere Sprachen zu laden. Suche in "
					+ dir.toString());

			try {
				DirectoryStream<Path> files = Files.newDirectoryStream(dir);
				for (Path file : files) {
					if (Files.isReadable(file) && Files.isRegularFile(file)) {

						LOG.info("Weitere Sprache gefunden: "
								+ file.getName(file.getNameCount() - 1)
										.toString());
						list.add(file.getName(file.getNameCount() - 1)
								.toString());

					}
				}
			} catch (IOException e) {
				LOG.warning("Fehler beim Lesen der Sprachpakete: "
						+ e.getMessage());
			}

		}

		String[] ret;

		ret = list.toArray(new String[0]);

		return ret;
	}

	private void createMainPanel() {

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		add(mainPanel, BorderLayout.CENTER);

	}

	private void createButtons() {

		createOKButton();
		createCancelButton();

	}

	private void createCancelButton() {

		cancelButton = new JButton(String(CANCEL));
		cancelButton.addActionListener(this);
		buttonsPanel.add(cancelButton);

	}

	private void createOKButton() {

		okButton = new JButton(String(START_APP));
		okButton.addActionListener(this);
		buttonsPanel.add(okButton);

	}

	private void createButtonsPanel() {

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		createButtons();
	}

	private void createLogoLabel() {
		logo = new JLabel();
		logo.setIcon(loadIcon("Logo.png"));
		logo.setOpaque(true);
		logo.setBackground(Color.BLACK);
		add(logo, BorderLayout.WEST);
	}

	private void addRow(JComponent title, JComponent comp, int row) {

		GridBagConstraints gbc1 = new GridBagConstraints();
		GridBagConstraints gbc2 = new GridBagConstraints();

		gbc1.gridx = 0;
		gbc1.gridy = row;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.ipadx = 5;
		gbc1.weightx = 0;

		gbc2.gridx = 1;
		gbc2.gridy = row;
		gbc2.gridwidth = 1;
		gbc2.gridheight = 1;
		gbc2.weightx = 1;
		gbc2.ipadx = 5;
		gbc2.fill = GridBagConstraints.HORIZONTAL;

		gridBagLayout.setConstraints(title, gbc1);
		settingsPanel.add(title);

		gridBagLayout.setConstraints(comp, gbc2);
		settingsPanel.add(comp);
	}

	private void setIcon() {
		try {
			setIconImage(loadIcon("MindRobot.png").getImage());
		} catch (Exception e) {
			LOG.warning("Beim Startdialog konnte das Icon nicht erzeugt werden. Cause: "
					+ e.getMessage());
		}
	}

	private ImageIcon loadIcon(String s) {
		return new ImageIcon(getClass().getClassLoader().getResource(
				"de/bno/mindrobot/gui/" + s));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {

		} else if (e.getSource() == cancelButton) {
			LOG.info("Close App");
			dispose();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		if (e.getSource() == selectLang) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (selectLang.getSelectedIndex() == 0
						|| selectLang.getSelectedItem().toString()
								.equals(DEF_LAN)) {
					LOG.info("Lade Default Sprache.");
					Strings.loadDefault();
				} else {
					Path file = Paths.get(LANG_DIR, selectLang
							.getSelectedItem().toString());

					LOG.info("Lade neue Sprache: "
							+ selectLang.getSelectedItem().toString());
					try {
						Strings.loadStrings(file);
					} catch (IOException e1) {
						LOG.warning("Fehler beim Umschalten der Sprache.");
					}
				}
				updateStrings();
			}
		}

	}

	private void updateStrings() {
		setTitle(String(TITLE));
		selectLang.setToolTipText(String.format(String(TOOLTIP_SELECT_LANG),
				LANG_DIR));
		langLabel.setText(String(LANGUAGE));
		cancelButton.setText(String(CANCEL));
		okButton.setText(String(START_APP));
	}

	public static void start() {

		StartDialog start = new StartDialog();
		start.setSize(500, 200);
		start.setVisible(true);

	}

}
