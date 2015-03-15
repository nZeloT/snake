package com.nzelot.snake.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import com.nzelot.snake.game.Game;
import com.nzelot.snake.utils.Settings;

public class Menu {

	private MENU_STATE state;

	private Color selCol;

	private Font fontVeryLarge;
	private Font fontLarge;
	private Font fontNormal;
	private Font fontSmall;
	private Font fontVerySmall;

	public Menu() {
		String[] selectionColor = Settings.getItem("SelectionColor").split("-");

		selCol = new Color(	Integer.parseInt(selectionColor[0]),
				Integer.parseInt(selectionColor[1]), 
				Integer.parseInt(selectionColor[2]));

		state = MENU_STATE.NEW_GAME;

		fontVeryLarge 	= new Font("Arial", 1, 40);
		fontLarge		= new Font("Arial", 0, 30);
		fontNormal  	= new Font("Arial", 0, 25);
		fontSmall		= new Font("Arial", 0, 20);
		fontVerySmall	= new Font("Arial", 0, 10);
	}

	public void update(){

	}

	public void render(Graphics g){
		g.setColor(new Color(0,0,0,200));
		g.fillRect(0, 0, Game.getWidthPix(), Game.getHeigthPix());

		g.setColor(Color.WHITE);

		switch (state) {
		case NEW_GAME:
			drawString("S N A K E", 		0.25f, 		fontVeryLarge, 	g);
			drawString("New Game", 			0.5f, 		fontNormal, 	g);
			drawString("(c)2015 nZeloT", 	0.96875f, 	fontVerySmall, 	g);

			drawSelection(getBoundsRect("New Game", 0.5f, fontNormal, g), g);
			break;
			
		case PAUSED:
			drawString("P A U S E", 		0.25f, 		fontVeryLarge, 	g);
			drawString("Continue", 			0.5f, 		fontNormal, 	g);

			drawSelection(getBoundsRect("Continue", 0.5f, fontNormal, g), g);
			break;
			
		case SHOW_HIGHSCORE:
			
			break;
			
		case ENTER_HIGHSCROE:
			
			break;
			
		case INGAME:
			break;
			
		default:
			break;
		}
	}

	public void setState(MENU_STATE state) {
		this.state = state;
	}
	
	public MENU_STATE getState() {
		return state;
	}

	public void handleKeyInput(KeyEvent evt){
		switch ((int)evt.getKeyChar()) {
		case KeyEvent.VK_ENTER:
			if(state == MENU_STATE.NEW_GAME || state == MENU_STATE.PAUSED)
				state = MENU_STATE.INGAME;
			break;

		default:
			break;
		}
	}

	private void drawString(String s, float y, Font f, Graphics g){
		g.setFont(f);

		//Horizontally centered
		Rectangle2D rect = getBoundsRect(s, y, f, g);

		g.drawString(s, (int)rect.getX(), (int)(rect.getY()+rect.getHeight()));
	}
	
	private Rectangle2D getBoundsRect(String s, float y, Font f, Graphics g){
		FontMetrics fm   = g.getFontMetrics(f);
		Rectangle2D rect = fm.getStringBounds(s, g);

		int x = (int) ((Game.getWidthPix()  - rect.getWidth())  / 2);
		y *= Game.getHeigthPix();
		
		Rectangle2D r = (Rectangle2D) rect.clone();
		r.setRect(x, y, rect.getWidth(), rect.getHeight());
		
		return r;
	}

	private void drawSelection(Rectangle2D rect, Graphics g){
		g.setColor(selCol);
		int oX = (int) (rect.getX() -10);
		int oY = (int) (rect.getY() -5);
		int width = (int) (rect.getWidth()	 +20);
		int height = (int) (rect.getHeight() +18);

		//Sekrecht
		g.drawLine(oX, oY, oX, oY+height);
		g.drawLine(oX+width, oY, oX+width, oY+height);

		//Waagrecht oben
		g.drawLine(oX, oY, oX+15, oY);
		g.drawLine(oX+width-15, oY, oX+width, oY);

		//Waagrecht unten
		g.drawLine(oX, oY+height, oX+15, oY+height);
		g.drawLine(oX+width-15, oY+height, oX+width, oY+height);
	}
}
