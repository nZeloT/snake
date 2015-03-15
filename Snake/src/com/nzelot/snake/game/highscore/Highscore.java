package com.nzelot.snake.game.highscore;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.nzelot.snake.utils.Crypt;
import com.nzelot.snake.utils.Settings;

public class Highscore {
	private ArrayList<Score> scoreList;

	public Highscore() {
		try{
			readScores();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public int checkScore(int score){
		int ret = -1;
		for (int i = 0; i < scoreList.size(); i++) {
			if(scoreList.get(i).getScore() < score){
				ret = i;
				break;
			}
		}
		return ret;
	}

	public void addScore(int pos, int score, String name){
		if(pos < 0 || scoreList.size() <= pos)
			return;

		scoreList.add(pos, new Score(score, checkForIllegalcharacters(name)));
		scoreList.remove(scoreList.size()-1);
		try {
			writeScores();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editScore(int pos, String name){
		if(pos < 0 || scoreList.size() <= pos)
			return;

		scoreList.get(pos).setName(checkForIllegalcharacters(name));
		try {
			writeScores();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readScores() throws Exception {
		String fileName = Settings.getItem("HighscoreFile");
		scoreList = new ArrayList<Score>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "ISO-8859-1"));
		String in;
		int j = 0;
		while((in = br.readLine()) != null && (j++) < 5){
			in = Crypt.decrypt(in, genD());
			scoreList.add(new Score(Integer.parseInt(in.split("#")[0]), in.split("#")[1]));
		}
		br.close();
	}

	private void writeScores() throws Exception{
		String fileName = Settings.getItem("HighscoreFile");
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(fileName), "ISO-8859-1");

		for (Score hs : scoreList) {
			w.write(Crypt.crypt(hs.getScore() + "#" + hs.getName() + "", genD()) + "\r\n");
		}
		w.close();

	}

	private int genD() {
		float zwv = 42.0f;
		zwv = 5 << 8 - 889/254 + 0xACDC << 0xCC - (10^0xA - 10^0xC);
		zwv %= 255;
		return (int) zwv;
	}

	private String checkForIllegalcharacters(String in){
		String out = "";
		for (int i = 0; i < in.length(); i++) {
			char ch = in.charAt(i);
			if(!Character.isLetterOrDigit(ch))
				ch = 'F';
			else if(ch == '\u00E4')
				ch = 'a';
			else if(ch == '\u00C4')
				ch = 'A';
			else if(ch == '\u00F6')
				ch = 'o';
			else if(ch == '\u00D6')
				ch = 'O';
			else if(ch == '\u00DC')
				ch = 'U';
			else if(ch == '\u00FC')
				ch = 'u';
			else if(ch == '\u00DF')
				ch = 's';
			out += ch;
		}
		return out;
	}

	public ArrayList<Score> getScoreList() {
		return scoreList;
	}

}
