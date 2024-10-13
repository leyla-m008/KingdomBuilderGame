package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class GUI {
	private GameButton confirmButton;
	private GameButton cancelButton;
	private GameButton nextTurnButton;
	private HBox moveSelectionBox;
	private Label playerLabel;
	private Button terrainButton;
	private static GUI gui;
	private Group playerStatsGroup;
	private ArrayList<Player> playerList;
	private Group endGameGroup;

	public GUI() {
		Image backgroundImage = new Image(getClass().getResourceAsStream("/images/KB-MainBackground.png"));
		GameObject background = new GameObject(backgroundImage, 0, 0, 1920, 1080, 0);
		endGameGroup = new Group();
		ObjectHandler.get().add(endGameGroup);
		
		Board board = Board.get();
		confirmButton = new GameButton("Confirm");
		confirmButton.setEffect(new DropShadow(10, Color.BLACK));
		Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 32);
		confirmButton.setFont(font);
		confirmButton.setTextFill(Color.WHITE); 
		confirmButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(7), new BorderWidths(7))));
		confirmButton.setBackground(null);
		confirmButton.setBounds(138, 500, 200, 75);

		confirmButton.setOnAction(e -> {
			if(board.getSettlementsPlacedSinceReset() == board.getSettlementLimit()) {
				TurnHandler turnHandler = TurnHandler.get();
				Player player = turnHandler.getCurrentPlayer();
				
				setConfirmButtonDisable(true);
				setCancelButtonDisable(true);
				setNextButtonDisable(false);

				TerrainCard card = board.getActiveCard();
				if(card.isActive() && !card.isTempActive()) {
					card.deactivateCard();
					terrainButton = (Button)moveSelectionBox.getChildren().get(0);
					terrainButton.setDisable(true);
					card.setIsUsed(true);
					
					ArrayList<ActionTile> tileList = player.getActionTileList();
					ArrayList<Button> buttonList = player.getButtonList();
					for(int i = 0; i < tileList.size(); i++) {
						if(!tileList.get(i).getIsUsed() && !tileList.get(i).getRecentlyTaken()) {
							buttonList.get(i).setDisable(false);
						}
					}
				} else {
					player.getCurrentTile().setIsUsed(true);
					System.out.println(player.getCurrentTile() + " " + player.getCurrentTile().getIsUsed());
					
					if(!card.getIsUsed()) {
						terrainButton.setDisable(false);
					}
					
					ArrayList<ActionTile> tileList = player.getActionTileList();
					ArrayList<Button> buttonList = player.getButtonList();
					for(int i = 0; i < tileList.size(); i++) {
						System.out.println(!tileList.get(i).getIsUsed() + " && " + !tileList.get(i).getRecentlyTaken());
						if(tileList.get(i).getIsUsed() || tileList.get(i).getRecentlyTaken()) {
							buttonList.get(i).setDisable(true);
						}
					}
					//card.clearAll();
					System.out.println(card.isActive());
				}
			}
			board.confirmPlacement();
			board.resetSettlementsPlaced();
			TurnHandler.get().getCurrentPlayer().setTurnConfirmed(true);
			TurnHandler.get().getCurrentPlayer().setScore(TurnHandler.get().getCurrentPlayer().getTempScore());
			TurnHandler.get().getCurrentPlayer().updateScore();
			//TurnHandler.get().getCurrentPlayer().updateActionTiles();
			//TurnHandler.get().getCurrentPlayer().actionTileDisabler();
		});
		cancelButton = new GameButton("Cancel");
		cancelButton.setBounds(0, 0, 0, 0);
		/*
		cancelButton.setEffect(new DropShadow(10, Color.BLACK));
		cancelButton.setFont(font);
		cancelButton.setTextFill(Color.WHITE); 
		cancelButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(7), new BorderWidths(7))));
		cancelButton.setBackground(null);
		cancelButton.setBounds(138, 600, 200, 75);

		cancelButton.setOnAction(e -> {
			GameObject settlementObj = board.getSettlementObj();
			if(settlementObj == null) {
				return;
			}
			TurnHandler turnHandler = TurnHandler.get();
			Player player = turnHandler.getCurrentPlayer();
			
			if(!board.getActiveCard().getIsUsed()) {
				player.getAddSettlements().setDisable(false);
			}
			
			ArrayList<ActionTile> tileList = player.getActionTileList();
			ArrayList<Button> buttonList = player.getButtonList();
			for(int i = 0; i < tileList.size(); i++) {
				if(!tileList.get(i).getIsUsed() && !tileList.get(i).getRecentlyTaken()) {
					buttonList.get(i).setDisable(false);
				}
			}
			
			settlementObj.removePreviousImages(board.getSettlementsPlacedSinceReset());
			board.cancelPlacement();
			board.resetSettlementsPlaced();
			setConfirmButtonDisable(true);
			setCancelButtonDisable(true);
			
			TerrainCard card = board.getActiveCard();
			if(card.isActive()) {
				card.deactivateCard();
			}
			TurnHandler.get().getCurrentPlayer().setTurnConfirmed(false);
			TurnHandler.get().getCurrentPlayer().setTempScore(0);
			TurnHandler.get().getCurrentPlayer().updateScore();
			//Board.get().getActiveCard().reset();
		});
		*/

		nextTurnButton = new GameButton("Next turn");
		nextTurnButton.setEffect(new DropShadow(10, Color.BLACK));
		nextTurnButton.setFont(font);
		nextTurnButton.setTextFill(Color.WHITE); 
		nextTurnButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(7), new BorderWidths(7))));
		nextTurnButton.setBackground(null);
		nextTurnButton.setBounds(138, 700, 200, 75);

		nextTurnButton.setOnAction(e -> {
			board.resetSettlementsPlaced();
			TurnHandler.get().getCurrentPlayer().setTurnConfirmed(false);
			
			TurnHandler turnHandler = TurnHandler.get();
			Player player = turnHandler.getCurrentPlayer();
			
			turnHandler.getCurrentPlayer().hideScore();
			turnHandler.nextTurn();
			setNextButtonDisable(true);
			turnHandler.getCurrentPlayer().displayScore();
			//Board.get().getActiveCard().reset();
		});
		setNextButtonDisable(true);

		moveSelectionBox = new HBox();
		ScrollPane moveSelectionScrollPane = new ScrollPane(moveSelectionBox);
		moveSelectionScrollPane.setEffect(new DropShadow(10, Color.BLACK));

		moveSelectionScrollPane.setLayoutX(75);
		moveSelectionScrollPane.setLayoutY(190);
		moveSelectionScrollPane.setPrefSize(320, 62);

		ObjectHandler objectHandler = ObjectHandler.get();
		objectHandler.add(moveSelectionScrollPane);

		TurnHandler turnHandler = TurnHandler.get();
		playerLabel = new Label("Player " + (turnHandler.getCurrentPlayer().getPlayerNum() + 1));
		playerLabel.setEffect(new DropShadow(10, Color.BLACK));

		font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 70);
		playerLabel.setFont(font);
		playerLabel.setTextFill(Color.WHITE); 
		playerLabel.setLayoutX(120);
		playerLabel.setLayoutY(40);
		playerLabel.setPrefSize(300, 80);

		objectHandler.add(playerLabel);

		Player player = turnHandler.getCurrentPlayer();
		
		terrainButton = new Button();
		player.setAddSettlements(terrainButton);
		terrainButton.setEffect(new DropShadow(10, Color.BLACK));

		ImageView settlementImg = new ImageView(player.getSettlementIcon());
		settlementImg.setEffect(new DropShadow(10, Color.BLACK));
		settlementImg.setFitHeight(52);
		settlementImg.setPreserveRatio(true);

		terrainButton.setPrefSize(settlementImg.getFitWidth(), settlementImg.getFitHeight());
		terrainButton.setGraphic(settlementImg);

		terrainButton.setOnAction(e -> {
			setCancelButtonDisable(false);

			TerrainCard card = board.getActiveCard();
			card.reset();
			card.tempDeactivateCard();
			
			if(!card.isActive()) {
				card.activateCard();
			}
		});
		moveSelectionBox.getChildren().add(terrainButton);
		setConfirmButtonDisable(true);
		setCancelButtonDisable(true);

		Scoring.scoreCards();
		updatePlayerStats();
		player.updateSettlementsRemaining();
	}

	public HBox getMoveSelectionBox() {return moveSelectionBox;}

	public void setPlayerLabelText(String text) {playerLabel.setText(text);}
	public void setConfirmButtonDisable(boolean disabled) {confirmButton.setDisable(disabled);}
	public void setCancelButtonDisable(boolean disabled) {cancelButton.setDisable(disabled);}
	public void setNextButtonDisable(boolean disabled) {nextTurnButton.setDisable(disabled);}
	
	@SuppressWarnings("unchecked")
	public void updatePlayerStats() {
		ObjectHandler objectHandler = ObjectHandler.get();
		TurnHandler turnHandler = TurnHandler.get();
		
		if(playerList == null) {
			playerList = (ArrayList<Player>)turnHandler.getPlayerList().clone();
		}
		playerList.add(playerList.remove(0));
		
		if(playerStatsGroup == null) {
			playerStatsGroup = new Group();
			objectHandler.add(playerStatsGroup);
		} else {
			playerStatsGroup.getChildren().clear();
		}
		
		for(int i = 0; i < playerList.size() - 1; i++) {
			Label playerLabel = new Label("Player " + (playerList.get(i).getPlayerNum() + 1));
			playerLabel.setEffect(new DropShadow(10, Color.BLACK));
			playerStatsGroup.getChildren().add(playerLabel);

			Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 20);
			playerLabel.setFont(font);
			playerLabel.setTextFill(Color.WHITE); 
			playerLabel.setLayoutX(60);
			playerLabel.setLayoutY(875 + i * 75);
			playerLabel.setPrefSize(300, 80);

			Image settlementImg = playerList.get(i).getSettlementIcon();
			ImageView settlementObj = new ImageView(settlementImg);
			settlementObj.setEffect(new DropShadow(10, Color.BLACK));
			
			settlementObj.setLayoutX(70);
			settlementObj.setLayoutY(860 + i * 75);
			settlementObj.setFitWidth(settlementImg.getWidth() / 10);
			settlementObj.setFitHeight(settlementImg.getHeight() / 10);
			
			playerStatsGroup.getChildren().add(settlementObj);
			Label settlementNumLabel = new Label("x " + playerList.get(i).getSettlementNum());
			settlementNumLabel.setEffect(new DropShadow(10, Color.BLACK));
			playerStatsGroup.getChildren().add(settlementNumLabel);

			font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 40);
			settlementNumLabel.setFont(font);
			settlementNumLabel.setTextFill(Color.WHITE); 
			settlementNumLabel.setLayoutX(125);
			settlementNumLabel.setLayoutY(840 + i * 75);
			settlementNumLabel.setPrefSize(300, 80);
			
			ArrayList<ActionTile> actionTileList = playerList.get(i).getActionTileList();
			for(int j = 0; j < actionTileList.size(); j++) {
				Image tileImage = actionTileList.get(j).getTileObj().getLocationImage();
				ImageView tileObj = new ImageView(tileImage);
				tileObj.setEffect(new DropShadow(10, Color.BLACK));
				
				tileObj.setLayoutX(200 + j * 50);
				tileObj.setLayoutY(855 + i * 75);
				tileObj.setFitWidth(tileImage.getWidth() / 4);
				tileObj.setFitHeight(tileImage.getHeight() / 4);
				
				playerStatsGroup.getChildren().add(tileObj);
			}
		}
	}

	public static GUI get() {
		if(GUI.gui == null) {
			GUI.gui = new GUI();
		}
		return GUI.gui;
	}
	
	public void openEndGameScreen() {
		TurnHandler turnHandler = TurnHandler.get();
		
		Image nullImage = new Image(getClass().getResourceAsStream("/images/NullImage.png"));
		new GameObject(nullImage, 0, 0, 1920, 1080, 4);
			
		GameButton endGameButton = new GameButton("Click to open or close scoring");
		endGameButton.setEffect(new DropShadow(10, Color.BLACK));
			
		Font font2 = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 30);
		endGameButton.setFont(font2);
		endGameButton.setTextFill(Color.WHITE);
		endGameButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(7))));
		endGameButton.setBackground(null);
		endGameButton.setBounds(760, 960, 400, 80);
		
		endGameButton.setOnAction(action -> {
			if(endGameGroup.getChildren().isEmpty()) {
				openEndGameScreen();
			} else {
				closeEndGameScreen();
			}
		});
		
		Image backgroundImg = new Image(getClass().getResourceAsStream("/images/EndGameScreen.png"));
		ImageView background = new ImageView(backgroundImg);
		background.setEffect(new DropShadow(10, Color.BLACK));

		background.setLayoutX(414);
		background.setLayoutY(152);
		background.setFitWidth(1093);
		background.setFitHeight(776);

		endGameGroup.getChildren().add(background);
		HashMap<String, Boolean> cardMap = Scoring.getCardMap();

		int cardX = 680;
		int playerY = 420;

		for(Map.Entry<String, Boolean> cardEntry : cardMap.entrySet()) {
			if(cardEntry.getValue()) {
				Image cardImg = new Image("/images/" + cardEntry.getKey() + "Objective.png");
				ImageView cardObj = new ImageView();
				cardObj.setEffect(new DropShadow(10, Color.BLACK));

				cardObj.setImage(cardImg);
				cardObj.setLayoutX(cardX);
				cardObj.setLayoutY(240);
				cardObj.setFitWidth(cardImg.getWidth() / 3);
				cardObj.setFitHeight(cardImg.getHeight() / 3);

				endGameGroup.getChildren().add(cardObj);
				cardX += 140;
			}
		}
		Image cityImg = new Image(getClass().getResourceAsStream("/images/KB-Location-City.png"));
		ImageView cityObj = new ImageView(cityImg);
		cityObj.setEffect(new DropShadow(10, Color.BLACK));
		endGameGroup.getChildren().add(cityObj);
		ArrayList<Player> playerList = turnHandler.getPlayerList();
		
		cityObj.setLayoutX(cardX - 20);
		cityObj.setLayoutY(260);
		cityObj.setFitWidth(cityImg.getWidth() / 1.5);
		cityObj.setFitHeight(cityImg.getHeight() / 1.5);
		
		for(Player player : playerList) {
			Image settlementImg = player.getSettlementIcon();
			ImageView settlementObj = new ImageView(settlementImg);
			settlementObj.setEffect(new DropShadow(10, Color.BLACK));
			
			settlementObj.setLayoutX(560);
			settlementObj.setLayoutY(playerY);
			settlementObj.setFitWidth(settlementImg.getWidth() / 8);
			settlementObj.setFitHeight(settlementImg.getHeight() / 8);
			
			endGameGroup.getChildren().add(settlementObj);
			playerY += 100;
			
			Label playerLabel = new Label("Player " + (player.getPlayerNum() + 1));
			playerLabel.setEffect(new DropShadow(10, Color.BLACK));
			
			playerLabel.setLayoutX(555);
			playerLabel.setLayoutY(playerY - 70);
			playerLabel.setPrefSize(300, 80);
			Font font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 20);
			playerLabel.setFont(font);
			playerLabel.setTextFill(Color.WHITE); 
			
			endGameGroup.getChildren().add(playerLabel);
			
			int[] scoreArr = Scoring.getScoreArr();
			for(int i = 0; i < scoreArr.length; i++) {
				Label scoreLabel = new Label(scoreArr[i] + "");
				scoreLabel.setEffect(new DropShadow(10, Color.BLACK));
				endGameGroup.getChildren().add(scoreLabel);
				
				scoreLabel.setLayoutX(710 + 140 * (i / (scoreArr.length / 5)));
				scoreLabel.setLayoutY(410 + 100 * i - i / (scoreArr.length / 5) * (100 * (scoreArr.length / 5)));
				scoreLabel.setPrefSize(300, 80);
				font = Font.loadFont(getClass().getResourceAsStream("/MorrisRoman-Black.ttf"), 50);
				scoreLabel.setFont(font);
				scoreLabel.setTextFill(Color.WHITE); 
			}
		}
	}

	public void closeEndGameScreen() {
		endGameGroup.getChildren().clear();
	}
}






