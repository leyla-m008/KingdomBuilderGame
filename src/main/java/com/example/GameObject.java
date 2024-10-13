package com.example;

import java.awt.Graphics2D;
import javafx.animation.*;
import javafx.animation.Transition.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;
import java.lang.*;

public class GameObject extends ImageView {
	//new Image(getClass().getResourceAsStream("/images/filename"));
	private static final Image nullImage = new Image(GameObject.class.getResourceAsStream("/images/NullImage.png"));
	private int sortingLayer;
	private ArrayList<ImageView> imageList;
	private ObjectHandler objectHandler;
	private GameObject temp;

	public GameObject() {
		super(nullImage);
		sortingLayer = 2;
		imageList = new ArrayList<>();

		objectHandler = ObjectHandler.get();
		objectHandler.add(this);
	}

	public GameObject(Image image, double x, double y, double width, double height, int sortingLayer) {
		super(image);
		setBounds(x, y, width, height);
		this.sortingLayer = sortingLayer;
		imageList = new ArrayList<>();

		ImageView imgView = new ImageView(image);
		imgView.setX(x);
		imgView.setY(y);
		imgView.setFitWidth(width);
		imgView.setFitHeight(height);
		imageList.add(imgView);

		objectHandler = ObjectHandler.get();
		objectHandler.add(this);
		temp = new GameObject();
	}

	public void setImage(Image image, double x, double y, double width, double height) {
		setImage(image);
		setBounds(x, y, width, height);

		ImageView imgView = new ImageView(image);
		imgView.setX(x);
		imgView.setY(y);
		imgView.setFitWidth(width);
		imgView.setFitHeight(height);

		imageList = new ArrayList<>();
		imageList.add(imgView);
	}

	public void add(Image image1, double x1, double y1, double width1, double height1) {
		ImageView imgView = new ImageView(image1);
		imgView.setX(x1);
		imgView.setY(y1);
		imgView.setFitWidth(width1);
		imgView.setFitHeight(height1);
		imageList.add(imgView);
		//temp = new GameObject(imgView.getImage(), x1, y1, width1, height1, 2);
		//wait(1500);
		Image image2 = getImage();
		double x2 = getX();
		double y2 = getY();
		double width2 = getFitWidth();
		double height2 = getFitHeight();

		BufferedImage bImg1 = SwingFXUtils.fromFXImage(image1, null);
		BufferedImage bImg2 = SwingFXUtils.fromFXImage(image2, null);

		int width = (int)(Math.max(x1 + width1, x2 + width2) - Math.min(x1, x2));
		int height = (int)(Math.max(y1 + height1, y2 + height2) - Math.min(y1, y2));

		BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = combinedImage.createGraphics();

		double x = Math.min(x1, x2);
		double y = Math.min(y1, y2);

		g.drawImage(bImg2, (int)(x2 - x), (int)(y2 - y), (int)width2, (int)height2, null);
		g.drawImage(bImg1, (int)(x1 - x), (int)(y1 - y), (int)width1, (int)height1, null);
		g.dispose();

		WritableImage img = SwingFXUtils.toFXImage(combinedImage, null);
		setImage(img);
		setBounds(Math.min(x1, x2), Math.min(y1, y2), width, height);
	}
	public void addSettlement(Image image1, double x1, double y1, double width1, double height1){
		ImageView imgView = new ImageView(image1);
		imgView.setX(x1);
		imgView.setY(y1);
		imgView.setFitWidth(width1);
		imgView.setFitHeight(height1);
		imageList.add(imgView);
		//this.setVisible(false);
		Image image2 = getImage();
		double x2 = getX();
		double y2 = getY();
		double width2 = getFitWidth();
		double height2 = getFitHeight();

		BufferedImage bImg1 = SwingFXUtils.fromFXImage(image1, null);
		BufferedImage bImg2 = SwingFXUtils.fromFXImage(image2, null);

		int width = (int)(Math.max(x1 + width1, x2 + width2) - Math.min(x1, x2));
		int height = (int)(Math.max(y1 + height1, y2 + height2) - Math.min(y1, y2));

		BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = combinedImage.createGraphics();

		double x = Math.min(x1, x2);
		double y = Math.min(y1, y2);

		g.drawImage(bImg2, (int)(x2 - x), (int)(y2 - y), (int)width2, (int)height2, null);
		g.drawImage(bImg1, (int)(x1 - x), (int)(y1 - y), (int)width1, (int)height1, null);
		g.dispose();

		WritableImage img = SwingFXUtils.toFXImage(combinedImage, null);
		setImage(img);
		setBounds(Math.min(x1, x2), Math.min(y1, y2), width, height);
	}

	public void removeImgAt(double x, double y, double width, double height){
		for(int i = 0; i < imageList.size(); i++) {
			ImageView imgView = imageList.get(i);

			if(x == imgView.getX() &&
					y == imgView.getY() &&
					width == imgView.getFitWidth() &&
					height == imgView.getFitHeight()) {
				imageList.remove(i);
				i = imageList.size();
			}
		}
		@SuppressWarnings("unchecked")
		ArrayList<ImageView> imgList = (ArrayList<ImageView>)imageList.clone();
		if(imgList.size() == 0) {
			imgList.add(new ImageView(nullImage));
		}

		ImageView imgView = imgList.remove(0);
		setImage(imgView.getImage(), imgView.getX(), imgView.getY(), imgView.getFitWidth(), imgView.getFitHeight());

		for(ImageView img : imgList) {
			add(img.getImage(), img.getX(), img.getY(), img.getFitWidth(), img.getFitHeight());
		}
	}

	public void removePreviousImages(int x){
		if(imageList.size() == 0 || x == 0) {
			return;
		}

		for(int i = 0; i < x; i++) {
			if(imageList.size() != 0) {
				imageList.remove(imageList.size() - 1);
			}
		}
		@SuppressWarnings("unchecked")
		ArrayList<ImageView> imgList = (ArrayList<ImageView>)imageList.clone();
		if(imgList.size() == 0) {
			imgList.add(new ImageView(nullImage));
		}

		ImageView imgView = imgList.remove(0);
		setImage(imgView.getImage(), imgView.getX(), imgView.getY(), imgView.getFitWidth(), imgView.getFitHeight());

		for(ImageView img : imgList) {
			add(img.getImage(), img.getX(), img.getY(), img.getFitWidth(), img.getFitHeight());
		}
	}

	public int getSortingLayer() {return sortingLayer;}

	public void setSortingLayer(int sortingLayer) {
		ObjectHandler objectHandler = ObjectHandler.get();
		TreeMap<Integer, ArrayList<GameObject>> gameObjects = objectHandler.getGameObjects();

		ArrayList<GameObject> objList1 = gameObjects.get(this.sortingLayer);
		objList1.remove(this);

		ArrayList<GameObject> objList2 = gameObjects.get(sortingLayer);
		if(objList2 == null) {
			objList2 = new ArrayList<>();
		}
		objList2.add(this);

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

		this.sortingLayer = sortingLayer;
		System.out.println("Not working dumby");
	}

	public void setBounds(double x, double y, double width, double height) {
		setX(x);
		setY(y);
		setFitWidth(width);
		setFitHeight(height);
	}

	public String toString() {
		return getImage() + " " + getX() + " " + getY() + " " + getFitWidth() + " " + getFitHeight() + " " +  getSortingLayer();
	}
	public ImageView getTemp(){
		return temp;
	}
}










