package com.nzelot.snake.menu;

import com.nzelot.snake.game.Game;
import com.nzelot.snake.game.highscore.Highscore;
import com.nzelot.snake.game.highscore.Score;
import com.nzelot.snake.utils.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Menu {

	private MENU_STATE state;
	private Highscore	hs;

	private Color selCol;

	private Font fontLarge;
	private Font fontNormal;
	private Font fontSmall;
	private Font fontVerySmall;

	private int selectedIndex;

	private boolean finishedGame;
	private int pointsLastGame;
	private int rankLastGame;
	private String rankName;
	private ArrayList<Score> score;

	public Menu() {
		String[] selectionColor = Settings.getItem("SelectionColor").split("-");

		selCol = new Color(	Integer.parseInt(selectionColor[0]),
				Integer.parseInt(selectionColor[1]),
				Integer.parseInt(selectionColor[2]));

		state 	= MENU_STATE.NEW_GAME;
		hs 		= new Highscore();

		fontLarge 		= new Font("Arial", 1, 40);
		fontNormal  	= new Font("Arial", 0, 25);
		fontSmall		= new Font("Courier New", 0, 20);
		fontVerySmall	= new Font("Arial", 0, 10);

		selectedIndex = 0;

		pointsLastGame  = 0;
		rankLastGame = 0;
		finishedGame = false;
		rankName = "";
		score			= hs.getScoreList();
	}

	public void update(){

	}

	public void render(Graphics g){
		g.setColor(new Color(0,0,0,200));
		g.fillRect(0, 0, Game.getWidthPix(), Game.getHeigthPix());

		g.setColor(Color.WHITE);

		switch (state) {
		case NEW_GAME:
			drawString("S N A K E", 0.25f, fontLarge, g);
			drawString("New Game", 			0.5f, 		fontNormal, 	g);
			drawString("Show Highscore", 0.75f, fontNormal, g);
			drawString("(c)2015 nZeloT", 	0.96875f, 	fontVerySmall, 	g);

			if (selectedIndex == 0)
				drawSelection(getBoundsRect("New Game", 0.5f, fontNormal, g), g);
			else
				drawSelection(getBoundsRect("Show Highscore", 0.75f, fontNormal, g), g);
			break;

		case PAUSED:
			drawString("P A U S E", 		0.25f, 		fontLarge, 	g);
			drawString("Continue", 			0.5f, 		fontNormal, 	g);

			drawSelection(getBoundsRect("Continue", 0.5f, fontNormal, g), g);
			break;

		case SHOW_HIGHSCORE:
			drawString("HIGHSCORE", 		0.25f, 		fontLarge, 	g);

			if (finishedGame)
				drawString(String.format("You got:    %04d", pointsLastGame), 0.375f, fontSmall, g);

			int i = 1;
			for(Score s : score){
				drawString(String.format(i + ". " + to8Chars(s.getName()) + " %04d", s.getScore()), 0.4375f+i*0.0625f, fontSmall, g);
				++i;
			}

			drawString("Main Menu", 		0.875f, 	fontNormal, 	g);

			drawSelection(getBoundsRect("Main Menu", 0.875f, fontNormal, g), g);
			break;

		case ENTER_HIGHSCORE:
			drawString("NEW HIGHSCORE", 	0.25f, 		fontLarge, 	g);

			drawString("You ranked: " + (rankLastGame + 1) + ". !", 0.375f, fontSmall, g);

			drawString(rankName, 0.625f, fontSmall, g);
			Rectangle2D rect = getBoundsRect("        ", 0, fontSmall, g);

			drawString("Ok", 0.875f, fontNormal, g);

			if (selectedIndex == 0)
				g.setColor(selCol);
			else {
				drawSelection(getBoundsRect("Ok", 0.875f, fontNormal, g), g);
				g.setColor(Color.WHITE);
			}

			g.drawLine((int) rect.getX(), (int) (0.625f * Game.getHeigthPix() + rect.getHeight() + 5), (int) (rect.getX() + rect.getWidth()), (int) (0.625f * Game.getHeigthPix() + rect.getHeight() + 5));
			break;

		case INGAME:
			break;
		}
	}

	public void gameEndet(int points){
		//Check Highscore
		rankLastGame = hs.checkScore(points);
		pointsLastGame = points;
		if (rankLastGame > -1) {
			//Enter Highscore
			state = MENU_STATE.ENTER_HIGHSCORE;
			selectedIndex = 0;
		}else{
			//Show Highscore table
			state = MENU_STATE.SHOW_HIGHSCORE;
		}
	}

	public void pauseGame() {
		state = MENU_STATE.PAUSED;
	}

	public MENU_STATE getState() {
		return state;
	}

	public void handleKeyInput(KeyEvent evt){

		switch ((int)evt.getKeyChar()) {
		case KeyEvent.VK_ENTER:
			switch (state) {
			case NEW_GAME:
				if (selectedIndex == 0)
					state = MENU_STATE.INGAME;
				else
					state = MENU_STATE.SHOW_HIGHSCORE;
				break;

			case PAUSED:
				state = MENU_STATE.INGAME;
				break;
			case SHOW_HIGHSCORE:
				state = MENU_STATE.NEW_GAME;
				selectedIndex = 0;
				finishedGame = false;
				break;
			case ENTER_HIGHSCORE:
				if (selectedIndex == 1) {
					state = MENU_STATE.SHOW_HIGHSCORE;
					hs.addScore(rankLastGame, pointsLastGame, rankName);
					rankName = "";
				}
				break;
			case INGAME:
				break;
			}
			break;

		default:
			break;
		}

		if (state == MENU_STATE.ENTER_HIGHSCORE) {
			if (Character.isLetterOrDigit(evt.getKeyChar()) && rankName.length() < 8)
				rankName += evt.getKeyChar();

			if ((int) evt.getKeyChar() == 8 && rankName.length() > 0)
				rankName = rankName.substring(0, rankName.length() - 1);
		}
	}

	public void arrowHandler(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				++selectedIndex;
				if (selectedIndex > getMaxSelectionIndex())
					selectedIndex = getMaxSelectionIndex();
				break;
			case KeyEvent.VK_UP:
				--selectedIndex;
				if (selectedIndex < 0)
					selectedIndex = 0;
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

	private String to8Chars(String s){
		String ret = s;

		if(ret.length() > 8)
			ret = ret.substring(0, 8);
		else{
			for(int i = ret.length(); i <= 8; i++){
				ret += " ";
			}
		}

		return ret;
	}

	private int getMaxSelectionIndex() {
		switch (state) {
			case ENTER_HIGHSCORE:
				return 1;

			case INGAME:
				return 0;

			case NEW_GAME:
				return 1;

			case PAUSED:
				return 0;

			case SHOW_HIGHSCORE:
				return 0;
		}

		return -1;
	}
}