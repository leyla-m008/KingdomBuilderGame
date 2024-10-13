package com.example;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class GameButton extends Button {
	private ObjectHandler objectHandler;

	public GameButton() {
		super();
		objectHandler = ObjectHandler.get();
		objectHandler.add(this);
	}
	public GameButton(String text) {
		super(text);
		objectHandler = ObjectHandler.get();
		objectHandler.add(this);
	}
	public GameButton(String text, Node graphic) {
		super(text, graphic);
		objectHandler = ObjectHandler.get();
		objectHandler.add(this);
	}

	public void setBounds(double x, double y, double width, double height) {
		Window window = Window.get();
		double xMultiplier = window.getWidth() / 1920;
		double yMultiplier = window.getHeight() / 1080;

		setLayoutX(x * xMultiplier);
		setLayoutY(y * yMultiplier);

		setMinSize(width * xMultiplier, height * yMultiplier);
		setMaxSize(width * xMultiplier, height * yMultiplier);
		setPrefSize(width * xMultiplier, height * yMultiplier);
	}
}