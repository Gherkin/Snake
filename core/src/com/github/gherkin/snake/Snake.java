package com.github.gherkin.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Snake extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	TextureRegion ground;
	TiledMap map;
	OrthographicCamera camera;
	TiledMapRenderer renderer;
	int spriteWidth, spriteHeight;
	Snakey snakey;
	StaticTiledMapTile cherry, snakeStart, snakeMid, snakeEnd, snakeCorner, snakeDownCorner;

	TiledMapTileLayer layer;
	
	Point cherryPos;
	
	boolean grow;
	
	@Override
	public void create () {
		grow = false;
		
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
		batch = new SpriteBatch();
		
		ground = new TextureRegion(new Texture("ground.bmp"));
		snakeStart = new StaticTiledMapTile(new TextureRegion(new Texture("snakestart.png"), 16, 16));
		snakeMid = new StaticTiledMapTile(new TextureRegion(new Texture("snakemid.png")));
		snakeEnd = new StaticTiledMapTile(new TextureRegion(new Texture("snakeend.png")));
		snakeCorner = new StaticTiledMapTile(new TextureRegion(new Texture("snakemidcorner.png")));
		snakeDownCorner = new StaticTiledMapTile(new TextureRegion(new Texture("snakedowncorner.png")));
		cherry = new StaticTiledMapTile(new TextureRegion(new Texture("cherry.png")));
		
		spriteHeight = (int) h / 10;
		spriteWidth = (int) w / 10;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		camera.update();
		
		map = new TmxMapLoader().load("snakeMap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, w / 10f / 16f);
		
		Gdx.input.setInputProcessor(this);

		snakey = new Snakey(this);
		
		layer = (TiledMapTileLayer) map.getLayers().get(1);
		
		for(int x = 0; x < 10; x++)
			for(int y = 0; y < 10; y++) {
				layer.setCell(x, y, new Cell());
				layer.getCell(x, y).setTile(null);
			}
		
		layer.getCell(snakey.positions.get(0).x, snakey.positions.get(0).y).setTile(snakeStart);
		for(int i = 1; i < snakey.positions.size() - 1; i++)
			layer.getCell(snakey.positions.get(i).x, snakey.positions.get(i).y).setTile(snakeMid);
		layer.getCell(snakey.positions.get(snakey.positions.size() - 1).x, snakey.positions.get(snakey.positions.size() - 1).y).setTile(snakeEnd);
		cherryPos = new Point();
		cherry();
		Timer.schedule(new Task() {

			@Override
			public void run() {
				move(grow)	;		
			}
			
		}, 1, 0.2f);
	}
	
	public void cherry() {
		while(layer.getCell(cherryPos.x, cherryPos.y).getTile() != null)
			cherryPos = new Point();
		layer.getCell(cherryPos.x, cherryPos.y).setTile(cherry);
	}
	public void removeSnakey() {
		for(Point point : snakey.positions)
			layer.getCell(point.x, point.y).setTile(null);
	}
	public void drawSnakey() {
		layer.getCell(snakey.positions.get(0).x, snakey.positions.get(0).y).setTile(snakeStart);
		layer.getCell(snakey.positions.get(0).x, snakey.positions.get(0).y).setRotation(snakey.positions.get(0).direction);
		layer.getCell(snakey.positions.get(snakey.positions.size() - 1).x, snakey.positions.get(snakey.positions.size() - 1).y).setTile(snakeEnd);
		if(snakey.positions.get(snakey.positions.size() - 1).direction == snakey.positions.get(snakey.positions.size() - 2).direction)
			layer.getCell(snakey.positions.get(snakey.positions.size() - 1).x, snakey.positions.get(snakey.positions.size() - 1).y).setRotation(snakey.positions.get(snakey.positions.size() - 1).direction);
		else
			layer.getCell(snakey.positions.get(snakey.positions.size() - 1).x, snakey.positions.get(snakey.positions.size() - 1).y).setRotation(snakey.positions.get(snakey.positions.size() - 2).direction);
		for(int i = 1; i < snakey.positions.size() - 1; i++) {
			int dir1 = snakey.positions.get(i - 1).direction;
			int dir2 = snakey.positions.get(i).direction;
			if(((dir1 > dir2) || (dir1 == 0 && dir2 == 3)) && ((dir1 != 3) && (dir2 != 0))) {
				System.out.println(dir1 + " " + dir2);
				layer.getCell(snakey.positions.get(i).x, snakey.positions.get(i).y).setTile(snakeCorner);
			}
			else if(dir1 < dir2 || dir1 == 3 && dir2 == 0)
				layer.getCell(snakey.positions.get(i).x, snakey.positions.get(i).y).setTile(snakeDownCorner);
			else {
				layer.getCell(snakey.positions.get(i).x, snakey.positions.get(i).y).setTile(snakeMid);
				layer.getCell(snakey.positions.get(i).x, snakey.positions.get(i).y).setRotation(dir2);
			}
			
		}
	}
	
	public void move(boolean grow) {
		/*if(!grow)
			layer.getCell(snakey.positions.get(snakey.positions.size() - 1).x, snakey.positions.get(snakey.positions.size() - 1).y).setTile(null);
		layer.getCell(snakey.positions.get(0).x, snakey.positions.get(0).y).setTile(snakeMid);
		System.out.println(snakey.positions);*/
		removeSnakey();
		snakey.move();
		drawSnakey();
		/*System.out.println(snakey.positions);
		
		layer.getCell(snakey.positions.get(snakey.positions.size() - 1).x, snakey.positions.get(snakey.positions.size() - 1).y).setTile(snakeEnd);
		layer.getCell(snakey.positions.get(0).x, snakey.positions.get(0).y).setTile(snakeStart);*/
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderer.setView(camera);
		renderer.render();
		batch.begin();
		batch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Keys.DPAD_UP:
				snakey.direction = 1;
				break;
			case Keys.DPAD_RIGHT:
				snakey.direction = 0;
				break;
			case Keys.DPAD_DOWN:
				snakey.direction = 3;
				break;
			case Keys.DPAD_LEFT:
				snakey.direction = 2;
				break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}