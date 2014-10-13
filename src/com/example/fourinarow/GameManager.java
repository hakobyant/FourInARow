package com.example.fourinarow;

public class GameManager {
	private static GameManager instance = null;
	public GameProcessActivity gameProcessActivity;
	public HelpActivity helpActivity;
	public MainActivity mainActivity;
	public PickOpponentActivity pickOpponentActivity;
	public SettingsActivity settingsActivity;

	private GameManager() {
	}

	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}
}
