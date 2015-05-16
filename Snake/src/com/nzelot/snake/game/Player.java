package com.nzelot.snake.game;

import java.awt.*;
import java.util.ArrayList;

public class Player {
	
	private ArrayList<Block> chain;
	
	private int dir;
	private int nDir;
	
	public Player(int x, int y) {
		
		this.dir	= 0;
		this.nDir	= 0;
		
		chain = new ArrayList<Block>();   
		chain.add(new Block(x, y, Color.RED));
		
	}
	
	public void changeDir(int nDir){
		this.nDir = nDir;
	}
	
	public void applyDirChange(){
		if(!(	dir == 0 && nDir == 2
				||	dir == 1 && nDir == 3
				||	dir == 2 && nDir == 0
				||	dir == 3 && nDir == 1))
				dir = nDir;
	}
	
	public void move(){
		int x = chain.get(0).getX();
		int y = chain.get(0).getY();
		
		Point oldPos = new Point(x, y);
		
		switch (dir) {
		case 0:
			y++;
			break;
		case 1:
			x++;
			break;
		case 2:
			y--;
			break;
		case 3:
			x--;
			break;
		}
		
		chain.get(0).setPos(x, y);
		
		for(int i = 1; i < chain.size(); i++){
			Point p = chain.get(i).getPos();
			chain.get(i).setPos(oldPos);
			oldPos = p;
		}
	}
	
	public void eatBlock(Block b){
		if(chain.contains(b))
			System.out.println("-.-");
		chain.add(0, b);
		
		b.setColor(Color.RED);
	}
	
	public ArrayList<Block> getChain() {
		return chain;
	}
	
	public int getDir() {
		return dir;
	}
	
}