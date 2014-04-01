package de.bno.mindrobot.data.importer;

import java.awt.Image;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.feld.FeldTyp;

public class CustomFileSkinImporter implements SkinImporter {

	private static final Logger LOG = MindRobot
			.getLogger(CustomFileSkinImporter.class);

	HashMap<FeldTyp, Image> images;
	HashMap<Integer, Image> goalImages;

	public CustomFileSkinImporter() {
		images = new HashMap<FeldTyp, Image>();
		goalImages = new HashMap<Integer, Image>();
	}

	@Override
	public Image getImage(int goalNumber) {
		Image ret = null;

		ret = getImage(FeldTyp.ZIEL);

		return ret;
	}

	@Override
	public Image getImage(FeldTyp typ) {
		Image ret = images.get(typ);

		if (ret == null) {
			ret = reloadImage(typ);
		}

		return ret;
	}

	private Image reloadImage(FeldTyp typ) {
		LOG.info("Lade Feld Tile: " + typ.toString());

		Image ret = null;

		if (typ == FeldTyp.CONFUSE) {
			ret = loadIcon("Floor_Confuse.jpg").getImage();
		} else if (typ == FeldTyp.BLOCKED) {
			ret = loadIcon("Floor_Closed.jpg").getImage();
		} else if (typ == FeldTyp.ZIEL) {
			ret = loadIcon("Floor_Goal.jpg").getImage();
		} else {
			ret = loadIcon("Floor_Normal.jpg").getImage();
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
