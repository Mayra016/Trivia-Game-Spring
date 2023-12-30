package com.TriviaGame.Trivia.Interfaces;

import java.util.List;

public interface TriviaI {

	void setStartTime();

	String getClue1();
	String getClue2();
	String getClue3();
	String getWord();

	boolean getAlive();

	byte getLifes();

	long getStartTime();

	void setLifes(int lifes);

	void setPlayedLevels(List<Long> playedLevels);

	Long getLevel();

	List<Long> getPlayedLevels();

	void setClue1(String clue1);
	void setClue2(String clue2);
	void setClue3(String clue3);
	void setWord(String word);

	void setLevel(Long level);

	void setScoreMultiplier(byte newScoreMultiplier);
	byte getScoreMultiplier();

	void setAlive(boolean newAlive);

	int getScore();
	void setScore(int newScore);
	
	String getLetters();
	void setLetters(String newLetters);
	

	
	
}