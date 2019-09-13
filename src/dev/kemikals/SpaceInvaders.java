package dev.kemikals;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SpaceInvaders extends Application {






	@Override
	public void start(Stage stage) throws Exception {
		Pane root = new Pane();

		root.setPrefSize(600, 800);

		Scene scene = new Scene(root);
		Game game = new Game(scene);

		stage.setTitle("Space Invaders");

		stage.setScene(scene);

		game.run();
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
