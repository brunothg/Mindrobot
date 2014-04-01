package de.bno.mindrobot.data.importer;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

import de.bno.mindrobot.feld.FeldTyp;

public class CustomFileSkinImporter implements SkinImporter {

	HashMap<FeldTyp, Image> images;

	public CustomFileSkinImporter() {
		images = new HashMap<FeldTyp, Image>();
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
