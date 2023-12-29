package com.TriviaGame.Trivia.Entities;
import java.util.ArrayList;
import java.util.List;

import com.TriviaGame.Trivia.Interfaces.TriviaI;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;


@Entity
@Table(name="triviaPT")
public class TriviaPT implements TriviaI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("level")
    private Long level;
    @Column
    @JsonProperty("clue1")
    private String clue1;
    @Column
    @JsonProperty("clue2")
    private String clue2;
    @Column
    @JsonProperty("clue3")
    private String clue3;
    @Column
    @JsonProperty("word")
    private String word;
    
    @Transient
    private byte lifes;
    @Transient
    private boolean alive;
    @Transient
    private List<Long> playedLevels = new ArrayList<>();
    @Transient
    private int score;
    @Transient
    private String underscores;
    @Transient
    private String userInput;
    @Transient
    private long startTime;
    @Transient
    private String letters;
    @Transient
    private byte scoreMultiplier;
    
    public TriviaPT() {
    	
    }
    
    public TriviaPT(Long level, String clue1, String clue2, String clue3, String word) {       
    	this.startTime = System.currentTimeMillis();
    	this.level = level;
        this.clue1 = "1. " + clue1;
        this.clue2 = "2. " + clue2;
        this.clue3 = "3. " + clue3;
        this.word = word;
        lifes = (byte) 3;
        this.alive = true;
        this.playedLevels.add(level);
        setUnderscores(word);
        
    }
    
    public void setScoreMultiplier( byte newScoreMultiplier) {
    	this.scoreMultiplier = newScoreMultiplier;
    }
    
    public byte getScoreMultiplier() {
    	return this.scoreMultiplier;
    }
    
    public void setLetters(String newLetters) {
    	this.letters = newLetters;
    }
    
    public String getLetters() {
    	return this.letters;
    }
    
    public void setStartTime() {
    	this.startTime = System.currentTimeMillis();;
    }
    
    public long getStartTime() {
    	return this.startTime;
    }
    
    public void setUserInput(String newUserInput) {
    	this.userInput = newUserInput;
    }
    
    public String getUserInput() {
    	return this.userInput;
    }
    
    public void setUnderscores(String word2) {
        StringBuilder newUnderscores = new StringBuilder();
    
        for (byte x = 0; x < word2.length(); x++) {
        	newUnderscores.append("_");
            
            if (x < word2.length() - 1) {
            	newUnderscores.append(" ");
            }
        }
		this.underscores = newUnderscores.toString();
	}
    
    public String getUnderscores() {
    	return this.underscores;
    }

	public void setLevel(Long newLevel) {
        this.level = newLevel;
    }
    
    public Long getLevel() {
        return this.level;
    }
    
    public void setClue1(String newClue1) {
        this.clue1 = newClue1;
    }
    
    public String getClue1() {
        return this.clue1;
    }
    
    public void setClue2(String newClue2) {
         this.clue2 = newClue2;
    }
    
    public String getClue2() {
        return this.clue2;
    }
    
    public void setClue3(String newClue3) {
        this.clue3 = newClue3;
    }
    
    public String getClue3() {
        return this.clue3;
    }
    
    public void setWord(String newWord) {
        this.word = newWord;
    }
    
    public void setLifes(int i) {
        this.lifes = (byte) i;
    }

    public byte getLifes() {
        return lifes;
    }
    
    public void setAlive(boolean newState) {
        this.alive = newState;
    }
    
    public boolean getAlive() {
        return this.alive;
    }

    public void setPlayedLevels(List<Long> levels) {
    	if (levels!=null) {
    		this.playedLevels = levels;
    	}	
    }
    
    public List<Long> getPlayedLevels() {
        return this.playedLevels;
    }

	public String getWord() {
		return this.word;
	}
	
	public void setScore(int newScore) {
		this.score = newScore;
	}
	
	public int getScore() {
		return this.score;
	}
}
