package dev.kemikals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Game {

	Sprite player = new Sprite(300, 750, 40, 40, Color.BLUE, "player");
	Scene scene;

	List<Sprite> enemies;

	private int score = 0;
	Text scoreText;

	private boolean moveLeft = true;
	LocalDateTime lastMoved = LocalDateTime.now();

	public Game(Scene scene) {
		this.scene = scene;
	}

	public void run() {

		new AnimationTimer() {

			@Override
			public void handle(long time) {
				update();
			}

		}.start();

		drawContent();

		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case A:
				if (player.getTranslateX() > 0)
					player.moveLeft();

				break;
			case D:
				if (player.getTranslateX() < scene.getWidth() - player.getWidth())
					player.moveRight();
				break;

			case W:
				if (player.getTranslateY() > scene.getHeight() - (scene.getHeight() / 4)) {
					player.moveUp();
				}
				break;

			case S:
				if (player.getTranslateY() < scene.getHeight() - player.getHeight()) {
					player.moveDown();
				}
				break;
			case F:
				if (player.canShoot()) {
					shoot(player);
				}
				break;
			default:
				break;
			}
		});
	}

	private void update() {
		sprites().forEach(s -> {
			switch (s.getType()) {
			case "playerbullet":
				s.moveUp();

				sprites().stream().filter(e -> e.getType().equals("enemy")).forEach(enemy -> {
					if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
						enemy.setAlive(false);
						s.setAlive(false);
					}
				});
				break;

			case "enemybullet":
				s.moveDown();

				if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
					player.setAlive(false);
					s.setAlive(false);
				}
				break;

			case "enemy":
				if (Math.random() < 0.0001) {
					if (s.canShoot()) {
						shoot(s);
					}
				}

				break;

			}
		});

		List<Sprite> deadSprites = new ArrayList<>();
		sprites().stream().filter(Predicate.not(Sprite::isAlive)).forEach(deadSprites::add);
		((Pane) scene.getRoot()).getChildren().removeAll(deadSprites);

		if (player.isAlive()) {
			scoreText.setText(String.valueOf(score += deadSprites.size() * 100));
		}

		if (lastMoved.plusSeconds(1).isBefore(LocalDateTime.now())) {

			if (moveLeft) {
				moveAllEnemiesLeft();
				moveLeft = enemiesCanMoveLeft();
			} else {
				moveAllEnemiesRight();
				moveLeft = !enemiesCanMoveRight();
			}

			if (!enemiesCanMoveLeft() || !enemiesCanMoveRight()) {
				moveAllEnemiesDown();
			}
			lastMoved = LocalDateTime.now();
		}

	}

	public void drawContent() {
		topInfoBar(0);
		createEnemies();
		((Pane)scene.getRoot()).getChildren().add(player);

	}

	public void moveAllEnemiesLeft() {
		sprites().stream().filter(s -> s.isEnemy()).forEach(s -> s.moveLeft());
	}

	public boolean enemiesCanMoveLeft() {
		return sprites().stream().filter(s -> s.isEnemy()).mapToDouble(e -> e.getTranslateX()).min().getAsDouble() > 30;
	}

	public boolean enemiesCanMoveRight() {
		List<Sprite> enemies = sprites().stream().filter(s -> s.isEnemy()).collect(Collectors.toList());

		return enemies.stream().mapToDouble(e -> e.getTranslateX()).max().getAsDouble() < scene.getWidth() - 30;

	}

	public void moveAllEnemiesRight() {
		sprites().stream().filter(s -> s.isEnemy()).forEach(e -> e.moveRight());
	}

	public void moveAllEnemiesDown() {
		sprites().stream().filter(s -> s.isEnemy()).forEach(s -> s.moveDown((int) s.getHeight() * 2));

	}

	public void createEnemies() {

		int leftStartingPosition = 90;
		int topStartingPosition = 50;

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 11; j++) {
				Sprite s = new Sprite(leftStartingPosition + j * 40, topStartingPosition, 15, 15, Color.RED, "enemy");
				((Pane) scene.getRoot()).getChildren().add(s);
			}
			topStartingPosition += 30;
		}
	}

	public void topInfoBar(int value) {
		Text t = new Text(0, 20, "Score: ");
		scoreText = new Text(t.getText().length() + 50, 20, String.valueOf(value));
		((Pane) scene.getRoot()).getChildren().addAll(t, scoreText);
	}

	private List<Sprite> sprites() {
		return ((Pane) scene.getRoot()).getChildren().stream().filter(n -> n instanceof Sprite).map(n -> (Sprite) n)
				.collect(Collectors.toList());
	}

	public Sprite getPlayer() {
		return player;
	}

	public void shoot(Sprite sprite) {
		Sprite projectile = new Sprite(sprite, 10, 5, Color.RED, "bullet");
		((Pane) scene.getRoot()).getChildren().add(projectile);
	}

}
