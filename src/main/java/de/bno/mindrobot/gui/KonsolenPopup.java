package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class KonsolenPopup extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = -2967732009111903750L;
	private JMenu befehleMenu;
	private JMenu schleifenMenu;
	private JMenu verzweigungenMenu;
	private JMenuItem wennDann;
	private JMenuItem rechtsrum;
	private JMenuItem linksrum;
	private JMenuItem moveBack;
	private JMenuItem moveFront;
	private JMenuItem solangeWie;
	private JMenuItem repeatX;

	public KonsolenPopup() {
		super();
		createEntries();
	}

	private void createEntries() {

		createBefehle();
		createVerzweigunen();
		createSchleifen();

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

		} else if (arg0.getSource() == rechtsrum) {

		} else if (arg0.getSource() == linksrum) {

		} else if (arg0.getSource() == solangeWie) {

		} else if (arg0.getSource() == repeatX) {

		} else if (arg0.getSource() == moveBack) {

		} else if (arg0.getSource() == moveFront) {

		}

	}

}
