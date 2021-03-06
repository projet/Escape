package game;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;


/**
 * This class represents our Ressources, class, which load files and images by a classloader.
 * 
 * @author Quentin Bernard et Ludovic Feltz
 */


/* <This program is an Shoot Them up space game, called Escape-IR, made by IR students.>
 *  Copyright (C) <2012>  <BERNARD Quentin & FELTZ Ludovic>

 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

public class Ressources {
	
	/**
	 * We use a map for avoid to initialize the same image twice.
	 */
	private static final Map<String, BufferedImage> images = new Hashtable<>();
	
	/**
	 * Load a BufferedImage;
	 * @param fileName Image to be loaded (must be in a package)
	 * @param optimized Set the optimisation, usefull for big image without transparency (default false)
	 * @return BufferedImage
	 */
	public static BufferedImage getImage(String fileName, boolean optimized){
		BufferedImage image = null;
		String filePath = Variables.IMAGES_URL + fileName;
		
		if(images.containsKey(filePath)){
			return images.get(filePath);
		}
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(filePath);
		try {
			image = ImageIO.read(input);
		} catch (IllegalArgumentException | IOException e) {
			System.out.println("Can't read file: " + filePath + System.getProperty("line.separator"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if(optimized)
			image = optimise(image);
		
		images.put(filePath, image);
		return image;		
	}
	
	/**
	 * Load a BufferedImage (non optimized, for transparent or small images).
	 * @param url Image to be loaded (must be in a package)
	 * @return BufferedImage
	 */
	public static BufferedImage getImage(String url){
		return getImage(url, false);
	}
	
	public static InputStream getFile(String url){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		return classLoader.getResourceAsStream(url);
	}
	
	/**
	 * Optimize the Image with device param.
	 * @param image to be optimized
	 * @return An optimize image
	 */
	private static BufferedImage optimise(BufferedImage image){
		BufferedImage imageTmp = null;
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration config = device.getDefaultConfiguration();
		imageTmp = config.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.OPAQUE);	
		imageTmp.createGraphics().drawImage(image, 0, 0, null);
		
		return imageTmp;
	}

}
