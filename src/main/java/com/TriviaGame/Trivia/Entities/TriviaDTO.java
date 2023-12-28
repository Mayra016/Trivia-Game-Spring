package com.TriviaGame.Trivia.Entities;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TriviaDTO {
	private byte lifes;
	private List<Long> playedLevels;
	
	public TriviaDTO() {
		this.lifes = 4;
	}
	
	public void setLifes(byte newLifes) {
		this.lifes = newLifes;
	}
	
	public byte getLifes() {
		return this.lifes;
	}
	
	// Add all previous levels to played levels
	public void setPlayedLevels(List<Long> newPlayedLevels, Long previousLevel) {
		this.playedLevels = newPlayedLevels;
		this.playedLevels.add(previousLevel);
	}

	public List<Long> getPlayedLevels() {
		return this.playedLevels;
	}
}