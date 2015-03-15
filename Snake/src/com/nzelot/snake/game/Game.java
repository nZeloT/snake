package com.nzelot.snake.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import com.nzelot.snake.menu.MENU_STATE;
import com.nzelot.snake.menu.Menu;
import com.nzelot.snake.utils.Settings;

public class Game extends JPanel implements KeyListener{
	private static final long serialVersionUID = -5506385121433927394L;

	private Timer		timer;
	
	private Menu		m;
	
	private GAME_STATE 	state;
	private Highscore	hs;
	
	private Field 		f;
	private Player 		p;
	
	private int 		points;
	
	public Game() {
		timer 	= null;
		m		= new Menu();
		
		hs 		= new Highscore();
		
		Block.setSize( Integer.parseInt( Settings.getItem("BlockSize") ) );
		
		String[] fieldSize = Settings.getItem("FieldSize").split("x");
		int x = Integer.parseInt(fieldSize[0]);
		int y = Integer.parseInt(fieldSize[1]);
		
		Field.setSizeX(x);
		Field.setSizeY(y);
		
		newGame();
		state = GAME_STATE.MENU;
	}
	
	public void newGame(){
		f = new Field();
		p = new Player(5, 5);
		f.addBlock(p.getChain());
		f.spawnMeal();
		points = 0;
		
		state = GAME_STATE.INGAME;
	}
	
	public void update(){
		switch (state) {
		case INGAME:
			p.applyDirChange();
			
			Block b = null;
			if((b = getMeal()) != null){
				p.eatBlock(b);
				++points;
				f.spawnMeal();
			}
			
			p.move();
			
			if(isDead()){
				state = GAME_STATE.MENU;
				endGame();
			}
			
			break;
		case MENU:
			m.update();
			
			if(m.getState() == MENU_STATE.INGAME)
				newGame();
			
			break;
		}
	}
	
	public Block getMeal(){
		Block b 	= null;
		Point pos 	= p.getChain().get(0).getPos();
		switch (p.getDir()) {
		case 0:
			b = f.getBlock(pos.x, pos.y+1);
			break;
		case 1:
			b = f.getBlock(pos.x+1, pos.y);
			break;
		case 2:
			b = f.getBlock(pos.x, pos.y-1);
			break;
		case 3:
			b = f.getBlock(pos.x-1, pos.y);
			break;
		}
		
		//Do not eat yourself. :P
		if(b != null && b.getColor() == Color.RED)
			b = null;
		
		return b;
	}
	
	public boolean isDead(){
		Point pos = p.getChain().get(0).getPos();
		
		//1. Field Borders
		if(		pos.x < 0 || pos.y < 0
			||	pos.x >= Field.getSizeX() 
			||  pos.y >= Field.getSizeY()
			)
			return true;
		
		//2. Crashed into itself, oder so
		for(int i = 1; i < p.getChain().size(); i++)
			if(pos.x == p.getChain().get(i).getX()
			&& pos.y == p.getChain().get(i).getY())
				return true;
				
		return false;
	}
	
	public void endGame(){
		//Check Highscore
		int place = hs.checkScore(points);
		if(place > -1){
			//Enter Highscore
			m.setState(MENU_STATE.ENTER_HIGHSCROE);
		}else{
			//Show Highscore table
			m.setState(MENU_STATE.SHOW_HIGHSCORE);
		}
	}
	
	public void render(Graphics g){
		f.render(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, Field.getPixSizeY(), Game.getWidthPix(), Game.getHeigthPix());
		g.setColor(Color.WHITE);
		g.drawString(String.format("SCORE: %04d", points), 10, Field.getPixSizeY()+14);
		g.drawLine(0, Field.getPixSizeY(), Game.getWidthPix(), Field.getPixSizeY());
		
		if(state == GAME_STATE.MENU)
			m.render(g);
	}
	
	public Field getField() {
		return f;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public static int getWidthPix(){
		return Field.getPixSizeX();
	}
	
	public static int getHeigthPix(){
		return Field.getPixSizeY() + 20;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if(state == GAME_STATE.MENU){
			m.handleKeyInput(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(state == GAME_STATE.MENU)
			return;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			p.changeDir(0);
			break;
		case KeyEvent.VK_RIGHT:
			p.changeDir(1);
			break;
		case KeyEvent.VK_UP:
			p.changeDir(2);
			break;
		case KeyEvent.VK_LEFT:
			p.changeDir(3);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//NOPE
	}
	
	public void start(){
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				update();
				repaint();
			}
		}, 2000, 125);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(timer != null){
			g.clearRect(0, 0, Game.getWidthPix(), Game.getHeigthPix());
			render(g);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Game.getWidthPix(), Game.getHeigthPix());
	}
}
