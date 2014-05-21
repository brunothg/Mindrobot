package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.EXPORT;
import static de.bno.mindrobot.gui.Strings.JAVA;
import static de.bno.mindrobot.gui.Strings.MENU_BEFEHL;
import static de.bno.mindrobot.gui.Strings.MENU_FRAGE;
import static de.bno.mindrobot.gui.Strings.MENU_FRAGE_HINDERNIS;
import static de.bno.mindrobot.gui.Strings.MENU_FRAGE_VERWIRRT;
import static de.bno.mindrobot.gui.Strings.MENU_GESCHWINDIGKEIT;
import static de.bno.mindrobot.gui.Strings.MENU_LINKSDREHEN;
import static de.bno.mindrobot.gui.Strings.MENU_RECHTSDREHEN;
import static de.bno.mindrobot.gui.Strings.MENU_RUECKWAERTS;
import static de.bno.mindrobot.gui.Strings.MENU_SCHLEIFE;
import static de.bno.mindrobot.gui.Strings.MENU_SOLANGEWIE;
import static de.bno.mindrobot.gui.Strings.MENU_VERZWEIGUNG;
import static de.bno.mindrobot.gui.Strings.MENU_VORWAERTS;
import static de.bno.mindrobot.gui.Strings.MENU_WIEDERHOLUNG;
import static de.bno.mindrobot.gui.Strings.SYNTAX_DANN;
import static de.bno.mindrobot.gui.Strings.SYNTAX_WENN;
import static de.bno.mindrobot.gui.Strings.String;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class KonsolenPopup extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = -2967732009111903750L;

	private JMenu verzweigungenMenu;
	private JMenuItem wennDann;

	private JMenu befehleMenu;
	private JMenuItem rechtsrum;
	private JMenuItem linksrum;
	private JMenuItem moveBack;
	private JMenuItem moveFront;
	private JMenuItem setSpeed;

	private JMenu schleifenMenu;
	private JMenuItem solangeWie;
	private JMenuItem repeatX;

	private JMenu fragenMenu;
	private JMenuItem binVerwirrt;
	private JMenuItem binHindernis;

	private JMenu exportMenu;
	private JMenuItem expJava;

	public KonsolenPopup() {
		super();
		createEntries();
	}

	private void createEntries() {

		createBefehle();
		createFragen();
		createVerzweigunen();
		createSchleifen();
		createExport();
	}

	private void createExport() {
		exportMenu = new JMenu(String(EXPORT));
		add(exportMenu);

		createJavaExport();
	}

	private void createJavaExport() {
		expJava = new JMenuItem(String(JAVA));
		exportMenu.add(expJava);
		expJava.addActionListener(this);
	}

	private void createFragen() {
		fragenMenu = new JMenu(String(MENU_FRAGE));
		add(fragenMenu);

		createHindernisFrage();
		createVerwirrtFrage();
	}

	private void createVerwirrtFrage() {
		binVerwirrt = new JMenuItem(String(MENU_FRAGE_VERWIRRT));
		fragenMenu.add(binVerwirrt);
		binVerwirrt.addActionListener(this);
	}

	private void createHindernisFrage() {
		binHindernis = new JMenuItem(String(MENU_FRAGE_HINDERNIS));
		fragenMenu.add(binHindernis);
		binHindernis.addActionListener(this);
	}

	private void createSchleifen() {
		schleifenMenu = new JMenu(String(MENU_SCHLEIFE));
		add(schleifenMenu);

		createWiederholung();
		createSolangewie();
	}

	private void createSolangewie() {
		solangeWie = new JMenuItem(String(MENU_SOLANGEWIE));
		schleifenMenu.add(solangeWie);
		solangeWie.addActionListener(this);
	}

	private void createWiederholung() {
		repeatX = new JMenuItem(String(MENU_WIEDERHOLUNG));
		schleifenMenu.add(repeatX);
		repeatX.addActionListener(this);
	}

	private void createVerzweigunen() {
		verzweigungenMenu = new JMenu(String(MENU_VERZWEIGUNG));
		add(verzweigungenMenu);

		createWennDann();
	}

	private void createWennDann() {
		wennDann = new JMenuItem(String.format("%s ...? %s ...",
				String(SYNTAX_WENN), String(SYNTAX_DANN)));
		verzweigungenMenu.add(wennDann);
		wennDann.addActionListener(this);
	}

	private void createBefehle() {
		befehleMenu = new JMenu(String(MENU_BEFEHL));
		add(befehleMenu);

		createVorwaerts();
		createRueckwaerts();
		createLinksDrehen();
		createRechtsDrehen();
		createSetzeGeschwindigkeit();

	}

	private void createSetzeGeschwindigkeit() {
		setSpeed = new JMenuItem(String(MENU_GESCHWINDIGKEIT));
		befehleMenu.add(setSpeed);
		setSpeed.addActionListener(this);
	}

	private void createRechtsDrehen() {
		rechtsrum = new JMenuItem(String(MENU_RECHTSDREHEN));
		befehleMenu.add(rechtsrum);
		rechtsrum.addActionListener(this);
	}

	private void createLinksDrehen() {
		linksrum = new JMenuItem(String(MENU_LINKSDREHEN));
		befehleMenu.add(linksrum);
		linksrum.addActionListener(this);
	}

	private void createRueckwaerts() {
		moveBack = new JMenuItem(String(MENU_RUECKWAERTS));
		befehleMenu.add(moveBack);
		moveBack.addActionListener(this);
	}

	private void createVorwaerts() {
		moveFront = new JMenuItem(String(MENU_VORWAERTS));
		befehleMenu.add(moveFront);
		moveFront.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == wennDann) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT,
					Insert.WENN_DANN_SONST);
		} else if (arg0.getSource() == rechtsrum) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT, Insert.RECHTS);
		} else if (arg0.getSource() == linksrum) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT, Insert.LINKS);
		} else if (arg0.getSource() == solangeWie) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT,
					Insert.SOLANGE_WIE);
		} else if (arg0.getSource() == repeatX) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT,
					Insert.WIEDERHOLE_X);
		} else if (arg0.getSource() == moveBack) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT,
					Insert.RUECKWAERTS);
		} else if (arg0.getSource() == moveFront) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT, Insert.VORWAERTS);
		} else if (arg0.getSource() == binHindernis) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT,
					Insert.HINDERNIS_Q);
		} else if (arg0.getSource() == binVerwirrt) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT, Insert.VERWIRRT_Q);
		} else if (arg0.getSource() == expJava) {
			Signals.sendSignal(Signals.SIGNAL_EXPORT_AS, "java");
		} else if (arg0.getSource() == setSpeed) {
			Signals.sendSignal(Signals.SIGNAL_KONSOLE_INSERT,
					Insert.SETZE_GESCHWINDIGKEIT);
		}

	}

}
