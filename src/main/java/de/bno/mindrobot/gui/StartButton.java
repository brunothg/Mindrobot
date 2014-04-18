package de.bno.mindrobot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class StartButton extends JButton implements MouseListener,
		ActionListener {

	private static final long serialVersionUID = -5082126803481181344L;
	private boolean isMouseDown;
	private boolean isMouseIn;

	private BufferedImage offImg;

	public StartButton(String s) {
		super(s);
		setOpaque(false);
		isMouseDown = false;

		addActionListener(this);
		addMouseListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width * 3,
				super.getPreferredSize().height * 3);
	}

	@Override
	public void paintComponent(Graphics g) {

		if (offImg == null || offImg.getWidth() != getWidth()
				|| offImg.getHeight() != getHeight()) {
			offImg = new BufferedImage(getWidth(), getHeight(),
					BufferedImage.TYPE_INT_ARGB);
		}

		paintBig(offImg.createGraphics(), offImg.getWidth(), offImg.getHeight());

		g.drawImage(offImg, 0, 0, getWidth(), getHeight(), 0, 0,
				offImg.getWidth(), offImg.getHeight(), this);
	}

	private void paintBig(Graphics2D g, int width, int height) {
		g.setBackground(new Color(0, 0, 0, 0));
		g.clearRect(0, 0, width, height);
		if (isVisible()) {
			g.setColor(Color.BLACK);
			g.drawRoundRect(5, 5, width - 10, height - 10, 20, 20);

			if (!isMouseDown) {
				g.setColor(getColorWithAlpha(getBackground(), 150));
			} else {
				g.setColor(getColorWithAlpha(getBackground(), 255));
			}
			g.fillRoundRect(6, 6, width - 11, height - 11, 20, 20);

			char[] start = getText().toCharArray();

			Font f = getSizedFont(g, start);
			FontMetrics metrics = g.getFontMetrics(f);
			g.setFont(f);

			if (!isMouseIn) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(getColorWithAlpha(getForeground(), 255));
			}

			int fontWidth = metrics.charsWidth(start, 0, start.length);
			int fontHeight = metrics.getHeight();
			g.drawChars(start, 0, start.length, (width - fontWidth) / 2,
					(height + fontHeight) / 2);
		}
	}

	private Color getColorWithAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(),
				alpha);
	}

	private Font getSizedFont(Graphics g, char[] string) {
		Font f = new Font(g.getFont().getName(), Font.BOLD, getHeight() / 4);

		while ((g.getFontMetrics(f)).charsWidth(string, 0, string.length) > getWidth() - 15) {
			f = new Font(g.getFont().getName(), Font.BOLD, f.getSize() - 1);
		}
		return f;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		isMouseDown = false;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		isMouseIn = true;
		invalidate();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		isMouseIn = false;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		isMouseDown = false;
		isMouseIn = false;
	}
}
