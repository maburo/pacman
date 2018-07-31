package ru.alx.edx.ai;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

	public static void main(String[] args) throws IOException {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game(), configuration);
	}
	
	private static void printRGB(int rgb) {
		System.out.println(String.format("0x%08X", rgb));
		int r = (rgb & 0xFF0000) >> 16;
		int g = (rgb & 0xFF00) >> 8;
		int b = rgb & 0xFF;
		
		System.out.println(String.format("R 0x%02X, G 0x%02X, B 0x%02X", r, g, b));
	}
	
	private static void readMaze(BufferedImage image) {
		for (int x = 0; x < image.getWidth(); x++) {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < image.getHeight(); y++) {
				int pixel = image.getRGB(x, y);
				
				String s = ((pixel & 0xFFFFFF) == 0xFFFFFF) ? "x" : ".";
				sb.append(s);
			}
			
			System.out.println(sb.toString());
		}
	}
}
