package com.example;

import java.util.*;
import java.util.Map.Entry;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Scoring {
    private static HashMap<String, Boolean> cardMap;
    private static HexNode[][] hexMatrix;
    private static HashMap<HexNode, Integer>[] playerSettlements;
    private static HashMap<HexNode, Integer> currentPlayerSettlements;
    private static ArrayList<TreeMap<Integer, ArrayList<HexNode>>> playerMaps;
    private static TreeMap<Integer, ArrayList<HexNode>> currentPlayerMap;
    private static int playerNum;
    private static int[] scoreArr;

    @SuppressWarnings("unchecked")
    public static void scoreCards() {
        TurnHandler turnhandler = TurnHandler.get();
        Player player = turnhandler.getCurrentPlayer();
        playerMaps = Board.get().getPlayerMaps();
        if(cardMap == null) {
            Board board = Board.get();
            playerSettlements = new HashMap[4];

            for(int i = 0; i < 4; i++) {
                playerSettlements[i] = new HashMap<>();
            }

            buildMap();
            hexMatrix = board.getBoardGraph().getMatrix();
            displayCards();
        }
        updatePlayerSettlements();
        int score = 0;
        for(Entry<String, Boolean> cardEntry : cardMap.entrySet()) {
            if(cardEntry.getValue()) {
                score += score(cardEntry.getKey(), player.getPlayerNum());
            }
        }
        score+= scoreCity(player.getPlayerNum());
        player.setTempScore(score);
        player.updateScore();

    }
    
    public static int[] getScoreArr() {
    	TurnHandler turnHandler = TurnHandler.get();

    	if(scoreArr == null) {
    		scoreArr = new int[turnHandler.getPlayerList().size() * 5];
    	}
    	
    	//set player scores
    	
    	/*          
    	 * player 1: [card 1] [card 2] [card 3] [city score] [total]
    	 * player 2: [card 1] [card 2] [card 3] [city score] [total]
    	 * player 3: [card 1] [card 2] [card 3] [city score] [total]
    	 * player 4: [card 1] [card 2] [card 3] [city score] [total]
    	 * 
    	 * 0 4 8 12 16
    	 * 1 5 9 13 17
    	 * 2 6 10 14 18
    	 * 3 7 11 15 19
    	 * 
    	 * rowAmount = turnHandler.getPlayerList().size();
    	 * use GUI.get().openEndGameScreen();
    	 */
    	
    	ArrayList<String> cardArr = new ArrayList<>();
    	for(Entry<String, Boolean> cardEntry : cardMap.entrySet()) {
            if(cardEntry.getValue()) {
            	cardArr.add(cardEntry.getKey());
            }
        }
    	
    	for(int i = 0; i < scoreArr.length; i++) {
    		if(i / (scoreArr.length / 5) < 3) {
    			scoreArr[i] = score(cardArr.get(i / (scoreArr.length / 5)), i % (scoreArr.length / 5));
    		} else if(i / (scoreArr.length / 5) == 3) {
    			scoreArr[i] = scoreCity(i % (scoreArr.length / 5));
    		} else {
    			int total = 0;
    			
    			for(String str : cardArr) {
    				total += score(str, i % (scoreArr.length / 5));
    			}
    			total += scoreCity(i % (scoreArr.length / 5));
    			
    			scoreArr[i] = total;
    		}
    	}
        
    	return scoreArr;
    }

    private static void buildMap() {
        cardMap = new HashMap<>();
        String[] cardNames = {"citizen", "discoverer", "fisherman", "miner", "worker", "farmer", "knight"};//, "lord"};

        for(String cardName : cardNames) {
            cardMap.put(cardName, false);
        }
        List<String> cardNameList = Arrays.asList(cardNames);
        Collections.shuffle(cardNameList);

        for(int i = 0; i < 3; i++) {
            cardMap.replace(cardNameList.get(i), true);
        }
    }

    @SuppressWarnings("unchecked")
    private static void updatePlayerSettlements() {
        TurnHandler turnhandler = TurnHandler.get();
        Player player = turnhandler.getCurrentPlayer();

        for(int i = 0; i < 4; i++) {
            playerSettlements[i].clear();
        }

        for(HexNode[] row : hexMatrix) {
            for(HexNode node : row) {
                if(node.hasSettlement()) {
                    playerSettlements[node.getPlayerNum()].put(node, node.getSector());
                }
            }
        }
        currentPlayerSettlements = (HashMap<HexNode, Integer>)playerSettlements[player.getPlayerNum()].clone();
        //playerNum = player.getPlayerNum();
    }

    private static int score(String cardName, int pNum) {
        int score = 0;
        playerNum = pNum;
        switch(cardName) {
            case "citizen":
                score += scoreCitizen(hexMatrix);

                break;
            case "discoverer":
                score += scoreDiscoverer(hexMatrix);

                break;
            case "fisherman":
                score += scoreFisherman();

                break;
            case "miner":
                score += scoreMiner();

                break;
            case "worker":
                score += scoreWorker();

                break;
            case "farmer":
                score += scoreFarmer();

                break;
            case "knight":
                score += scoreKnight(hexMatrix);

                break;
            case "lord":
                score += scoreLord();

                break;
        }
        return score;
    }

    public static void displayCards() {
        GameObject cardObj = new GameObject();
        cardObj.setEffect(new DropShadow(10, Color.BLACK));
        int cardY = 50;

        for(Entry<String, Boolean> cardEntry : cardMap.entrySet()) {
            if(cardEntry.getValue()) {
                Image cardImg = new Image(Scoring.class.getResourceAsStream("/images/" + cardEntry.getKey() + "Objective.png"));

                cardObj.add(cardImg, 1730, cardY, cardImg.getWidth() / 2, cardImg.getHeight() / 2);
                cardY += 250;
            }
        }
    }

    private static int scoreCitizen(HexNode[][] hexMatrix){
        int high=0;
        for(int i = 0; i<20; i++){
            for(int j = 0; j<20; j++){
                //if(hexMatrix[i][j].getPlayerNum()==playerNum)
                    //System.out.println(hexMatrix[i][j].getChecked(playerNum));
                if(!hexMatrix[i][j].getChecked(playerNum) && hexMatrix[i][j].getNum()==playerNum){
                    int k = citizenHelper(hexMatrix[i][j]);
                    if (k>high){
                        high = k;
                    }
                }
            }
        }
        for(HexNode[] nodes : hexMatrix)
            for(HexNode node : nodes)
                if(node.getPlayerNum()==playerNum)
                    node.setChecked(playerNum, false);
        //System.out.println(high/2);
        return high/2;
    }
    private static int citizenHelper(HexNode root){
        if(root!= null && root.getNum()==playerNum && !root.getChecked(playerNum)){
            //System.out.println(root.getTerrain());
            HexNode[] lol = new HexNode[6];
            root.setChecked(playerNum);
            for(int i = 0; i<6; i++){
                lol[i]=root.getBordering(i);
            }
            return + 1 + citizenHelper(lol[0]) + citizenHelper(lol[1]) + citizenHelper(lol[2]) + citizenHelper(lol[3]) + citizenHelper(lol[4]) + citizenHelper(lol[5]);
        }
        return 0;
    }
    private static int scoreDiscoverer(HexNode[][] hexMatrix){
        int cnt = 0;
        for(int i = 0; i<20; i++){
            if(discovererHelper(hexMatrix[i][0]))
                cnt++;
        }
        return cnt;
    }
    private static boolean discovererHelper(HexNode root){
        if(root==null)
            return false;
        if(root.getNum()==playerNum)
            return true;
        return discovererHelper(root.getBordering(1));
    }
    private static int scoreFisherman(){
        int pts = 0;
        //x.get()
        TreeMap<Integer, ArrayList<HexNode>> x = Board.get().getPlayerMap(playerNum);
        for( ArrayList<HexNode> z : x.values()){
            for (HexNode node : z){
                //System.out.println(z.size());
                boolean temp = false;
                for(int i =0; i<6; i++){
                    if(node.getBordering(i)!=null)
                        if(node.getBordering(i).getTerrain().equals("w"))
                            temp = true;
                }
                if (temp)
                    pts++;
            }
        }
        //System.out.println(pts);
        return pts;
    }
    private static int scoreMiner(){
        int pts = 0;
        //x.get()
        TreeMap<Integer, ArrayList<HexNode>> x = Board.get().getPlayerMap(playerNum);
        for( ArrayList<HexNode> z : x.values()){
            for (HexNode node : z){
                //System.out.println(z.size());
                boolean temp = false;
                for(int i =0; i<6; i++){
                    if(node.getBordering(i)!=null)
                        if(node.getBordering(i).getTerrain().equals("m"))
                            temp = true;
                }
                if (temp)
                    pts++;
            }
        }
        //System.out.println(pts);
        return pts;
    }
    private static int scoreWorker(){
        String lmfao = "c d w m t f g";
        int pts = 0;
        //x.get()
        TreeMap<Integer, ArrayList<HexNode>> x = Board.get().getPlayerMap(playerNum);
        for( ArrayList<HexNode> z : x.values()){
            for (HexNode node : z){
                boolean temp = false;
                for(int i =0; i<6; i++){
                    if(node.getBordering(i)!=null)
                        if (!lmfao.contains(node.getBordering(i).getTerrain())) {
                            temp = true;
                        }
                }
                if (temp)
                    pts++;
            }
        }
        return pts;
    }
    private static int scoreFarmer(){
        //int pts = 0;
        TreeMap<Integer, ArrayList<HexNode>> x = Board.get().getPlayerMap(playerNum);
        int TL = 0;
        int TR = 0;
        int BL = 0;
        int BR = 0;
        //sector with least
        TL+=x.get(0).size();

        TR+=x.get(1).size();

        BL+=x.get(2).size();

        BR+=x.get(3).size();
        int least = 0;
        least = Math.min(TL, Math.min(TR, Math.min(BL, BR)));
        return least*3;
    }
    private static int scoreKnight(HexNode[][] hexMatrix){
        //playerNum = TurnHandler.get().getCurrentPlayer().getPlayerNum();
        int highest = -1;
        for(int i =0; i<20; i++){
            int z = knightHelper(hexMatrix[i][0]);
            if (z>highest)
                highest=z;
        }
        return highest*2;
    }
    private static int knightHelper(HexNode root){
        if(root==null)
            return 0;
        if(root.getNum()==playerNum) {
            //System.out.println("ran");
            return 1 + knightHelper(root.getBordering(1));
        }
        return knightHelper(root.getBordering(1));
    }
    private static int scoreLord(){
        ArrayList<TreeMap<Integer, ArrayList<HexNode>>> x = Board.get().getPlayerMaps();
        int score = 0;
        int[][] sr = new int[4][4];
        for(int i = 0; i<4;i++){
            for (int j = 0; j<4; j++){
                sr[i][j]=-5;
            }
        }
        int[][] pp = new int[4][4];
        for(int i =0; i<4; i++){
            for(int j =0; j<4; j++){
                pp[i][j] = x.get(i).get(j).size();
                //System.out.print(pp[i][j]  + " ");
            }
            //System.out.println();
        }
        //System.out.println();
        // row is player, column is sector
        for(int i = 0; i<4; i++){
            // i iterates through sector
            int sectorMax = 0;
            int sectorSecond = 0;
            int sectorThird = 0;
            int sectorFourth = 0;
            int sectorMaxPlayer = 5;
            int sectorSecondPlayer = 5;
            int sectorThirdPlayer = 5;
            int sectorFourthPlayer = 5;
            for(int j = 0; j<4; j++){
                // iterates each player
                if(pp[i][j]>sectorMax){
                    sectorMax=pp[i][j];
                    sectorMaxPlayer=j;
                }
                else if(pp[i][j]>sectorSecond){
                    sectorSecond = pp[i][j];
                    sectorSecondPlayer = j;
                }
                else if(pp[i][j]>sectorThird){
                    sectorThird = pp[i][j];
                    sectorThirdPlayer = j;
                }
                else if(pp[i][j]>sectorFourth){
                    sectorFourth = pp[i][j];
                    sectorFourthPlayer = j;
                }
            }
            //System.out.println(sectorMaxPlayer);
            sr[i][0] = sectorMaxPlayer;
            sr[i][1] = sectorSecondPlayer;
            sr[i][2] = sectorThirdPlayer;
            sr[i][3] = sectorFourthPlayer;
            //System.out.println();
        }
        //System.out.println(playerNum);
        //System.out.println();
        for(int j = 0; j<4; j++){
            //System.out.println(j + " " + sr[j][0]);
            if(sr[j][0]==playerNum)
                score+=12;
            else if(sr[j][1]==playerNum)
                score+=6;
        }
        return score;
    }
    
    private static int scoreCity(int playerNum){
        int points =0;
        //boolean check = false;
        //playerNum = TurnHandler.get().getCurrentPlayer().getPlayerNum();
        HashSet<HexNode> set = new HashSet<>();
        for(ArrayList<HexNode> list : Board.get().getPlayerMaps().get(playerNum).values()){
            for (HexNode z : list){
                for (HexNode k : z.getBordering()){
                    if (k!=null && k.getTerrain().equals("ci")){
                        set.add(k);
                    }
                }
            }
        }
        points+=set.size()*3;
        //System.out.println("set size " + set.size());
        return points;
    }
    
    public static HashMap<String, Boolean> getCardMap() {
        return cardMap;
    }
}
