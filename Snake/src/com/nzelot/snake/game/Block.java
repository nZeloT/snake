package com.nzelot.snake.game;

import java.awt.*;

public class Block {
	private static int size;
	
	private int x;
	private int y;
	private Color c;
	
	public Block(int x, int y, Color c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}

	public static int getSize() {
		return size;
	}

	public static void setSize(int size) {
		Block.size = size;
	}
	
	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public Point getPos(){
		return new Point(x, y);
	}

	public void setPos(Point p) {
		setPos(p.x, p.y);
	}
	
	public Color getColor() {
		return c;
	}
	
	public void setColor(Color c) {
		this.c = c;
	}
	
	public void render(Graphics g){
		g.setColor(c);
		g.fillRect(x*size, y*size, size-1, size-1);
	}
}