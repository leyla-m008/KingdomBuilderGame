package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import javafx.animation.Animation;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Board {
	private static Board board;
	private BoardGraph boardGraph;
	private int[] boardNums;
	private HexButton[][] buttonMatrix;
	private GameObject settlementObj;
	private int settlementsPlacedSinceReset;
	private ArrayList<HexNode> settlementQueue;
	private String[][] terrainMatrix;
	private ArrayList<TerrainCard> terrainCards;
	private GameObject terrainCardObj;
	private TerrainCard activeCard;
	private int settlementLimit;
	private ArrayList<TreeMap<Integer, ArrayList<HexNode>>> playerMaps;
	private ArrayList<GameObject> cardBackImages;
	private boolean harbor, barn, tavern, tavernDone;
	private int tavernRemoved;

	public Board(){
		boardNums = new int[4];
		buttonMatrix = new HexButton[20][20];
		settlementsPlacedSinceReset = 0;
		settlementQueue = new ArrayList<>();
		terrainCards = new ArrayList<>();
		terrainCardObj = new GameObject();
		terrainCardObj.setEffect(new DropShadow(10, Color.BLACK));
		terrainCardObj.setBounds(180, 290, 117, 180);
		tavernRemoved = 0;

		setBoardNums();
		displayBoard(504, 14, 1212, 1041);

		HashMap<Integer, String[][]> terrainMaps = buildMap();
		terrainMatrix = buildMatrix(terrainMaps);

		boardGraph = new BoardGraph(terrainMatrix);

		addHexButtons();
		createTerrainCards();
		drawTerrainCard();
		playerMaps = new ArrayList<>();
		for(int i =0; i<4; i++){
			playerMaps.add(new TreeMap<>());
			for(int j = 0; j<4; j++){
				playerMaps.get(i).put(j, new ArrayList<HexNode>());
			}
		}
		settlementLimit=0;
	}

	private void setBoardNums() {
		for(int i = 0; i < 4; i++) {
			int num = (int)(Math.random() * 6 + 1);

			for(int j = 0; j < 4; j++) {
				if(boardNums[j] == num) {
					num = (int)(Math.random() * 6 + 1);
					j = -1;
				}
			}		
			boardNums[i] = num;			
		}
	}

	private void displayBoard(double x, double y, double width, double height){
		//354, 14, 1212, 1041
		//620 x 528
		Image backImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/BlackSquare.png")));
		GameObject obj = new GameObject(backImg, x + 30, 30, 1151, 1008, 1);

		for(int i = 0; i < 4; i++) {
			Image boardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Board" + boardNums[i] + ".png")));
			obj.add(boardImg, x + 591.2 * (i % 2), y + 512.5 * (i / 2), 620, 528);
		}
		obj.setEffect(new DropShadow(10, Color.BLACK));
		obj.setBounds(x, y, width, height);
	}

	private HashMap<Integer, String[][]> buildMap() {
		HashMap<Integer, String[][]> tempMap = new HashMap<>();

		for(int i = 0; i<4; i++) {
			String[][] tempArr = new String[10][10];
			InputStream board = Main.class.getResourceAsStream("/boards/board" + boardNums[i] + ".txt");
			assert board != null;
			Scanner sc = new Scanner(board);

			for(int r = 0; r < tempArr.length; r++) {
				for(int c = 0; c < tempArr[0].length; c++) {
					String token = sc.next();

					if(token.equals("s")) {
						tempArr[r][c] = sc.next();
					} else {
						tempArr[r][c] = token;
					}
				}
			}

			tempMap.put(i, tempArr);
		}
		return tempMap;
	}

	private String[][] buildMatrix(HashMap<Integer, String[][]> terrainMaps) {
		String[][] tempMatrix = new String[20][20];

		for(int r = 0; r < tempMatrix.length; r++) {
			for(int c = 0; c < tempMatrix[0].length; c++) {
				if(r < 10) {
					if(c < 10) {
						tempMatrix[r][c] = terrainMaps.get(0)[r][c];
					} else {
						tempMatrix[r][c] = terrainMaps.get(1)[r][c - 10];
					}
				} else {
					if(c < 10) {
						tempMatrix[r][c] = terrainMaps.get(2)[r - 10][c];
					} else {
						tempMatrix[r][c] = terrainMaps.get(3)[r - 10][c - 10];
					}
				}
			}
		}
		return tempMatrix;
	}

	private void addHexButtons() {
		HexNode[][] hexMatrix = boardGraph.getMatrix();

		for(int r = 0; r < 20; r++) {
			for(int c = 0; c < 20; c++) {
				if(terrainMatrix[r][c].length() < 2) {
					createHexButton(hexMatrix, r, c, 534, 48, 1152, 973.75);
				} else {
					createActionTile(hexMatrix, r, c, 534, 48, 1152, 973.75);
				}
			}
		}
	}

	private HexButton createHexButton(HexNode[][] hexMatrix, int r, int c, double xOffset, double yOffset, double width, double height){
		HexButton button = new HexButton(33, hexMatrix[r][c]);

		double hexWidth = (width - 31 * (width / 1152)) / 19;
		double hexHeight = height / 19;
		button.setBounds(xOffset + hexWidth * c + c / 10 * (1.5 * (width / 1152)) + r % 2 * (29.5 * (width / 1152)), yOffset + r * hexHeight);
		buttonMatrix[r][c] = button;
		//button.setOpacity(0);

		button.setOnMouseClicked(e -> {
			GUI gui = GUI.get();
			TurnHandler turnHandler = TurnHandler.get();
			Player player = turnHandler.getCurrentPlayer();
			HexNode hexNode = button.getHexNode();
			
			if(settlementObj == null) {
				settlementObj = new GameObject();
				//settlementObj.setSortingLayer(2);
				DropShadow shadow = new DropShadow(30, Color.BLACK);
				shadow.setBlurType(BlurType.ONE_PASS_BOX);
				settlementObj.setEffect(shadow);
			}
			if(!hexNode.hasSettlement() && settlementLimit > settlementsPlacedSinceReset && player.getSettlementNum() > 0) {
				//settlementObj.getTemp().setVisible(false);
				//settlementObj.setEffect();
				hexNode.addSettlement();
				settlementsPlacedSinceReset++;
				settlementQueue.add(hexNode);

				if(activeCard.isActive()) {
					if (activeCard.isTempActive())
						activeCard.reactivate();
					else{
						activeCard.deactivateCard();
						activeCard.activateCard();
					}
				}

				if(settlementsPlacedSinceReset == settlementLimit) {
					gui.setConfirmButtonDisable(false);
				}
				Image settlement = player.getSettlementImg();
				double x1 = button.getLayoutX() - 28 + Math.random() * 16;
				double y1 = button.getLayoutY() - 28 + Math.random() * 16;
				double width1 = settlement.getWidth() * (42 / settlement.getHeight());
				GameObject temp = new GameObject(settlement, x1, y1, width1, 42, 3);
				temp.setEffect(new DropShadow(30, Color.BLACK));
				AnimationClass.SettlementFadeScale(.8, temp, settlement, x1, y1, width1);
				hexNode.checkAdjacent();
				ArrayList<HexNode> entry = (ArrayList<HexNode>) playerMaps.get(player.getPlayerNum()).get(hexNode.getSector());
				entry.add(hexNode);
				//System.out.println(entry.size());
				if (hexNode!=null)
					hexNode.getHexButton().setTheImage(x1, y1, width1, 42);
				playerMaps.get(player.getPlayerNum()).put(hexNode.getSector(), entry);
				Scoring.scoreCards();
				System.out.println("sr " + settlementsPlacedSinceReset );
				System.out.println("sl " + settlementLimit);
				player.updateSettlementsRemaining();
				
				if(player.getSettlementNum() == 0) {
					gui.setNextButtonDisable(false);
				}
			} else if(!AnimationClass.getActive()){
				//System.out.println(tavern);
				if(tavern){
					ImageView temp = hexNode.getHexButton().getImageView();
					settlementObj.removeImgAt(temp.getX(), temp.getY(), temp.getFitWidth(), temp.getFitHeight());
					tavernRemoved++;
					System.out.println(settlementsPlacedSinceReset);
					tavern = false;
					tavernDone = true;
					//return;
					if(tavernDone){
						if(harbor){
							Harbor();
						}
						if(barn){
							Barn();
						}
					}
					hexNode.removeSettlement();
					hexNode.removeAdjacent(true);
				}
				else {
					System.out.println("lmfao got here");
					settlementObj.removePreviousImages(1);
					settlementsPlacedSinceReset--;
					settlementQueue.remove(hexNode);
					//
					hexNode.removeSettlement();
					if(activeCard.isActive()) {
						if (activeCard.isTempActive())
							activeCard.reactivate();
						else{
							activeCard.deactivateCard();
							activeCard.activateCard();
						}
					}
					hexNode.removeAdjacent(false);
				}
				if(settlementsPlacedSinceReset == 2) {
					gui.setConfirmButtonDisable(true);
				}
				ArrayList<HexNode> entry = (ArrayList<HexNode>) playerMaps.get(TurnHandler.get().getCurrentPlayer().getPlayerNum()).get(hexNode.getSector());
				entry.remove(hexNode);
				//System.out.println(entry.size());
				playerMaps.get(TurnHandler.get().getCurrentPlayer().getPlayerNum()).put(hexNode.getSector(), entry);
				Scoring.scoreCards();
				TurnHandler.get().getCurrentPlayer().updateSettlementsRemaining();
			}
			//System.out.println(hexMatrix[0][0].toPlayerString());
		});
		//button.setDisable(true);
		button.setVisible(false);
		return button;
	}

	private void createActionTile(HexNode[][] hexMatrix, int r, int c, double xOffset, double yOffset, double width, double height) {
		ActionTile tile = new ActionTile(buttonMatrix[r][c], r, c, xOffset, yOffset, width, height, hexMatrix[r][c].getTerrainType());
		hexMatrix[r][c].setActionTile(tile);
	}

	private void createTerrainCards() {
		for(int i = 0; i < 25; i++) {
			terrainCards.add(new TerrainCard(i % 5));
		}
		Collections.shuffle(terrainCards);
		cardBackImages = new ArrayList<>();
		
		Image cardBack = new Image(getClass().getResourceAsStream("/images/KB-Card-Back.png"));
		GameObject cardBackObj1 = new GameObject(cardBack, 1750, 830, cardBack.getWidth(), cardBack.getHeight(), 2);
		cardBackObj1.setEffect(new DropShadow(10, Color.BLACK));
		cardBackImages.add(cardBackObj1);
		
		for(int i = 0; i < 24; i++) {
			GameObject cardBackObj = new GameObject(cardBack, 1748 + Math.random() * 4, 828 + Math.random() * 4, cardBack.getWidth(), cardBack.getHeight(), 2);
			cardBackObj.setRotate(((Math.random() + 1) * 2 - 3) * 12.5);
			cardBackImages.add(cardBackObj);
		}
	}

	public void drawTerrainCard() {
		ObjectHandler objectHandler = ObjectHandler.get();
		
		if(terrainCards.size() == 0) {
			createTerrainCards();
		}
		TerrainCard card = terrainCards.remove(terrainCards.size() - 1);

		terrainCardObj.setImage(card.getImage());
		activeCard = card;
		objectHandler.remove(cardBackImages.get(cardBackImages.size() - 1));
		cardBackImages.remove(cardBackImages.size() - 1);
	}

	public BoardGraph getBoardGraph() {return boardGraph;}
	public GameObject getSettlementObj() {return settlementObj;}
	public int getSettlementsPlacedSinceReset() {return settlementsPlacedSinceReset;}
	public ArrayList<HexNode> getSettlementQueue() {return settlementQueue;}
	public HexButton[][] getButtonMatrix() {return buttonMatrix;}
	public TerrainCard getActiveCard() {return activeCard;}

	public void resetSettlementsPlaced() {
		settlementQueue.clear();
		settlementsPlacedSinceReset = 0;
		settlementLimit=0;
	}

	public static Board get() {
		if(Board.board == null) {
			Board.board = new Board();
		}
		return Board.board;
	}
	public void confirmPlacement(){
		for (HexNode hexNode : settlementQueue) {
			hexNode.setConfirmed();
		}
		settlementQueue.clear();
	}
	public void cancelPlacement(){
		System.out.println("CURRENT SIZE " + settlementQueue.size());
		for(int j = 0; j<4; j++) {
			ArrayList<HexNode> entry = (ArrayList<HexNode>) playerMaps.get(TurnHandler.get().getCurrentPlayer().getPlayerNum()).get(j);
			for (int i = 0; i < settlementQueue.size(); i++) {
				settlementQueue.get(i).removeSettlement();
				entry.remove(settlementQueue.get(i));
				settlementQueue.get(i).removeAdjacent(false);
				//System.out.println(current.size());
				TurnHandler.get().getCurrentPlayer().setSettlementNum(TurnHandler.get().getCurrentPlayer().getSettlementNum()-1);
			}
			//TurnHandler.get().getCurrentPlayer().setSettlementNum(TurnHandler.get().getCurrentPlayer().getSettlementNum()-9);
			playerMaps.get(TurnHandler.get().getCurrentPlayer().getPlayerNum()).put(j, entry);
		}
		TurnHandler.get().getCurrentPlayer().setSettlementNum(TurnHandler.get().getCurrentPlayer().getSettlementNum() + settlementQueue.size());
		//settlementQueue.clear();
		settlementLimit=3;
		settlementsPlacedSinceReset=0;
		TurnHandler.get().getCurrentPlayer().updateSettlementsRemaining();
	}
	public TreeMap<Integer, ArrayList<HexNode>> getPlayerMap(int x){
		return playerMaps.get(x);
	}
	public ArrayList<TreeMap<Integer, ArrayList<HexNode>>> getPlayerMaps(){
		return playerMaps;
	}
	public int getSettlementLimit(){
		return settlementLimit;
	}
	public void allowAdditionalSettlement(){
		settlementLimit++;
	}
	public void setSettlementLimit(int limit) {
		settlementLimit = limit;
	}
	public void setPlayerSettlementsActive(String type) {
		if (type.equals("harbor"))
			harbor=true;
		if (type.equals("barn"))
			barn=true;
		//int removed = 0;
		tavern = true;
		TurnHandler turnHandler = TurnHandler.get();
		Player player = turnHandler.getCurrentPlayer();
		//HexButton button;
		for(HexButton[] row : buttonMatrix) {
			for(HexButton hexButton : row) {
				if(tavernRemoved<1 && hexButton!=null && hexButton.getHexNode()!=null && hexButton.getHexNode().hasSettlement() &&
						hexButton.getHexNode().getPlayerNum() == player.getPlayerNum()) {
					hexButton.setVisible(true);
					System.out.println("its showing");
					//removed++;
				}
			}
		}
	}
	public boolean getTavernDone(){
		return tavernDone;
	}
	private void Harbor(){
		settlementLimit++;
		Board.get().getActiveCard().tempActivateCard("w");
		//System.out.println("harbor - sl " + settlementLimit + " sp " + settlementsPlacedSinceReset);
	}
	private void Barn(){
		settlementLimit++;
		Board.get().getActiveCard().activateCard();
	}
}









