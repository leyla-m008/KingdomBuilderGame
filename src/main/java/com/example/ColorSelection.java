package com.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ColorSelection {
	private static final String[] settlementColors = {"RED", "GREEN", "PINK", "ORANGE", "BLUE", "BROWN", "YELLOW", "PURPLE"};
	private HashSet<Node> selectionObjects;
	private String[] playerColors;
	private GameButton[] mainButtons;
	private int currentColorNum;
	private GameButton nextButton;

	public ColorSelection(int playerAmount) {
		//setStyle("-fx-background-color: " + Color.COLOR.getValue());
		Image backgroundImage = new Image(getClass().getResourceAsStream("/images/KB-ColorSelectionScreen.png"));
		new GameObject(backgroundImage, 0, 0, 1920, 1080, 0);
		selectionObjects = new HashSet<>();
		playerColors = new String[playerAmount];
		for(int i = 0; i < playerAmount; i++) {
			playerColors[i] = "";
		}

		mainButtons = new GameButton[playerAmount];
		nextButton = new GameButton("Use random colors");
		nextButton.setEffect(new DropShadow(10, Color.BLACK));

		for(int i = 0; i < playerAmount; i++) {
			GameButton button = new GameButton("Click to select color");
			button.setEffect(new DropShadow(10, Color.BLACK));
			Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 20);
			button.setFont(font);
			button.setBounds(1300, 56 + 256 * i, 200, 200);

			button.setOnAction(e -> {
				if(!selectionObjects.isEmpty()) {
					closeColorSelection();
				}

				currentColorNum = (int)(button.getLayoutY() - 56) / 256;
				openColorSelection(button, button.getLayoutX() - 260, button.getLayoutY() - 61);
			});
			mainButtons[i] = button;
		}
		Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 20);
		nextButton.setFont(font);
		nextButton.setTextFill(Color.WHITE);
		nextButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(5))));
		nextButton.setBackground(null);
		nextButton.setBounds(1670, 980, 200, 50);

		nextButton.setOnAction(e -> {
			if(!nextButton.getText().equals("Next")) {
				for(int i = 0; i < playerAmount; i++) {
					if(playerColors[i].equals("")) {
						String newColor = settlementColors[(int)(Math.random() * 8)];

						while(Arrays.asList(playerColors).contains(newColor)) {
							newColor = settlementColors[(int)(Math.random() * 8)];
						}
						playerColors[i] = newColor;
					}
				}
			}
			
			AnimationClass.FadeScreenIn(action -> {
				ObjectHandler objectHandler = ObjectHandler.get();
				objectHandler.clear();

				TurnHandler turnHandler = TurnHandler.get();
				turnHandler.setPlayers(playerColors);
				
				GUI.get();
				Board.get();
				
				AnimationClass.FadeScreenOut();
			});
		});
		AnimationClass.FadeScreenOut();
	}

	private void openColorSelection(GameButton button, double xOffset, double yOffset) {
		GameButton selectionBackground = new GameButton();
		selectionBackground.setEffect(new DropShadow(10, Color.BLACK));
		//selectionBackground.setBounds(xOffset - 60, yOffset, 320, 320);
		selectionBackground.setBounds(25 + 200, 45 + 75, 810, 810);
		selectionBackground.setStyle("-fx-background-color: " + "#" + Color.BLACK.toString().substring(2));
		selectionObjects.add(selectionBackground);
		
		for(int i = 0; i < 8; i++) {
			GameButton colorButton = new GameButton();
			//colorButton.setBounds(xOffset - 55 + 105 * (i % 3), yOffset + 5 + 105 * (i / 3), 100, 100);
			colorButton.setBounds(40 + 265 * (i % 3) + 200, 60 + 265 * (i / 3) + 75, 250, 250);
			colorButton.setStyle("-fx-background-color: " + "#" + Color.web(settlementColors[i]).toString().substring(2));
			for(String str : playerColors) {
				if(str != null && str.equals(settlementColors[i])) {
					colorButton.setDisable(true);
				}
			}

			colorButton.setOnAction(e -> {
				String newColor = colorButton.getStyle().substring(23);

				for(int j = 0; j < 8; j++) {
					String oldColor = Color.web(settlementColors[j]).toString().substring(2);

					if(newColor.equals(oldColor)) {
						playerColors[currentColorNum] = settlementColors[j];
						j = 8;
					}
				}
				mainButtons[currentColorNum].setStyle("-fx-background-color: " + "#" + Color.web(playerColors[currentColorNum]).toString().substring(2));
				closeColorSelection();

				for(String str : playerColors) {
					if(str == null) {
						return;
					}
				}
				
				for(String color : playerColors) {
					if(color == "") {
						return;
					}
				}
				nextButton.setText("Next");
			});
			selectionObjects.add(colorButton);
		}
		GameButton colorButton = new GameButton("Back");
		Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 50);
		colorButton.setFont(font);
		//colorButton.setBounds(xOffset + 155 , yOffset + 215, 100, 100);
		colorButton.setBounds(570 + 200, 590 + 75, 250, 250);

		colorButton.setOnAction(e -> {
			closeColorSelection();
		});
		selectionObjects.add(colorButton);
	}

	private void closeColorSelection() {
		ObjectHandler objectHandler = ObjectHandler.get();
		Iterator<Node> iter = selectionObjects.iterator();

		while(iter.hasNext()) {
			Node node = iter.next();

			objectHandler.remove(node);
		}
		selectionObjects.clear();
	}
}










