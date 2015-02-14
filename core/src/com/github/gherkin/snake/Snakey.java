package com.github.gherkin.snake;

import java.util.ArrayList;

public class Snakey {
	public ArrayList<Point> positions;
	public int direction;
	private Snake snake;
	private Point tailPos;
 
	public Snakey(Snake snak) {
		snake = snak;
		direction = 0;
		
		positions = new ArrayList<Point>();
		
		positions.add(new Point(5, 5, direction));
		positions.add(new Point(4, 5, direction));
		positions.add(new Point(3, 5, direction));
	}
	
	public void initialSnakey() {
		positions.clear();
		
		direction = 0;
		
		positions.add(new Point(5, 5, direction));
		positions.add(new Point(4, 5, direction));
		positions.add(new Point(3, 5, direction));
	}
	
	public void move() {
		tailPos = positions.get(positions.size() - 1);
		for(int i = positions.size() - 1; i > 0; i--)
			positions.set(i, positions.get(i - 1));
	//	positions.remove(positions.size() - 1);
		Point point = positions.get(0);
		switch (direction) {
			case 1:
				if(point.y < 9)
					positions.set(0, new Point(point.x, point.y + 1, direction));
				else
					positions.set(0, new Point(point.x, 0, direction));
				break;
			case 0:
				if(point.x < 9)
					positions.set(0, new Point(point.x + 1, point.y, direction));
				else
					positions.set(0, new Point(0, point.y, direction));
				break;
			case 3: 
				if(point.y > 0)
					positions.set(0, new Point(point.x, point.y - 1, direction));
				else
					positions.set(0, new Point(point.x, 9, direction));
				break;
			case 2:
				if(point.x > 0)
					positions.set(0, new Point(point.x - 1, point.y, direction));
				else
					positions.set(0, new Point(9, point.y, direction));
				break;
		}
		if(positions.get(0).x == snake.cherryPos.x && positions.get(0).y == snake.cherryPos.y) {
			positions.add(tailPos);
			snake.cherry();
		if(positions.subList(1, positions.size() - 1).contains(positions.get(0)))
			initialSnakey();
		}
	}
}
