package com.example;

import java.util.HashMap;

public class HexNode {
    private String terrain;
    private HexNode[] bordering;
    private boolean hasSettlement;
    private int playerNum;
    private ActionTile actionTile;
    private boolean confirmed;
    private HexButton hexButton;
    private boolean[] isTaken;
    private boolean[] checked;
    private boolean[] checkedF;
    private int tokens;
    private int sector;

    // 0 1 2 3 4 5
    // NE E SE SW W NW
    public HexNode(String terrain){
        bordering = new HexNode[6];
        this.terrain = terrain;
        hasSettlement = false;
        playerNum = 5;
        confirmed=false;
        isTaken = new boolean[4];
        tokens = 2;
        checked = new boolean[4];
        checkedF = new boolean[4];
        actionTile = null;
    }
    public HexNode(String terrain, int sector){
        this.terrain = terrain;
        this.sector = sector;
        bordering = new HexNode[6];
        hasSettlement = false;
        playerNum = 5;
        actionTile = null;
        checked = new boolean[4];
    }
    // 0 1 2 3 4 5
    // NE E SE SW W NW

    public void checkAdjacent() {
        HexNode nodeAT = null;

        for(HexNode node : bordering) {
            if(node != null && node.getTerrainType().length() > 1) {
                nodeAT = node;
            }
        } if(nodeAT == null) {
            return;
        }
        TurnHandler turnHandler = TurnHandler.get();
        Player player = turnHandler.getCurrentPlayer();

        if(nodeAT.getActionTile().takeToken()) {
            player.addActionTile(nodeAT.getActionTile());
        }
    }

    public void removeAdjacent(boolean returnActionTile) {
        HexNode nodeAT = null;

        for(HexNode node : bordering) {
            if(node!=null)
                if(node.getTerrainType().length() > 1) {
                    nodeAT = node;
                }
        } if(nodeAT == null) {
            return;
        }
        TurnHandler turnHandler = TurnHandler.get();
        Player player = turnHandler.getCurrentPlayer();
        HexNode nodeAT2 = null;

        for(HexNode node : nodeAT.getBordering()) {
            if(node!=null)
                if(node.hasSettlement && node.getPlayerNum() == player.getPlayerNum()) {
                    nodeAT2 = node;
                }
        } if(nodeAT2 != null) {
            return;
        }
        if (nodeAT!=null) {
            if(!returnActionTile)
                nodeAT.getActionTile().returnToken();
            player.removeActionTile(nodeAT.getActionTile());
        }
    }

    public boolean hasSettlement() {return hasSettlement;}
    public int getPlayerNum() {return playerNum;}
    public HexNode[] getBordering(){return bordering;}
    public String getTerrainType() {return terrain;}
    public ActionTile getActionTile() {return actionTile;}

    public void setActionTile(ActionTile tile) {actionTile = tile;}

    public void addSettlement() {
        TurnHandler turnHandler = TurnHandler.get();
        Player player = turnHandler.getCurrentPlayer();

        player.removeSettlement();
        hasSettlement = true;

        playerNum = player.getPlayerNum();
    }

    public void removeSettlement() {
        TurnHandler turnHandler = TurnHandler.get();
        Player player = turnHandler.getCurrentPlayer();
        playerNum = 5;
        player.addSettlement();
        hasSettlement = false;
    }
    public String toString(){
        if(bordering[4]==null){
            if(bordering[3]==null){
                return terrain + " " + toString(bordering[1]) + "\n" + toString(bordering[2]);
            }
            else{
                return terrain + " " + toString(bordering[1]) + "\n" + toString(bordering[3]);
            }
        }
        return terrain + " " + toString(bordering[1]);
    }
    private String toString(HexNode x){
        if(x==null) return "";
        if(x.getBordering(4)==null){
            if(x.getBordering()[3]==null){
                return x.getTerrain() + " " + toString(x.getBordering(1)) + "\n" + toString(x.getBordering(2));
            }
            else{
                return x.getTerrain() + " " + toString(x.getBordering(1)) + "\n" + toString(x.getBordering(3));
            }
        }
        return x.getTerrain() + " " + toString((x.getBordering(1)));
    }
    public String toPlayerString(){
        if(bordering[4]==null){
            if(bordering[3]==null){
                return playerNum + " " + toPlayerString(bordering[1]) + "\n" + toPlayerString(bordering[2]);
            }
            else{
                return playerNum + " " + toPlayerString(bordering[1]) + "\n" + toPlayerString(bordering[3]);
            }
        }
        return playerNum + " " + toPlayerString(bordering[1]);
    }
    private String toPlayerString(HexNode x){
        if(x==null) return "";
        if(x.getBordering()[4]!=null)
            return x.getNum() + " " + toPlayerString(x.getBordering()[1]);
        if(x.getBordering()[3]==null){
            return x.getNum() + " " + toPlayerString(x.getBordering()[1]) + "\n" + toPlayerString(x.getBordering()[2]);
        }
        else{
            return x.getNum() + " " + toPlayerString(x.getBordering()[1]) + "\n" + toPlayerString(x.getBordering()[3]);
        }
    }
    public String getTerrain(){
        return  terrain;
    }
    public HexNode getBordering(int x){
        return bordering[x];
    }
    public int getNum(){
        return playerNum;
    }
    public void setConfirmed(){
        confirmed=!confirmed;
    }
    public boolean isConfirmed(){
        return confirmed;
    }
    public HexButton getHexButton() {return hexButton;}
    //public int getPlayerNum() {return playerNum;}

    public void setHexButton(HexButton button) {
        hexButton = button;
    }
    public boolean isTaken(int x){
        return isTaken[x];
    }
    public void setIsTaken(int x){
        isTaken[x]=true;
        tokens--;
    }
    public void setChecked(int n){
        checked[n] = true;
    }
    public void setChecked(int n, boolean b){
        checked[n] = b;
    }
    public boolean getChecked(int n){
        return checked[n];
    }
    public int getSector(){
        return sector;
    }
}











