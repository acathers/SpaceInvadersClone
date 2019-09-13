package dev.kemikals;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle implements Moveable {

	private final static int INCREMENT = 10;
	public String type;
	public boolean alive = true;

	LocalDateTime timeLastShot = LocalDateTime.now();



	public Sprite(double x, double y, double h, double w, Color color, String type) {
		super(w, h, color);
		this.type = type;
		setTranslateX(x);
		setTranslateY(y);
	}



	public Sprite(Sprite sprite, double h, double w, Color color, String type) {
		this(sprite.getTranslateX()+sprite.getWidth() /2, sprite.getTranslateY(), h, w, color, createType(sprite, type));
	}

	private static String createType(Sprite sprite, String type) {
		if (sprite.getType().equals("player")) {
			type = "player" + type;
		} else if (sprite.getType().equals("enemy")) {
			type = "enemy" + type;
		}

		return type;
	}

	@Override
	public void moveLeft() {
		setTranslateX(getTranslateX() - INCREMENT);
	}

	@Override
	public void moveRight() {
		setTranslateX(getTranslateX() + INCREMENT);

	}

	@Override
	public void moveUp() {
		setTranslateY(getTranslateY() - INCREMENT);

	}

	@Override
	public void moveDown() {
		setTranslateY(getTranslateY() + INCREMENT);

	}

	public void moveDown(int y) {
		setTranslateY(getTranslateY() + y);

	}

	public String getType() {
		return type;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isEnemy() {
		return type.equals("enemy");
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean canShoot() {
		boolean canShoot = LocalDateTime.now().isAfter(timeLastShot.plusSeconds(1));
		if(canShoot) {
		timeLastShot = LocalDateTime.now();
		}
		return canShoot;
	}

}
