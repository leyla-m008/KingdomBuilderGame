package com.example;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class ObjectHandler {
	private static TreeMap<Integer, ArrayList<GameObject>> gameObjects;
	private static HashSet<GameButton> buttonList;
	private static HashSet<HexButton> hexList;
	private static HashSet<Node> miscList;
	private static ObjectHandler objectHandler;

	public ObjectHandler() {
		gameObjects = new TreeMap<>();
		buttonList = new HashSet<>();
		hexList = new HashSet<>();
		miscList = new HashSet<>();
	}

	public TreeMap<Integer, ArrayList<GameObject>> getGameObjects() {return gameObjects;}

	public void add(Node node) {
		switch(node.getClass().toString()) {
			case "class application.GameObject":
				GameObject obj = (GameObject)node;
				add(obj);

				break;
			case "class application.GameButton":
				GameButton button = (GameButton)node;
				add(button);

				break;
			case "class application.HexButton":
				HexButton hexButton = (HexButton)node;
				add(hexButton);

				break;
			default:
				miscList.add(node);

				Window window = Window.get();
				Group root = window.getRoot();

				root.getChildren().add(node);

				break;
		}
	}

	private void add(GameObject imgView) {
		int sortingLayer = imgView.getSortingLayer();

		if(gameObjects.get(sortingLayer) == null) {
			gameObjects.put(sortingLayer, new ArrayList<GameObject>());
		}

		ArrayList<GameObject> temp;
		temp = gameObjects.get(sortingLayer);
		temp.add(imgView);

		Window window = Window.get();
		Group root = window.getRoot();

		ObservableList<Node> list = root.getChildren();
		list.removeAll(list);

		Set<Entry<Integer, ArrayList<GameObject>>> entrySet = gameObjects.entrySet();
		Iterator<Entry<Integer, ArrayList<GameObject>>> iter = entrySet.iterator();

		while(iter.hasNext()) {
			Entry<Integer, ArrayList<GameObject>> entry = iter.next();
			
			for(GameObject img : entry.getValue()) {
				root.getChildren().add(img);
			}
		}

		Iterator<GameButton> iter2 = buttonList.iterator();
		while(iter2.hasNext()) {
			GameButton button = iter2.next();

			root.getChildren().add(button);
		}

		Iterator<HexButton> iter3 = hexList.iterator();
		while(iter3.hasNext()) {
			HexButton button = iter3.next();

			root.getChildren().add(button);
		}

		Iterator<Node> iter4 = miscList.iterator();
		while(iter4.hasNext()) {
			Node node = iter4.next();

			root.getChildren().add(node);
		}
	}

	private void add(GameButton button) {
		buttonList.add(button);

		Window window = Window.get();
		Group root = window.getRoot();

		root.getChildren().add(button);
	}

	private void add(HexButton button) {
		hexList.add(button);

		Window window = Window.get();
		Group root = window.getRoot();

		root.getChildren().add(button);
	}

	public void clear() {
		Window window = Window.get();
		Group root = window.getRoot();
		ObservableList<Node> rootList = root.getChildren();
		rootList.clear();

		gameObjects.clear();
		buttonList.clear();
		hexList.clear();
	}

	public void remove(Node node) {
		switch(node.getClass().toString()) {
			case "class application.GameObject":
				GameObject obj = (GameObject)node;
				remove(obj);

				break;
			case "class application.GameButton":
				GameButton button = (GameButton)node;
				remove(button);

				break;
			case "class application.HexButton":
				HexButton hexButton = (HexButton)node;
				remove(hexButton);

				break;
		}
	}

	private void remove(GameObject obj) {
		Set<Entry<Integer, ArrayList<GameObject>>> entrySet = gameObjects.entrySet();
		Iterator<Entry<Integer, ArrayList<GameObject>>> iter = entrySet.iterator();

		while(iter.hasNext()) {
			Entry<Integer, ArrayList<GameObject>> entry = iter.next();
			ArrayList<GameObject> objArr = entry.getValue();

			if(objArr.contains(obj)) {
				objArr.remove(obj);
			}
		}
		Window window = Window.get();
		Group root = window.getRoot();

		root.getChildren().remove(obj);
	}

	private void remove(GameButton button) {
		if(buttonList.contains(button)) {
			buttonList.remove(button);
		}
		Window window = Window.get();
		Group root = window.getRoot();

		root.getChildren().remove(button);
	}

	private void remove(HexButton button) {
		if(hexList.contains(button)) {
			hexList.remove(button);
		}
		Window window = Window.get();
		Group root = window.getRoot();

		root.getChildren().remove(button);
	}

	public static ObjectHandler get() {
		if(ObjectHandler.objectHandler == null) {
			ObjectHandler.objectHandler = new ObjectHandler();
		}
		return ObjectHandler.objectHandler;
	}
}

















