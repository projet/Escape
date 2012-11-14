package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import entities.ships.Player;
import entities.ships.WeaponItem;
import factories.WeaponFactory.WeaponType;
import fr.umlv.zen2.MotionEvent;
import fr.umlv.zen2.MotionEvent.Kind;
/**
 * This class represents our Head Up Display, which manage the elements associated with the game (life, weapon).
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
public class Hud {


	private final Player player;
	
	
	/**
	 * BufferedImage for the left and right hud
	 */
	private final BufferedImage hudLeft, hudRight;
	private final BufferedImage cadreSup, cadreInf, cadreBor;
	

	/**
	 * Boolean for know if we have to display the ItemList
	 */
	private boolean displayItemList;

	/**
	 * Only for show an empty item if our ItemList is empty
	 */
	private final WeaponItem itemEmpty;

	/**
	 * Player's score
	 */
	private int score;
	

	/**
	 * The scale to compare one Point of life into a Percent of the Health Menu
	 */

	public Hud(Player player){
		cadreSup = Ressources.getImage("hud/fontWeaponTop.png");
		cadreInf = Ressources.getImage("hud/fontWeaponBot.png");
		cadreBor = Ressources.getImage("hud/fontWeapon.png");

		hudLeft = Ressources.getImage("hud/hudLeft.png");
		hudRight = Ressources.getImage("hud/hudRight.png");

		this.player = player;
		score = 0;
		
		displayItemList = false;
		itemEmpty = new WeaponItem(WeaponType.Null, 0);
	}



	/**
	 * Draw the life of the player
	 * @param graphics the graphics2D to print on
	 */
	public void drawLife(Graphics2D graphics){
		graphics.setColor(Variables.GREEN);
		graphics.fillRect(2*hudLeft.getWidth()/7, 6*hudLeft.getHeight()/11, player.getLife(), hudLeft.getHeight()/4);
		score++;
	}

	/**
	 * Draw the score of the player
	 * @param graphics the graphics2D to print on
	 */
	public void drawScore(Graphics2D graphics){
		graphics.setColor(Variables.WHITE);
		graphics.drawString("SCORE", hudLeft.getWidth()/6,2*hudLeft.getHeight()/5);
		graphics.drawString(Integer.toString(score), hudLeft.getWidth()/2, 2*hudLeft.getHeight()/5);
	}

	/**
	 * display the item list of this object on the graphics
	 * The first element displayed is the second, because the first element is already displays on the hud
	 * Drawing a wallpaper behind item, for have a best render
	 * @param graphics the graphics2D to print on
	 * @param x the begin of the drawing of the listItem, at position x
	 * @param y the begin of the drawing of the listItem, at position y
	 */
	public void drawItemList(Graphics2D graphics, int x, int y){
		int echelleY = cadreSup.getHeight();
		graphics.drawImage(cadreSup, x, y, cadreSup.getWidth(), cadreSup.getHeight(), null);
		
		graphics.setColor(Variables.WHITE);
		graphics.drawString("Weapon", x+22, y+20);

		Iterator<WeaponItem> it = player.getWeapons().iterator();
		if(it.hasNext())//Don't care about the first element, because he is print on the hud
			it.next();

		while(it.hasNext()){
			graphics.drawImage(cadreBor, x, y+echelleY, cadreBor.getWidth(), cadreBor.getHeight(), null);//the font of the item
			it.next().drawItem(graphics, x+5, y+echelleY);// the draw of the item
			echelleY+=cadreBor.getHeight();
		}

		/* Drawing the bo;rder for items*/
		graphics.drawImage(cadreInf, x, y+echelleY, cadreInf.getWidth(), cadreInf.getHeight(), null);
		
	}
	
	/**
	 * Draw the right hud, with the weapon associated. When click on the hud, display the weapon list
	 * @param graphics the graphics2D to print on
	 */
	public void drawWeapons(Graphics2D graphics){
		int beginLeftHud = Variables.SCREEN_WIDTH-hudRight.getWidth();
		graphics.drawImage(hudRight, beginLeftHud, 0, hudRight.getWidth(), hudRight.getHeight(), null);//Right hud

		if(displayItemList == true)//Display menu on click, which is represents by this boolean
			drawItemList(graphics, Variables.SCREEN_WIDTH-hudRight.getWidth() + 2*hudRight.getWidth()/9, 6*hudRight.getHeight()/11);

		if(player.getWeapons().isEmpty())//Drawing actual item in the Right Hud 
			itemEmpty.drawItem(graphics, beginLeftHud+hudRight.getWidth()/4, hudRight.getHeight()/4);
		else
			player.getWeapons().getCurrentWeaponItem().drawItem(graphics, beginLeftHud+hudRight.getWidth()/4, hudRight.getHeight()/4);
		//ONLY FOR TEST -> DRAW THE SIZE OF THE EVENT
		//graphics.drawRect(beginLeftHud, 10, hudRight.getWidth()-20, hudRight.getHeight()-10);
	}


	/**
	 * The event whose checking we select an item in our item list. Be care, doesn't checks if the Item is displayed! The Hud does this work
	 * @see Hud 
	 * @param event The even to check
	 * @param hudRightWidth the Width of the Right Hud
	 * @param hudRightHeight the Height of the Right Hud
	 * @return true if the event is associated with a selection of a weapon, else false.
	 */
	public boolean eventItemList(MotionEvent event){
		int mouseX = event.getX(), mouseY = event.getY();
		int debItemX = Variables.SCREEN_WIDTH-hudRight.getWidth() + hudRight.getWidth()/7;
		int finItemX = debItemX + cadreBor.getWidth();
		int debItemY = 6*hudRight.getHeight()/11+cadreSup.getHeight();
		int echelleY = cadreBor.getHeight();

		if(event.getKind() != Kind.ACTION_DOWN)//Only accept on the down click of the mouse
			return false;

		for(int i=1;i<player.getWeapons().size();i++)
			if(mouseX >= debItemX && mouseX <= finItemX)
				if(mouseY >= debItemY+echelleY*(i-1) && mouseY <= debItemY+echelleY*i){
					//System.out.println("mise en tete de "+itemList.get(i).getName());
					player.getWeapons().setIndexInList(i, 0);
					return true;
				}
		return false;
	}


	/**
	 * Display the menu of weapon when click on the right hud, and launch the eventItemList for manage the selection of weapon
	 * @param event the MotionEvent which reprensents the event of the mouse
	 */
	public void event(MotionEvent event) {
		int beginLeftHud = Variables.SCREEN_WIDTH-hudRight.getWidth();
		int mouseX = event.getX(), mouseY = event.getY();

		/* Check the event of the list of weapon*/
		if(displayItemList == true && eventItemList(event) == true)
			displayItemList=false;

		/*Displaying the menu*/
		if(mouseX >= beginLeftHud && mouseX <= (beginLeftHud+hudRight.getWidth()-20))
			if(mouseY >= 10 && mouseY <= hudRight.getHeight()-10)
				if(event.getKind() == Kind.ACTION_DOWN)
					displayItemList=(displayItemList==true)?false:true;
	}


	/**
	 * Display the HUD, which is compone of several elements : the left hud with the life and score, the right hud with the weapon.
	 * @param graphics the graphics2D to print on
	 */
	public void render(Graphics2D graphics){
		drawLife(graphics);
		graphics.drawImage(hudLeft, 0, 0, hudLeft.getWidth(), hudLeft.getHeight(), null);//Draw the right HUD
		drawWeapons(graphics);
		drawScore(graphics);
	}


}