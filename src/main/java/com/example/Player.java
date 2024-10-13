package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.example.ActionTile.ActionTileTemplate;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Player {
    private int score;
    private int playerNum;
    private int settlementNum;
    private Image[] settlementImgArr;
    private Image settlementIcon;
    private ArrayList<ActionTile> actionTileList;
    private ArrayList<Button> actionButtonList;
    private Label scoreLabel;
    private int tempScore;
    private boolean turnConfirmed;
    private boolean usedActionTile;
    private Image firstPlayerToken;
    private static GameObject firstPlayerObj;
    private static Group settlementAmountGroup;
    private Button addSettlements;
    private Button currentButton;
    private ActionTile currentTile;

    public Player(int num, String color) {
        score = 0;
        playerNum = num;
        settlementNum = 40;
        settlementImgArr = new Image[4];
        settlementIcon = new Image(getClass().getResourceAsStream("/images/" + color + ".png"));
        actionTileList = new ArrayList<>();
        actionButtonList = new ArrayList<>();

        for(int i = 1; i < 5; i++) {
            settlementImgArr[i - 1] = new Image(getClass().getResourceAsStream("/images/" + color + i + ".png"));
        }
        displayScore();
        tempScore =0;
        turnConfirmed = false;
        firstPlayerToken = new Image(getClass().getResourceAsStream("/images/NullImage.png"));

        if(firstPlayerObj == null) {
            firstPlayerObj = new GameObject(firstPlayerToken, 215, 10, 50, 57.67, 2);
            firstPlayerObj.setEffect(new DropShadow(10, Color.BLACK));
        }
    }

    public Image getSettlementIcon() {return settlementIcon;}
    public Image getSettlementImg() {
        List<Image> tempList = Arrays.asList(settlementImgArr);
        Collections.shuffle(tempList);

        return tempList.get(0);
    }
    public int getSettlementNum() {return settlementNum;}
    public int getPlayerNum() {return playerNum;}
    public ArrayList<ActionTile> getActionTileList() {return actionTileList;}
    public Button getCurrentButton() {return currentButton;}
    public ArrayList<Button> getButtonList() {return actionButtonList;}
    public Button getAddSettlements() {return addSettlements;}
    public ActionTile getCurrentTile() {return currentTile;}

    public void setScore(int score) {this.score = score;}
    public void setTempScore(int score) {this.tempScore = score;}
    public void setAddSettlements(Button settlementButton) {addSettlements = settlementButton;}

    public void displayScore() {
        ObjectHandler objectHandler = ObjectHandler.get();

        scoreLabel = new Label("Score: ");
        scoreLabel.setEffect(new DropShadow(10, Color.BLACK));
        
        Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 50);
        scoreLabel.setFont(font);
		scoreLabel.setTextFill(Color.WHITE); 
        scoreLabel.setVisible(true);
        scoreLabel.setLayoutX(160);
        scoreLabel.setLayoutY(100);
        scoreLabel.setPrefSize(300, 80);

        objectHandler.add(scoreLabel);
    }

    private void displayFirstPlayerToken() {
        firstPlayerObj.setImage(firstPlayerToken);
    }

    public void setFirstPlayer() {
        firstPlayerToken = new Image(getClass().getResourceAsStream("/images/KB-FirstPlayerToken.png"));
        displayFirstPlayerToken();
    }

    public void updateScore() {
        if (!turnConfirmed)
            scoreLabel.setText("Score: " + tempScore);
        else
            scoreLabel.setText("Score: " + score);
    }

    public void startTurn() {
    	for(ActionTile tile : actionTileList) {
        	tile.setRecentlyTaken(false);
        }
    	for(int i = 0; i < actionTileList.size(); i++) {
        	actionTileList.get(i).setIsUsed(false);
        }
    	
        updateGUIButtons();
        displayFirstPlayerToken();
        updateSettlementsRemaining();
        turnConfirmed = false;
    }

    public void addActionTile(ActionTile actionTile) {
        actionTileList.add(actionTile);
        updateGUIButtons();
    }

    public void removeActionTile(ActionTile actionTile) {
        actionTileList.remove(actionTile);
        updateGUIButtons();
    }

    private void updateGUIButtons() {
        GUI gui = GUI.get();
        ObservableList<Node> moveSelectionList = gui.getMoveSelectionBox().getChildren();

        moveSelectionList.clear();

        updateTerrainCard();
        updateActionTiles();
    }

    private void updateTerrainCard() {
        GUI gui = GUI.get();
        ObservableList<Node> moveSelectionList = gui.getMoveSelectionBox().getChildren();

        addSettlements = new Button();

        ImageView settlementImg = new ImageView(settlementIcon);
        settlementImg.setEffect(new DropShadow(10, Color.BLACK));
        settlementImg.setFitHeight(52);
        settlementImg.setPreserveRatio(true);

        addSettlements.setPrefSize(settlementImg.getFitWidth(), settlementImg.getFitHeight());
        addSettlements.setGraphic(settlementImg);

        addSettlements.setOnAction(e -> {
            Board board = Board.get();

            gui.setCancelButtonDisable(false);

            TerrainCard card = board.getActiveCard();
            if(!card.isActive()) {
                card.activateCard();
            }
            
            for(Button button : actionButtonList) {
            	button.setDisable(true);
            }
        });
        moveSelectionList.add(addSettlements);
    }

    public void updateActionTiles() {
        Iterator<ActionTile> iter = actionTileList.iterator();
        actionButtonList.clear();

        while(iter.hasNext()) {
            ActionTile tile = iter.next();
            GUI gui = GUI.get();
            ObservableList<Node> moveSelectionList = gui.getMoveSelectionBox().getChildren();

            Button addActionTiles = new Button();
            actionButtonList.add(addActionTiles);

            ActionTileTemplate tileObj = tile.getTileObj();
            ImageView tileImg = new ImageView(tileObj.getLocationImage());
            tileImg.setFitHeight(52);
            tileImg.setPreserveRatio(true);

            addActionTiles.setPrefSize(tileImg.getFitWidth(), tileImg.getFitHeight());
            addActionTiles.setGraphic(tileImg);
            addActionTiles.setOnAction(e -> {
            	currentTile = tile;
            	System.out.println(tile);
            	currentButton = addActionTiles;
                gui.setCancelButtonDisable(true);
                usedActionTile = true;
                tileObj.setActive(true);
                
                addSettlements.setDisable(true);
                for(int i = 0; i < actionTileList.size(); i++) {
                	if(tile != actionTileList.get(i)) {
                		actionButtonList.get(i).setDisable(true);
                	}
                }
            });
            moveSelectionList.add(addActionTiles);
            addActionTiles.setDisable(tile.getRecentlyTaken());
        }
    }
    
    public void updateSettlementsRemaining() {
    	if(settlementAmountGroup == null) {
    		settlementAmountGroup = new Group();
    		ObjectHandler.get().add(settlementAmountGroup);
    	}
    	settlementAmountGroup.getChildren().clear();
    	
    	ImageView settlementObj = new ImageView(settlementIcon);
    	settlementObj.setEffect(new DropShadow(10, Color.BLACK));
    	settlementObj.setLayoutX(160);
    	settlementObj.setLayoutY(780);
    	settlementObj.setFitWidth(settlementIcon.getWidth() / 7);
    	settlementObj.setFitHeight(settlementIcon.getHeight() / 7);
    	
    	settlementAmountGroup.getChildren().add(settlementObj);
    	Label playerLabel = new Label("x " + settlementNum);
    	playerLabel.setEffect(new DropShadow(10, Color.BLACK));
		settlementAmountGroup.getChildren().add(playerLabel);

		Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 50);
		playerLabel.setFont(font);
		playerLabel.setTextFill(Color.WHITE); 
		playerLabel.setLayoutX(230);
		playerLabel.setLayoutY(770);
		playerLabel.setPrefSize(300, 80);
    }

    public void addSettlement() {settlementNum++;}
    public void removeSettlement() {
    	settlementNum--;
    }
    
    public void setTurnConfirmed(boolean x){
        turnConfirmed = x;
    }
    public int getTempScore(){
        return tempScore;
    }
    public int getScore(){
        return score;
    }
    public void hideScore(){
        scoreLabel.setVisible(false);
    }
    public boolean isUsedActionTile(){
        return usedActionTile;
    }
    public void setSettlementNum(int n){
        settlementNum = n;
    }
}







