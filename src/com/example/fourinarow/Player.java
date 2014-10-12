package com.example.fourinarow;

public class Player {
	public final int DEFAULT_SCORE = 1000;

	private int playerID;
	private String playerUsername;
	private int playerScore;
	private String playerColor;
	private String opponentColor;
	private boolean isSoundOn;

	private Player currentOpponentPlayer;

	public Player() {
		playerID = -1;
		playerUsername = "0";
		playerScore = DEFAULT_SCORE;
		playerColor = "Red";
		opponentColor = "Yellow";
		isSoundOn = true;
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

	public void setCurrentOpponentPlayer(final Player p) {
		currentOpponentPlayer = new Player();
		currentOpponentPlayer.setPlayer(p.getPlayerID(), p.getPlayerUsername(),
				p.getPlayerScore(), p.getPlayerColor(), p.getOpponentColor(),
				p.getPlayerIsSoundOn());
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
}
