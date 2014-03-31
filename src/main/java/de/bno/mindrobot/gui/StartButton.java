package de.bno.mindrobot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class StartButton extends JButton implements MouseListener {

	private static final long serialVersionUID = -5082126803481181344L;
	private boolean isMouseDown;
	private boolean isMouseIn;

	private Font f;

	public StartButton(String s) {
		super(s);
		setOpaque(false);
		isMouseDown = false;

		addMouseListener(this);
		loadFont();
	}

	private void loadFont() {
		f = null;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width * 3,
				super.getPreferredSize().height * 3);
	}

	@Override
	public void paint(Graphics g) {

		if (isVisible()) {
			g.setColor(Color.BLACK);
			g.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);

			if (!isMouseDown) {
				g.setColor(new Color(0, 150, 0, 150));
			} else {
				g.setColor(new Color(0, 150, 0, 255));
			}
			g.fillRoundRect(6, 6, getWidth() - 11, getHeight() - 11, 20, 20);

			if (f != null) {
				g.setFont(f);
			}

			if (!isMouseIn) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.RED);
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		isMouseIn = true;
		invalidate();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		isMouseIn = true;
		invalidate();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		isMouseDown = true;
		invalidate();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		isMouseDown = false;
		invalidate();
	}
}
