package com.example;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;

public class HexButton extends Polygon {
    private HexNode hexNode;
    private double radianStep = (2 * Math.PI) / 6;
    private ImageView theImage;

    public HexButton(double radius, HexNode hexNode) {
        //setOnMouseClicked(e -> {});
        this.hexNode = hexNode;
        buildHexagon(radius);
        hexNode.setHexButton(this);
        ObjectHandler objectHandler = ObjectHandler.get();
        objectHandler.add(this);
        //theImage = x;
    }

    private void buildHexagon(double radius) {
        setFill(Color.TRANSPARENT);
        setStroke(Color.BLACK);
        //setEffect(new DropShadow(10, Color.BLACK));
        setStrokeWidth(5);
        //setStrokeType(StrokeType.OUTSIDE);

        for (int i = 0; i < 6; i++) {
            double angle = radianStep * i;

            getPoints().add(Math.cos(angle) * radius / 1.1);
            getPoints().add(Math.sin(angle) * radius / 1.1);
        }
        getTransforms().add(new Rotate(90, 0, 0));
    }

    public void setBounds(double x, double y) {
        Window window = Window.get();
        double xMultiplier = window.getWidth() / 1920;
        double yMultiplier = window.getHeight() / 1080;

        setLayoutX(x * xMultiplier);
        setLayoutY(y * yMultiplier);
    }

    public HexNode getHexNode() {return hexNode;}
    
    public void setTheImage(double x, double y, double width, double height){
        theImage = new ImageView();
        theImage.setX(x);
        theImage.setY(y);
        theImage.setFitHeight(height);
        theImage.setFitWidth(width);
    }
    public ImageView getImageView(){
        return theImage;
    }
}








