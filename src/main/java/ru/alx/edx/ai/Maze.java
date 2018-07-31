package ru.alx.edx.ai;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Maze {
	private final int width;
	private final int height;
	public int [][] tiles;
	
	private final Texture cellTexture;
	private final SpriteBatch batch;
	
	public Maze(String filePath) {
		try {
			BufferedImage image = ImageIO.read(Files.newInputStream(Paths.get(filePath)));		
			cellTexture = new Texture(Gdx.files.local("./images/cell.png"));
			
			batch = new SpriteBatch();
			width = image.getWidth();
			height = image.getHeight();
			tiles = new int[width][height];
			
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int pixel = image.getRGB(x, y) & 0xFFFFFF;
					
					if (pixel == 0xFFFFFF) {
						tiles[x][y] = 0;
					} else {
						tiles[x][y] = 1;
					}
				}
			}
			
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	
	public void draw() {
		batch.begin();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (tiles[x][y] == 1) {
					batch.draw(cellTexture, x * 25, y * 25);	
				}
			}
		}
		
		
		batch.end();
	}
}
