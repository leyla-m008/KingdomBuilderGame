package com.example;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AnimationClass implements EventHandler<ActionEvent> {
    private static boolean active;
    public AnimationClass(){}
    public static void SettlementFadeScale(double Duration, javafx.scene.Node n, javafx.scene.image.Image settlement, double x1, double y1, double width1){
        active = true;
        ScaleTransition current = new ScaleTransition(javafx.util.Duration.seconds(Duration), n);
        FadeTransition whatever = new FadeTransition(javafx.util.Duration.seconds(Duration), n);
        GUI.get().setCancelButtonDisable(true);
        whatever.setFromValue(.25);
        whatever.setToValue(1);
        current.setFromX(1.4);
        current.setFromY(1.4);
        current.setToY(1);
        current.setToX(1);
        current.setInterpolator(Interpolator.LINEAR);
        whatever.setInterpolator(Interpolator.LINEAR);
        whatever.play();
        current.setOnFinished(e -> {
            Board.get().getSettlementObj().addSettlement(settlement, x1, y1, width1,42);
            n.setVisible(false);
            GUI.get().setCancelButtonDisable(false);
            active = false;
        });
        current.play();
    }
    public static boolean getActive(){
        return active;
    }
    
    @SuppressWarnings("unchecked")
	public static void FadeScreenIn(){
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.BLACK);
        rectangle.setHeight(1080);
        rectangle.setWidth(1920);
        rectangle.setX(0);
        rectangle.setY(0);
        
        ObjectHandler.get().add(rectangle);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), rectangle);
        
        fadeIn.setFromValue(0);  
        fadeIn.setToValue(1); 
        
        
        fadeIn.setOnFinished(action -> {
        	rectangle.setVisible(false);
        	ObjectHandler.get().remove(rectangle);
        });
        
        fadeIn.setOnFinished(action -> {
        	rectangle.setVisible(false);
        	ObjectHandler.get().remove(rectangle);
        });
        
        fadeIn.play();
    }
    
    @SuppressWarnings("unchecked")
	public static void FadeScreenIn(EventHandler<ActionEvent> e){
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.BLACK);
        rectangle.setHeight(1080);
        rectangle.setWidth(1920);
        rectangle.setX(0);
        rectangle.setY(0);
        
        ObjectHandler.get().add(rectangle);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), rectangle);
        
        fadeIn.setFromValue(0);  
        fadeIn.setToValue(1); 
        
        fadeIn.setOnFinished(action -> {
        	rectangle.setVisible(false);
        	ObjectHandler.get().remove(rectangle);
        	e.handle(action);
        });
        
        fadeIn.play();
    }
    
    @SuppressWarnings("unchecked")
	public static void FadeScreenOut(){
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.BLACK);
        rectangle.setHeight(1080);
        rectangle.setWidth(1920);
        rectangle.setX(0);
        rectangle.setY(0);
        
        ObjectHandler.get().add(rectangle);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), rectangle);
        
        fadeIn.setFromValue(1);  
        fadeIn.setToValue(0); 
        
        fadeIn.setOnFinished(action -> {
        	rectangle.setVisible(false);
        	ObjectHandler.get().remove(rectangle);
        });
        
        fadeIn.play();
    }
    
    @SuppressWarnings("unchecked")
	public static void FadeScreenOut(EventHandler<ActionEvent> e){
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.BLACK);
        rectangle.setHeight(1080);
        rectangle.setWidth(1920);
        rectangle.setX(0);
        rectangle.setY(0);
        
        ObjectHandler.get().add(rectangle);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), rectangle);
        
        fadeIn.setFromValue(1);  
        fadeIn.setToValue(0); 
        
        fadeIn.setOnFinished(action -> {
        	rectangle.setVisible(false);
        	ObjectHandler.get().remove(rectangle);
        	e.handle(action);
        });
        
        fadeIn.play();
    }
    
    public static void rotate(int rotationAngle, Node node) {
        RotateTransition rotate = new RotateTransition();
        
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setByAngle(rotationAngle);
        rotate.setCycleCount(50000);
        rotate.setDuration(Duration.millis(1500));
        rotate.setAutoReverse(true);
        rotate.setNode(node);
        rotate.play();
    }
    
	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
}
