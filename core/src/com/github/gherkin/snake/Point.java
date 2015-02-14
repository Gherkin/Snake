package com.github.gherkin.snake;

import java.util.Random;

public class Point {
int x, y, direction;
	
	public Point() {
		Random rand = new Random();
		x = rand.nextInt(9);
		y = rand.nextInt(9);
	}

	public Point(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	@Override
	public String toString() {
		return "" + x + " " + y + " ";
	}
}
