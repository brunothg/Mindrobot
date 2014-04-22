package de.bno.mindrobot.data.importer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.feld.FeldTyp;
import de.bno.mindrobot.gui.Avatar;

public class CustomFileSkinImporter implements SkinImporter {

	private static final Logger LOG = MindRobot
			.getLogger(CustomFileSkinImporter.class);

	HashMap<FeldTyp, ImageIcon> images;
	HashMap<Integer, ImageIcon> goalImages;
	HashMap<Integer, ImageIcon> avatarImages;

	private String map;

	public CustomFileSkinImporter(String map) {
		this.map = map;

		images = new HashMap<FeldTyp, ImageIcon>();
		goalImages = new HashMap<Integer, ImageIcon>();
		avatarImages = new HashMap<Integer, ImageIcon>();
	}

	@Override
	public void setMap(String map) {
		this.map = map;

		images.clear();
		goalImages.clear();
		avatarImages.clear();
	}

	@Override
	public Image getImage(int goalNumber) {
		return getImageIcon(goalNumber).getImage();
	}

	public ImageIcon getImageIcon(int goalNumber) {
		ImageIcon ret = null;

		ret = goalImages.get(new Integer(goalNumber));

		if (ret == null) {
			ret = reloadCustomGoalImageIcon(goalNumber);
			goalImages.put(new Integer(goalNumber), ret);
		}

		if (ret == null) {
			ret = getImageIcon(FeldTyp.ZIEL);
			ret = paintNumberOnGoal(ret, goalNumber);
			goalImages.put(new Integer(goalNumber), ret);
		}

		return ret;
	}

	private ImageIcon reloadCustomGoalImageIcon(int goalNumber) {
		ImageIcon ret = null;

		Path dir = Paths.get(MindRobot.MAP_SEARCH_STRING, map
				+ MindRobot.IMAGE_SEARCH_STRING);

		if (Files.exists(dir) && Files.isDirectory(dir)) {

			try {
				DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir);
				for (Path file : dirStream) {
					boolean isGoalWithNumber = file.getFileName().toString()
							.indexOf("G" + goalNumber) != -1;
					if (Files.isReadable(file) && Files.isRegularFile(file)
							&& isGoalWithNumber) {
						ret = new ImageIcon(ImageIO.read(Files
								.newInputStream(file)));
						break;
					}
				}
			} catch (IOException e) {
				LOG.warning("Fehler beim Laden der Custom Tiles: "
						+ e.getMessage());
			}
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
			ret = reloadCustomImage(typ);
			images.put(typ, ret);
		}

		if (ret == null) {
			ret = reloadImage(typ);
			images.put(typ, ret);
		}

		return ret;
	}

	private ImageIcon reloadCustomImage(FeldTyp typ) {
		ImageIcon ret = null;

		Path dir = Paths.get(MindRobot.MAP_SEARCH_STRING, map
				+ MindRobot.IMAGE_SEARCH_STRING);

		if (Files.exists(dir) && Files.isDirectory(dir)) {

			try {
				DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir);
				for (Path file : dirStream) {
					boolean isImageOfTyp = file
							.getFileName()
							.toString()
							.indexOf(
									"Custom"
											+ Character.toUpperCase(typ
													.getCharRepresentation())) != -1;
					if (Files.isReadable(file) && Files.isRegularFile(file)
							&& isImageOfTyp) {
						ret = new ImageIcon(ImageIO.read(Files
								.newInputStream(file)));
						break;
					}
				}
			} catch (IOException e) {
				LOG.warning("Fehler beim Laden der Custom Tiles: "
						+ e.getMessage());
			}
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

		return ret;
	}

	@Override
	public Image getAvatar(int direction) {
		return getAvatarIcon(direction).getImage();
	}

	private ImageIcon getAvatarIcon(int direction) {

		if (!Avatar.isValidDirection(direction)) {
			return null;
		}

		ImageIcon ret = avatarImages.get(direction);

		if (ret == null) {
			ret = reloadCustomAvatar(direction);
			avatarImages.put(direction, ret);
		}

		if (ret == null) {
			ret = loadIcon("Avatar.png");
			ret = paintArrowOnAvatar(ret, direction);
			avatarImages.put(direction, ret);
		}

		return ret;
	}

	private ImageIcon reloadCustomAvatar(int direction) {
		LOG.info("Lade AvatarIcon " + direction);
		ImageIcon ret = null;

		Path dir = Paths.get(MindRobot.MAP_SEARCH_STRING, map
				+ MindRobot.IMAGE_SEARCH_STRING);

		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			System.out.println("1-------------- " + dir.toString() + " - "
					+ map);
			return null;
		}

		boolean paintArrows = true;

		try {
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir);
			for (Path file : dirStream) {
				boolean isAvatarWithDirecion = file.getFileName().toString()
						.indexOf("Avatar" + direction) != -1;
				boolean isAvatarForAnyDirection = file.getFileName().toString()
						.indexOf("AvatarAny") != -1;
				if (Files.isReadable(file) && Files.isRegularFile(file)
						&& (isAvatarWithDirecion || isAvatarForAnyDirection)) {
					ret = new ImageIcon(
							ImageIO.read(Files.newInputStream(file)));

					if (isAvatarWithDirecion) {
						paintArrows = false;
						break;
					}

				}
			}
		} catch (IOException e) {
			LOG.warning("Fehler beim Laden der Custom Tiles: " + e.getMessage());
		}

		if (paintArrows && ret != null) {
			ret = paintArrowOnAvatar(ret, direction);
		}

		return ret;
	}

	private ImageIcon paintArrowOnAvatar(ImageIcon img, int direction) {
		BufferedImage ret = new BufferedImage(img.getIconWidth(),
				img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) ret.getGraphics();
		g.drawImage(img.getImage(), 0, 0, ret.getWidth(), ret.getHeight(), 0,
				0, img.getIconWidth(), img.getIconHeight(),
				img.getImageObserver());

		int midX, midY;
		midX = ret.getWidth() / 2;
		midY = ret.getHeight() / 2;

		int arrowWidth = Math.min(ret.getHeight(), ret.getHeight()) / 6;

		g.setColor(new Color(255, 0, 0, 150));
		if (direction == Avatar.NORTH) {
			g.fillPolygon(new int[] { midX, midX - arrowWidth,
					midX + arrowWidth },
					new int[] { 0, arrowWidth, arrowWidth }, 3);
		} else if (direction == Avatar.SOUTH) {
			g.fillPolygon(new int[] { midX, midX - arrowWidth,
					midX + arrowWidth },
					new int[] { ret.getHeight(), ret.getHeight() - arrowWidth,
							ret.getHeight() - arrowWidth }, 3);
		} else if (direction == Avatar.EAST) {
			g.fillPolygon(new int[] { ret.getWidth(),
					ret.getWidth() - arrowWidth, ret.getWidth() - arrowWidth },
					new int[] { midY, midY - arrowWidth, midY + arrowWidth }, 3);
		} else if (direction == Avatar.WEST) {
			g.fillPolygon(new int[] { 0, arrowWidth, arrowWidth }, new int[] {
					midY, midY - arrowWidth, midY + arrowWidth }, 3);
		}

		return new ImageIcon(ret);
	}

	private ImageIcon loadIcon(String s) {
		return new ImageIcon(getClass().getClassLoader().getResource(
				"de/bno/mindrobot/gui/" + s));
	}

}
