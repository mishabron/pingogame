package com.bronshteyn.android.pingo.model;

import com.bronshteyn.cardsgame.model.AuthinticateResponse;

public class Game {

	private static Game game;
	private Long cardId;
	private String authTocken;
	private Integer guesses = 0;
	private Integer triesLeft1 = 4;
	private Integer triesLeft2 = 4;
	private Integer triesLeft3 = 4;
	private Integer triesLeft4 = 4;
	private Boolean cardPlayed;
	public static final int TOATL_TRIALS = 4;
	public static final int MAX_NUMBER = 9;

	protected Game() {

	}

	public static Game getInstance() {

		if (game == null) {
			game = new Game();
		}

		return game;
	}

	public void init(AuthinticateResponse result) {

		cardId = result.getCardID();
		authTocken = result.getAuthToken();
		cardPlayed = result.getCardPlayed();
		guesses = result.getGuesses();
		triesLeft1 = result.getTriesleft1();
		triesLeft2 = result.getTriesleft2();
		triesLeft3 = result.getTriesleft3();
		triesLeft4 = result.getTriesleft4();
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getAuthTocken() {
		return authTocken;
	}

	public void setAuthTocken(String authTocken) {
		this.authTocken = authTocken;
	}

	public Integer getGuesses() {
		return guesses;
	}

	public synchronized void setGuesses(Integer guesses) {
		this.guesses = guesses;
	}

	public Integer getTriesLeft1() {
		return triesLeft1;
	}

	public synchronized void setTriesLeft1(Integer triesLeft1) {
		this.triesLeft1 = triesLeft1;
	}

	public Integer getTriesLeft2() {
		return triesLeft2;
	}

	public synchronized void setTriesLeft2(Integer triesLeft2) {
		this.triesLeft2 = triesLeft2;
	}

	public Integer getTriesLeft3() {
		return triesLeft3;
	}

	public synchronized void setTriesLeft3(Integer triesLeft3) {
		this.triesLeft3 = triesLeft3;
	}

	public Integer getTriesLeft4() {
		return triesLeft4;
	}

	public synchronized void setTriesLeft4(Integer triesLeft4) {
		this.triesLeft4 = triesLeft4;
	}

	public Boolean isCardPlayed() {
		return cardPlayed;
	}

	public void setCardPlayed(Boolean cardPlayed) {
		this.cardPlayed = cardPlayed;
	}

	public synchronized Integer getTrials() {
		int trials = triesLeft1;

		if (triesLeft2 < trials) {
			trials = triesLeft2;
		}
		if (triesLeft3 < trials) {
			trials = triesLeft3;
		}
		if (triesLeft4 < trials) {
			trials = triesLeft4;
		}

		return trials;
	}
}
