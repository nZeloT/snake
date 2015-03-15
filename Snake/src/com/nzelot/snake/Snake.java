package com.nzelot.snake;

import javax.swing.JFrame;

import com.nzelot.snake.game.Game;

public class Snake {

	public static void main(String[] args) {
		JFrame window = new JFrame("Snake");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Game g = new Game();
		
		window.add(g);
		window.pack();
		
		window.addKeyListener(g);
		
		window.setVisible(true);
		g.start();
	}

}
