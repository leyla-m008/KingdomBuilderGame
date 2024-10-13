package com.example;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StartScreen {
	private int playerAmount;

	public StartScreen() {
		playerAmount = 4;
		Image background = new Image(getClass().getResourceAsStream("/images/KB-StartScreen.png"));
		GameObject z = new GameObject(background, 0, 0, 1920, 1080, 0);
		Image logo = new Image(getClass().getResourceAsStream("/images/KB-Logo.png"));
		GameObject y = new GameObject(logo, 640, 30, 290, 256, 3);
		y.setEffect(new DropShadow(10, Color.BLACK));
		
		Image axeImage = new Image(getClass().getResourceAsStream("/images/BackgroundAxe.png"));
		AnimationClass.rotate(31, new GameObject(axeImage, 578, 175, 414, 808, 2));
		Image swordImage = new Image(getClass().getResourceAsStream("/images/BackgroundSword.png"));
		AnimationClass.rotate(-31, new GameObject(swordImage, 962, 225, 276, 656, 2));

		GameButton startButton = new GameButton("Play");
		startButton.setEffect(new DropShadow(10, Color.BLACK));
		Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 40);
		startButton.setFont(font);
		startButton.setTextFill(Color.WHITE);
		startButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(7), new BorderWidths(7))));
		startButton.setBackground(null);
		startButton.setBounds(985, 80, 200, 85);

		GameButton playerAmountButton = new GameButton("Players: " + playerAmount);
		playerAmountButton.setEffect(new DropShadow(10, Color.BLACK));
		playerAmountButton.setFont(font);
		playerAmountButton.setTextFill(Color.WHITE);
		playerAmountButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(7), new BorderWidths(7))));
		playerAmountButton.setBackground(null);
		playerAmountButton.setBounds(970, 175, 230, 85);

		playerAmountButton.setOnMouseReleased(e -> {
			playerAmount++;
			if(playerAmount > 4) {
				playerAmount = 2;
			}
			playerAmountButton.setText("Players: " + playerAmount);
		});
		AnimationClass.FadeScreenOut();

		startButton.setOnMouseReleased(e -> {
			AnimationClass.FadeScreenIn(action -> {
				ObjectHandler objectHandler = ObjectHandler.get();
				objectHandler.clear();
				
				ColorSelection n = new ColorSelection(playerAmount);
			});
		});
	}

}






