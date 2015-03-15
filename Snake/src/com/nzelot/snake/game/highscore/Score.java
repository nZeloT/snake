package com.nzelot.snake.game.highscore;

public class Score {
	private int score = 0;
	private String name = "";

	public Score(int s, String n) {
		score = s;
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "HScore [score=" + score + ", name=" + name + "]";
	}

}