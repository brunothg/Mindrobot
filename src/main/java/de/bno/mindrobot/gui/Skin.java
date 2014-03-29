package de.bno.mindrobot.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class Skin {

	private String id;
	private BufferedImage img;
	
	public Skin(String id, String folder) throws IOException{
		Path pic = Paths.get(folder, id);
		BufferedImage img;
		
		img = ImageIO.read(Files.newInputStream(pic));
		
		this.id = id;
		this.img = img;
	}
	
	public Skin(String id, BufferedImage img){
		this.id = id;
		this.img = img;
	}
	
	public BufferedImage getImage(){
		return img;
	}

	public BufferedImage getImage(int width, int height, ImageObserver observer){
		
		BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = ret.getGraphics();
		
		g.drawImage(img, 0, 0, width, height, 0, 0, img.getWidth(), img.getHeight(), observer);
		
		return ret;
	}
	
	public BufferedImage getImage(double scale, ImageObserver observer){
		return getImage((int)(img.getWidth()*scale), (int)(img.getHeight()*scale), observer);
	}
	
	public BufferedImage getImage(int width, ImageObserver observer){
		int height = (int)(img.getHeight() * ((double)width/(double)img.getWidth()));
		
		return getImage(width, height, observer);
	}
	
	public String getId(){
		return id;
	}		
	
}
