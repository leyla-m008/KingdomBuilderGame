package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TurnHandler {
	private ArrayList<Player> playerList;
	private ListIterator<Player> playerIter;
	private static TurnHandler turnHandler;

	public TurnHandler() {
		playerList = new ArrayList<>();
	}

	public void nextTurn() {
		playerIter.next();

		if(!playerIter.hasNext()) {
			while(playerIter.hasPrevious()) {
				playerIter.previous();
			}
		}
		getCurrentPlayer().startTurn();

		GUI gui = GUI.get();
		Board board = Board.get();

		gui.setPlayerLabelText("Player " + (getCurrentPlayer().getPlayerNum() + 1));
		gui.updatePlayerStats();
		board.drawTerrainCard();
		
		if(canEndGame() && getCurrentPlayer().getPlayerNum() == 0) {
			gui.openEndGameScreen();
		}
	}

	public Player getCurrentPlayer() {
		playerIter.next();

		return playerIter.previous();
	}
	public ArrayList<Player> getPlayerList() {return playerList;}

	public void setPlayers(String[] colors) {
		for(int i = 0; i < colors.length; i++) {
			Player player = new Player(i, colors[i]);
			playerList.add(player);
		}
		playerIter = playerList.listIterator();
		playerList.get(0).setFirstPlayer();
	}
	
	public boolean canEndGame() {
		for(Player player : playerList) {
			if(player.getSettlementNum() == 0) {
				return true;
			}
		}
		return false;
	}

	public static TurnHandler get() {
		if(TurnHandler.turnHandler == null) {
			TurnHandler.turnHandler = new TurnHandler();
		}
		return TurnHandler.turnHandler;
	}
}