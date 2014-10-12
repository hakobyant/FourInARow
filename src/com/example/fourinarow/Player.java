package com.example.fourinarow;

public class Player {
	public static final int DEFAULT_SCORE = 1000;

	private static int playerID = -1;
	private static String playerUsername = "";
	private static int playerScore = DEFAULT_SCORE;
	
	private static Player currentOppponentPlayer;
	
	public int getPlayerID() {
		return playerID;
	}
	
	public String getPlayerUsername() {
		return playerUsername;
	}

	public int getPlayerScore() {
		return playerScore;
	}
	
	public Player getCurrentOpponentPlayer() {
		return currentOppponentPlayer;		
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

	public void setCurrentOpponentPlayer(final Player p) {
		currentOppponentPlayer = p;
	}
	
	public void setPlayer(final int id, final String usr, final int score) {
		setPlayerID(id);
		setPlayerUsername(usr);
		setPlayerScore(score);
	}
}
