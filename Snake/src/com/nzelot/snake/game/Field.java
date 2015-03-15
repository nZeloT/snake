package com.nzelot.snake.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Field {

	private static int sizeX;
	private static int sizeY;

	private ArrayList<Block> chain;

	public Field() {
		chain = new ArrayList<Block>();
	}

	public void spawnMeal(){
		Random r = new Random();
		int x, y;

		do{
			x = r.nextInt(Field.sizeX);
			y = r.nextInt(Field.sizeY);
		}while(hasBlock(x, y));

		addBlock(new Block(x, y, Color.GREEN));
	}

	public Block getBlock(int x, int y){

		for(int i = 0; i < chain.size(); i++){
			if(chain.get(i).getX() == x && chain.get(i).getY() == y)
				return chain.get(i);
		}

		return null;
	}

	public Block getBlock(Point p){
		return getBlock(p.x, p.y);
	}

	public boolean hasBlock(int x, int y){
		return getBlock(x, y) != null;
	}

	public boolean hasBlock(Point p){
		return hasBlock(p.x, p.y);
	}

	public void addBlock(Block b){
		chain.add(b);
	}

	public void addBlock(ArrayList<Block> bl){
		for(int i = 0; i < bl.size(); i++)
			addBlock(bl.get(i));
	}

	public void render(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Field.getPixSizeX(), Field.getPixSizeY());

		for(Block b : chain)
			b.render(g);
	}
	
	public static int getSizeX() {
		return Field.sizeX;
	}
	
	public static int getSizeY() {
		return Field.sizeY;
	}
	
	public static void setSizeX(int sizeX) {
		Field.sizeX = sizeX;
	}
	
	public static void setSizeY(int sizeY) {
		Field.sizeY = sizeY;
	}
	
	public static int getPixSizeX(){
		return Field.sizeX * Block.getSize();
	}

	public static int getPixSizeY(){
		return Field.sizeY * Block.getSize();
	}

}
