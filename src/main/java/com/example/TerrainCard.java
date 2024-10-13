package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import javafx.scene.image.Image;

public class TerrainCard {
    private Image cardImage;
    private String terrainToken;
    private boolean isActive;
    private boolean isTempActive;
    private String tempString;
    private boolean isUsed;

    public TerrainCard(int type) {
        isActive = false;
        terrainToken = createToken(type);
        tempString = "";
        isUsed = false;
    }

    private String createToken(int type) {
        String temp = "";

        switch(type) {
            case 0:
                temp = "c";
                cardImage = new Image(getClass().getResourceAsStream("/images/KB-Card-Canyon.png"));
                break;

            case 1:
                temp = "d";
                cardImage = new Image(getClass().getResourceAsStream("/images/KB-Card-Desert.png"));
                break;

            case 2:
                temp = "f";
                cardImage = new Image(getClass().getResourceAsStream("/images/KB-Card-Flower.png"));
                break;

            case 3:
                temp = "t";
                cardImage = new Image(getClass().getResourceAsStream("/images/KB-Card-Forest.png"));
                break;

            case 4:
                temp = "g";
                cardImage = new Image(getClass().getResourceAsStream("/images/KB-Card-Meadow.png"));
                break;
        }

        return temp;
    }

    public Image getImage() {return cardImage;}
    public String getToken() {return terrainToken;}
    public boolean isActive() {return isActive;}
    public boolean getIsUsed() {return isUsed;}
    
    public void setIsUsed(boolean used) {isUsed = used;}

    public void activateCard() {
        Board board = Board.get();
        HexButton[][] buttonMatrix = board.getButtonMatrix();
        HashSet<HexButton> buttonSet = new HashSet<>();
        
        board.setSettlementLimit(3);

        for(HexButton[] row : buttonMatrix) {
            for(HexButton button : row) {
                HexNode node = button != null? button.getHexNode() : null;
                TurnHandler turnHandler = TurnHandler.get();
                ArrayList<HexNode> settlementQueue = board.getSettlementQueue();

                if(node == null) {}
                else if((board.getSettlementsPlacedSinceReset() < board.getSettlementLimit() ||
                        node.hasSettlement()) &&
                        node.getTerrainType().equals(terrainToken) &&
                        !(node.hasSettlement() &&
                                (!settlementQueue.isEmpty()? !settlementQueue.get(settlementQueue.size() - 1).equals(node) : true)) &&
                        hasBorderingSettlement(button) ||
                        (node.hasSettlement() &&
                                node.getPlayerNum() == turnHandler.getCurrentPlayer().getPlayerNum() &&
                                (!settlementQueue.isEmpty()? settlementQueue.get(settlementQueue.size() - 1).equals(node) : false))) {
                    //button.setDisable(false);
                    button.setVisible(true);
                    buttonSet.add(button);
                    if(turnHandler.getCurrentPlayer().isUsedActionTile()){
                        button.setVisible(true);
                    }
                }
            }
        }

        if(buttonSet.isEmpty() ||
                (buttonSet.size() == 1 &&
                        buttonSet.stream().findFirst().get().getHexNode().hasSettlement() &&
                        board.getSettlementsPlacedSinceReset() < board.getSettlementLimit())) {
            for(HexButton[] row : buttonMatrix) {
                for(HexButton button : row) {
                    HexNode node = button != null? button.getHexNode() : null;
                    TurnHandler turnHandler = TurnHandler.get();
                    ArrayList<HexNode> settlementQueue = board.getSettlementQueue();

                    if(node == null) {}
                    else if(node.getTerrainType().equals(terrainToken) &&
                            !(node.hasSettlement() &&
                                    node.getPlayerNum() != turnHandler.getCurrentPlayer().getPlayerNum()) &&
                            !(node.hasSettlement() &&
                                    node.getPlayerNum() == turnHandler.getCurrentPlayer().getPlayerNum() &&
                                    (!settlementQueue.isEmpty()? !settlementQueue.get(settlementQueue.size() - 1).equals(node) : true))) {
                        //button.setDisable(false);
                        button.setVisible(true);
                        if (board.getSettlementsPlacedSinceReset()==board.getSettlementLimit()){
                            button.setVisible(false);
                        }
                        if(turnHandler.getCurrentPlayer().isUsedActionTile()){
                            button.setVisible(true);
                        }
                    }
                }
            }
        } else {
            buttonSet.clear();
        }
        isActive = true;
    }

    public void deactivateCard() {
        clearAll();
        isActive = false;
    }
    
    public void clearAll() {
    	Board board = Board.get();
        HexButton[][] buttonMatrix = board.getButtonMatrix();

        for(HexButton[] row : buttonMatrix) {
            for(HexButton button : row) {
                //button.setDisable(true);
                if(button != null) {
                    button.setVisible(false);
                }
            }
        }
    }

    private boolean hasBorderingSettlement(HexButton button) {
        HexNode[] bordering = button.getHexNode().getBordering();

        for(int i = 0; i < bordering.length; i++) {
            HexNode node = bordering[i];
            TurnHandler turnHandler = TurnHandler.get();

            if(node != null && node.hasSettlement() && node.getPlayerNum() == turnHandler.getCurrentPlayer().getPlayerNum()) {
                return true;
            }
        }
        return false;
    }
    public void tempActivateCard(String t) {
        tempString = t;
        isTempActive=true;
        Board board = Board.get();
        HexButton[][] buttonMatrix = board.getButtonMatrix();
        HashSet<HexButton> buttonSet = new HashSet<>();

        for(HexButton[] row : buttonMatrix) {
            for(HexButton button : row) {
                HexNode node = button != null? button.getHexNode() : null;
                TurnHandler turnHandler = TurnHandler.get();
                ArrayList<HexNode> settlementQueue = board.getSettlementQueue();

                if(node == null) {}
                else if((board.getSettlementsPlacedSinceReset() < board.getSettlementLimit() ||
                        node.hasSettlement()) &&
                        node.getTerrainType().equals(t) &&
                        !(node.hasSettlement() &&
                                (!settlementQueue.isEmpty()? !settlementQueue.get(settlementQueue.size() - 1).equals(node) : true)) &&
                        hasBorderingSettlement(button) ||
                        (node.hasSettlement() &&
                                node.getPlayerNum() == turnHandler.getCurrentPlayer().getPlayerNum() &&
                                (!settlementQueue.isEmpty()? settlementQueue.get(settlementQueue.size() - 1).equals(node) : false))) {
                    //button.setDisable(false);
                    button.setVisible(true);
                    buttonSet.add(button);
                    if(turnHandler.getCurrentPlayer().isUsedActionTile()){
                        button.setVisible(true);
                    }
                }
            }
        }

        if(buttonSet.isEmpty() ||
                (buttonSet.size() == 1 &&
                        buttonSet.stream().findFirst().get().getHexNode().hasSettlement() &&
                        board.getSettlementsPlacedSinceReset() < board.getSettlementLimit())) {
            for(HexButton[] row : buttonMatrix) {
                for(HexButton button : row) {
                    HexNode node = button != null? button.getHexNode() : null;
                    TurnHandler turnHandler = TurnHandler.get();
                    ArrayList<HexNode> settlementQueue = board.getSettlementQueue();

                    if(node == null) {}
                    else if(node.getTerrainType().equals(t) &&
                            !(node.hasSettlement() &&
                                    node.getPlayerNum() != turnHandler.getCurrentPlayer().getPlayerNum()) &&
                            !(node.hasSettlement() &&
                                    node.getPlayerNum() == turnHandler.getCurrentPlayer().getPlayerNum() &&
                                    (!settlementQueue.isEmpty()? !settlementQueue.get(settlementQueue.size() - 1).equals(node) : true))) {
                        //button.setDisable(false);
                        button.setVisible(true);
                        if (board.getSettlementsPlacedSinceReset()==board.getSettlementLimit()){
                            button.setVisible(false);
                        }
                        if(turnHandler.getCurrentPlayer().isUsedActionTile()){
                            button.setVisible(true);
                        }
                    }
                }
            }
        } else {
            buttonSet.clear();
        }
        isActive = true;
    }
    public void towerActivateCard() {
        isTempActive=true;
        deactivateCard();
        Board board = Board.get();
        HexButton[][] buttonMatrix = board.getButtonMatrix();
        HashSet<HexButton> buttonSet = new HashSet<>();

        for(int i =0; i<20; i++) {
            for(int j =0; j<20; j++) {
                HexButton button = buttonMatrix[i][j];
                HexNode node = button != null? button.getHexNode() : null;
                TurnHandler turnHandler = TurnHandler.get();
                ArrayList<HexNode> settlementQueue = board.getSettlementQueue();

                if(node == null) {}
                else if((i==19 || i==0 || j==0 || j==19) && !(node.getTerrain().equals("w") || node.getTerrain().equals("m")) && (board.getSettlementsPlacedSinceReset() < board.getSettlementLimit() ||
                        node.hasSettlement()) &&
                        /*node.getTerrainType().equals(t) &&*/
                        !(node.hasSettlement() &&
                                (!settlementQueue.isEmpty()? !settlementQueue.get(settlementQueue.size() - 1).equals(node) : true)) &&
                        hasBorderingSettlement(button) ||
                        (node.hasSettlement() &&
                                node.getPlayerNum() == turnHandler.getCurrentPlayer().getPlayerNum() &&
                                (!settlementQueue.isEmpty()? settlementQueue.get(settlementQueue.size() - 1).equals(node) : false))) {
                    //button.setDisable(false);
                    button.setVisible(true);
                    buttonSet.add(button);
                }
            }
        }

        if(buttonSet.isEmpty() ||
                (buttonSet.size() == 1 &&
                        buttonSet.stream().findFirst().get().getHexNode().hasSettlement() &&
                        board.getSettlementsPlacedSinceReset() < board.getSettlementLimit())) {
            for(int i =0; i<20; i++) {
                for(int j =0; j<20; j++) {
                    HexButton button = buttonMatrix[i][j];
                    HexNode node = button != null? button.getHexNode() : null;
                    TurnHandler turnHandler = TurnHandler.get();
                    ArrayList<HexNode> settlementQueue = board.getSettlementQueue();

                    if(node == null) {}
                    else if(/*node.getTerrainType().equals(t) &&*/!(node.getTerrain().equals("w") || node.getTerrain().equals("m")) &&(i==19 || i==0 || j==0 || j==19) &&
                            !(node.hasSettlement() &&
                                    node.getPlayerNum() != turnHandler.getCurrentPlayer().getPlayerNum()) &&
                            !(node.hasSettlement() &&
                                    node.getPlayerNum() == turnHandler.getCurrentPlayer().getPlayerNum() &&
                                    (!settlementQueue.isEmpty()? !settlementQueue.get(settlementQueue.size() - 1).equals(node) : true))) {
                        //button.setDisable(false);
                        button.setVisible(true);
                    }
                }
            }
        } else {
            buttonSet.clear();
        }
        isActive = true;
    }
    public boolean isTempActive(){
        return isTempActive;
    }
    public void tempDeactivateCard() {
        Board board = Board.get();
        HexButton[][] buttonMatrix = board.getButtonMatrix();

        for(HexButton[] row : buttonMatrix) {
            for(HexButton button : row) {
                //button.setDisable(true);
                if(button != null) {
                    button.setVisible(false);
                }
            }
        }
        isTempActive = false;
    }
    public void reactivate(){
        deactivateCard();
        if (isTempActive && !tempString.equals("")){
            tempActivateCard(tempString);
        }
        if (isTempActive && tempString.equals("")){
            towerActivateCard();
        }
    }
    public void reset(){
        tempString = "";
        deactivateCard();
    }
}










