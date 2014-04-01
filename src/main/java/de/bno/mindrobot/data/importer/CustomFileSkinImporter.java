package de.bno.mindrobot.data.importer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.feld.FeldTyp;

public class CustomFileSkinImporter implements SkinImporter {

	private static final Logger LOG = MindRobot
			.getLogger(CustomFileSkinImporter.class);

	HashMap<FeldTyp, ImageIcon> images;
	HashMap<Integer, ImageIcon> goalImages;

	public CustomFileSkinImporter() {
		images = new HashMap<FeldTyp, ImageIcon>();
		goalImages = new HashMap<Integer, ImageIcon>();
	}

	@Override
	public Image getImage(int goalNumber) {
		return getImageIcon(goalNumber).getImage();
	}

	public ImageIcon getImageIcon(int goalNumber) {
		ImageIcon ret = null;

		if (ret == null) {
			ret = getImageIcon(FeldTyp.ZIEL);
			ret = paintNumberOnGoal(ret, goalNumber);
		}

		return ret;
	}

	private ImageIcon paintNumberOnGoal(final ImageIcon img, int number) {
		BufferedImage ret = new BufferedImage(img.getIconWidth(),
				img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) ret.getGraphics();
		g.drawImage(img.getImage(), 0, 0, ret.getWidth(), ret.getHeight(), 0,
				0, img.getIconWidth(), img.getIconHeight(),
				img.getImageObserver());

		int size = ret.getHeight() / 2;

		g.setFont(new Font(g.getFont().getName(), Font.BOLD, size));
		FontMetrics fm = g.getFontMetrics();

		char[] numberChar = ("" + number).toCharArray();
		int numHeight = fm.getHeight();
		int numWidth = fm.charsWidth(numberChar, 0, numberChar.length);

		while (numWidth > ret.getWidth()) {
			g.setFont(new Font(g.getFont().getName(), Font.BOLD, g.getFont()
					.getSize() - 1));
			fm = g.getFontMetrics();
			numWidth = fm.charsWidth(numberChar, 0, numberChar.length);
			numHeight = fm.getHeight();
		}

		int numX = (int) ((ret.getWidth() - numWidth) / 2.0);
		int numY = (int) ((ret.getHeight() + numHeight) / 2.0);

		g.setColor(Color.RED);
		System.out.println(new String(numberChar) + " " + numX + " " + numY);
		g.drawChars(numberChar, 0, numberChar.length, numX, numY);

		return new ImageIcon(ret);
	}

	@Override
	public Image getImage(FeldTyp typ) {
		return getImageIcon(typ).getImage();
	}

	public ImageIcon getImageIcon(FeldTyp typ) {
		ImageIcon ret = images.get(typ);

		if (ret == null) {
			ret = reloadImage(typ);
		}

		return ret;
	}

	private ImageIcon reloadImage(FeldTyp typ) {
		LOG.info("Lade Feld Tile: " + typ.toString());

		ImageIcon ret = null;

		if (typ == FeldTyp.CONFUSE) {
			ret = loadIcon("Floor_Confuse.jpg");
		} else if (typ == FeldTyp.BLOCKED) {
			ret = loadIcon("Floor_Closed.jpg");
		} else if (typ == FeldTyp.ZIEL) {
			ret = loadIcon("Floor_Goal.jpg");
		} else {
			ret = loadIcon("Floor_Normal.jpg");
		}

		images.put(typ, ret);

		return ret;
	}

	@Override
	public Image getAvatar(int direction) {
		return null;
	}

	private ImageIcon loadIcon(String s) {
		return new ImageIcon(getClass().getClassLoader().getResource(
				"de/bno/mindrobot/gui/" + s));
	}

}
