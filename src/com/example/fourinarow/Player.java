package com.example.fourinarow;

import android.util.Log;

public class Player {
	public final int DEFAULT_SCORE = 1000;

	private int playerID;
	private String playerUsername;
	private int playerScore;
	private String playerColor;
	private String opponentColor;
	private boolean isSoundOn;
	private boolean isPlayerOnline;


	private Player currentOpponentPlayer;

	public Player() {
		playerID = -1;
		playerUsername = "New Player";
		playerScore = DEFAULT_SCORE;
		playerColor = "red";
		opponentColor = "yellow";
		isSoundOn = true;
		isPlayerOnline = false;
	}

	public Player(final int id, final String username, final int score) {
		playerID = id;
		playerUsername = username;
		playerScore = score;
		playerColor = "red";
		opponentColor = "yellow";
		isSoundOn = true;
		isPlayerOnline = false;
	}

	public int getPlayerID() {
		return playerID;
	}

	public String getPlayerUsername() {
		return playerUsername;
	}

	public int getPlayerScore() {
		return playerScore;
	}

	public String getPlayerColor() {
		return playerColor;
	}

	public String getOpponentColor() {
		return opponentColor;
	}

	public boolean getPlayerIsSoundOn() {
		return isSoundOn;
	}

	public Player getCurrentOpponentPlayer() {
		return currentOpponentPlayer;
	}

	public boolean getIsPlayerOnline() {
		return isPlayerOnline;
	}
	
	public void setPlayerID(final int id) {
		playerID = id;
	}

	public void setPlayerUsername(final String usr) {
		playerUsername = usr;
	}

	public void setPlayerScore(final int score) {
		playerScore = score;
	}

	public void setPlayerColor(final String color) {
		playerColor = color;
	}

	public void setOpponentColor(final String color) {
		opponentColor = color;
	}

	public void setIsSoundOn(final boolean isOn) {
		isSoundOn = isOn;
	}
	
	public void setIsPlayerOnline(final boolean isOnline) {
		isPlayerOnline = isOnline;
	}

	// switch opponent colors
	public void setCurrentOpponentPlayer(final Player p) {
		currentOpponentPlayer = new Player();
		currentOpponentPlayer.setPlayer(p.getPlayerID(), p.getPlayerUsername(),
				p.getPlayerScore(), GameManager.getInstance().mainActivity
						.getPlayer().getOpponentColor(), GameManager
						.getInstance().mainActivity.getPlayer()
						.getPlayerColor(),
				GameManager.getInstance().mainActivity.getPlayer()
						.getPlayerIsSoundOn());
	}

	public void setPlayer(final int id, final String usr, final int score) {
		setPlayerID(id);
		setPlayerUsername(usr);
		setPlayerScore(score);
	}

	public void setPlayer(final int id, final String usr, final int score,
			final String playerCol, final String OpponentCol, final boolean isOn) {
		setPlayer(id, usr, score);
		setPlayerColor(playerCol);
		setOpponentColor(OpponentCol);
		setIsSoundOn(isOn);
	}

	public void print() {
		Log.i("Vasa", Integer.toString(playerID) + " " + playerUsername + " "
				+ Integer.toString(playerScore) + " " + playerColor + " "
				+ opponentColor + " "
		// Integer.toString(currentOpponentPlayer.getPlayerID()) + " " +
		// currentOpponentPlayer.getPlayerUsername()
		);
	}
}

