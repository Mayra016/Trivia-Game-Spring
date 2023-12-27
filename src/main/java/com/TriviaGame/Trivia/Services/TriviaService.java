package com.TriviaGame.Trivia.Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.TriviaGame.Trivia.Entities.Trivia;
import com.TriviaGame.Trivia.Repositories.TriviaRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class TriviaService {

    @Autowired
    TriviaRepository repository;
    Long currentLevelService;
    Trivia currentGame;
    
    // CRUD
    
    // CREATE LEVEL   
    public void addNewLevel(Long level, String clue1, String clue2, String clue3, String word) {
        Trivia newLevel = new Trivia(level, clue1, clue2, clue3, word);
        repository.save(newLevel);
    }
    
    // GENERATE FIRST LEVEL
    public Long generateLevel() {
    	Long levelsNum = repository.count() - 1;
    	Random randomize = new Random();
    	Long nextLevel = randomize.nextLong(1, levelsNum);
    	Optional<Trivia> game = repository.findById(nextLevel);
    	this.currentGame = game.get();
    	return nextLevel;
    }
    
    // READ LEVEL
    public Trivia getLevel(Long level) {
        Optional<Trivia> game = repository.findById(level);
        Trivia game2 = game.get();
        Trivia newGame = new Trivia(level, game2.getClue1(), game2.getClue2(), game2.getClue3(), game2.getWord());
        this.currentLevelService = level;
        this.currentGame = game.get();
        return newGame;
    }
    
    // GET ALL LEVELS
    public List<Trivia> findAll() {
        return this.repository.findAll();
    }
    
    // GET SCORE
    public int calculateScore(Long level) {
    	Optional<Trivia> game = repository.findById(level);
        if (game.isPresent()) {
        	Trivia updateGame = game.get();
        	int size = (updateGame.getPlayedLevels().size()) - 1;
        	if (updateGame.getScoreMultiplier()>=1) {
        		updateGame.setScore(size * updateGame.getScoreMultiplier());
        	} else {
        		updateGame.setScore(size * 10);
        	}       	
        	return updateGame.getScore();
        }	
        else {
        	return 0;
        }
    }
    
    // UPDATE LEVEL
    public void updateLevel(Long level, String clue1, String clue2, String clue3, String word) {
        Optional<Trivia> game = repository.findById(level);
        if (game.isPresent()) {
        	Trivia updateGame = game.get();
        	updateGame.setClue1(clue1);
        	updateGame.setClue2(clue2);
        	updateGame.setClue3(clue3);
        	updateGame.setWord(word);
        	updateGame.setLevel(level);
        }    
    }
    
    // UPDATE DATA BASE
    public void updateDataBase(int initialLevel, int finalLevel) throws Exception {
	    try {
	        // Opción 2: Utilizar la variable 'inp' obtenida del ClassLoader directamente
	        Resource resource = new ClassPathResource("static/Trivia 30.04.xlsx");
	        File file = resource.getFile();
	        try (InputStream inp = new FileInputStream(file)) {
	            Workbook workbook = new XSSFWorkbook(inp);
	            Sheet sheet = workbook.getSheetAt(0);

	            for (int level = initialLevel; level <= finalLevel; level++) {
	                String word = sheet.getRow((int) level).getCell(0).getStringCellValue();
	                String clue1 = sheet.getRow((int) level).getCell(1).getStringCellValue();
	                String clue2 = sheet.getRow((int) level).getCell(2).getStringCellValue();
	                String clue3 = sheet.getRow((int) level).getCell(3).getStringCellValue();
	                Long newLong= Long.valueOf(level);
			    	Trivia newLevel = new Trivia((long) level, clue1, clue2, clue3, word);
			    	System.out.println(word);
			    	System.out.println("Dentro del try");
			    	repository.save(newLevel);
    	            
    	        }   
	        }   
	    } catch (IOException e) {
    	    	System.out.println("exception");
    	    	throw e;
    	} catch (Exception p) {
    	    	System.out.println(p);
    	    	throw p;
    	}
    	    
    	    
    }
    
    
    // DELETE LEVEL
    public void deleteLevel(Long level) {
        Optional<Trivia> levelToDelete = repository.findById(level);
        if (levelToDelete.isPresent()) {
        	Trivia toDelete = levelToDelete.get();
        	repository.delete(toDelete);
        }
    }
    
    // COMPARE ANSWER
    public boolean compareAnswer(Long level, String userInput) {
        Optional<Trivia> currentLevel = repository.findById(level);
        if (currentLevel.isPresent()) {
        	Trivia game = currentLevel.get();
	        String levelAnswer = game.getWord();
	        if( levelAnswer.equalsIgnoreCase(userInput) ) {
	            return true;
	        } else {
	            return false;
	        }
        } else {
        	return false;
        }
    }
    
    // CHECK TIME 
    @Scheduled(fixedDelay = 1000)
    public void activeTimeOptions() {
    	if (currentLevelService != null) {
    		Optional<Trivia> currentLevel = repository.findById(currentLevelService);
    		
	        if (currentLevel.isPresent()) {
	        	Trivia game = currentLevel.get();
	        	String word = game.getWord();
	    		String letters = game.getLetters();
	        	long currentTime = System.currentTimeMillis() - game.getStartTime();
	        	// In 9 seconds will the first clue letter be displayed on screen
	        	if (TimeUnit.MILLISECONDS.toSeconds(currentTime) == 9 && letters.length() < 1) {       		
	        		letters = letters.concat(Character.toString(word.charAt(0)));
	        		game.setScoreMultiplier((byte)30);
	        	}
	        	
	        	// In 22 seconds will the third clue letter be displayed on screen
	        	if (TimeUnit.MILLISECONDS.toSeconds(currentTime) == 22 && letters.length() < 2) { 
	        		letters = letters.concat(" ");
	        		letters = letters.concat(Character.toString(word.charAt(2)));
	        		game.setScoreMultiplier((byte)20);
	        	}
	        	
	        	// In 40 seconds will the last clue letter be displayed on screen
	        	if (TimeUnit.MILLISECONDS.toSeconds(currentTime) == 40 && letters.length() < word.length()) { 
	        		byte spaces = (byte) (word.length() - 4);
	        		for (byte i = 1; i <= spaces; i++) {
	        			letters.concat(" ");
	        		}      		
	        		letters.concat(Character.toString(word.charAt(word.length()-1)));
	        		game.setScoreMultiplier((byte)10);
	        	}
	        	if (TimeUnit.MILLISECONDS.toMinutes(currentTime) >= 3) {       		
	        		game.setAlive(false);
	        	}
	        }	
        }
        
    }
    
    // REDUCE LIFE
    public Trivia reduceLife(byte lifes, Trivia level) {
        if (lifes > 0) {
        	level.setLifes(lifes-1);
        	currentGame.setLifes(lifes - 1);
        } else {
        	level.setAlive(false);
        }  
        return level;
    }
    
    // DETERMINE IF THE PLAYER HAS LOST
    public boolean hasLost(Long level) {
        Optional<Trivia> currentLevel = repository.findById(level);
        Trivia game = currentLevel.get();
        return game.getAlive();
    }
    
    // DETERMINE THE NEXT LEVEL
    public Trivia chooseNextLevel(Trivia game) {
        Random randomize = new Random();
        Long levels = repository.count() - 1;
        Long nextLevel = randomize.nextLong(1, levels);
        Long currentLevel = game.getLevel();
        List<Long> playedLevels = game.getPlayedLevels();
        while(game.getPlayedLevels().contains(nextLevel) || nextLevel == currentLevel) {
            nextLevel = randomize.nextLong(1, 100);            
        }
        if (!game.getPlayedLevels().contains(nextLevel)) {
                Optional<Trivia> newLevel = repository.findById(nextLevel);
                Trivia newGame = newLevel.get();
                for (Long level: playedLevels) {
                	if (game.getPlayedLevels().contains(level)) {
                		
                	} else {
                		game.setPlayedLevels(level);
                    	currentGame.setPlayedLevels(currentLevel);
                	}
                	
                }
                game.setLevel(newGame.getLevel());
                game.setClue1(newGame.getClue1());
                game.setClue2(newGame.getClue2());
                game.setClue3(newGame.getClue3());
                game.setWord(newGame.getWord());    
                game.setStartTime();
                game.setScoreMultiplier((byte) 30);
                game.setLifes(currentGame.getLifes());
                currentGame = game;
        }    
        return game;
    }
    
    
    
    
}
