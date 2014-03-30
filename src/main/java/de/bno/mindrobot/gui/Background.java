package de.bno.mindrobot.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Background extends JPanel {

	private static final long serialVersionUID = -6304621603865587841L;

	Image bg;

	public Background() {

		bg = loadIcon("Sky.jpg").getImage();
		setOpaque(false);
	}

	private void superPaint(Graphics g) {
		super.paint(g);
	}

	@Override
	public void paint(Graphics g) {

		g.drawImage(bg, 0, 0, getWidth(), getHeight(), 0, 0, bg.getWidth(this),
				bg.getHeight(this), this);

		superPaint(g);
	}

	private ImageIcon loadIcon(String s) {
		return new ImageIcon(getClass().getClassLoader().getResource(
				"de/bno/mindrobot/gui/" + s));
	}
}
