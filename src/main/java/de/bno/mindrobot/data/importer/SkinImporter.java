package de.bno.mindrobot.data.importer;

import java.awt.Image;

import de.bno.mindrobot.feld.FeldTyp;

public interface SkinImporter {

	public Image getImage(FeldTyp typ);

	public Image getAvatar(int direction);

}
