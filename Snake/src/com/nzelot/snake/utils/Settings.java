package com.nzelot.snake.utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;



public class Settings {

	private static HashMap<String, String> map;

	public static void readConfig(){
		try{
			map = new HashMap<String, String>();
			BufferedReader br = new BufferedReader(new FileReader("res/config"));
			String in;
			while((in = br.readLine()) != null){
				map.put(in.split("#")[0], in.split("#")[1]);
			}
			br.close();
		}catch(Exception e){
		}
	}
	
	public static void writeConfig(){
		try{
			if(map == null)
				return;
			FileWriter w = new FileWriter("res/config");
			for(String key : map.keySet()){
				w.write(key + "#" + map.get(key) + "\n");
			}
			w.close();
		}catch(Exception e){
		}
	}
	
	public static String getItem(String key){
		if(map == null)
			readConfig();
		return map.get(key);
	}
	
	public static void setItem(String key, String val){
		if(map == null)
			readConfig();
		map.put(key, val);
	}
}
