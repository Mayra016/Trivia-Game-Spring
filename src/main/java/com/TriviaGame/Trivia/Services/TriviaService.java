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
import org.springframework.ui.Model;

import com.TriviaGame.Trivia.Entities.Trivia;
import com.TriviaGame.Trivia.Entities.TriviaDE;
import com.TriviaGame.Trivia.Entities.TriviaEN;
import com.TriviaGame.Trivia.Entities.TriviaPT;
import com.TriviaGame.Trivia.Interfaces.TriviaI;
import com.TriviaGame.Trivia.Repositories.TriviaDERepository;
import com.TriviaGame.Trivia.Repositories.TriviaENRepository;
import com.TriviaGame.Trivia.Repositories.TriviaPTRepository;
import com.TriviaGame.Trivia.Repositories.TriviaRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class TriviaService {
	// REPOSITORIES
    @Autowired
    TriviaRepository repository;
    @Autowired
    TriviaDERepository repositoryDE;
    @Autowired
    TriviaPTRepository repositoryPT;
    @Autowired
    TriviaENRepository repositoryEN;    
    
    Long currentLevelService;
    Long levelsNum = Long.valueOf(50);; // Number of levels on this language data basis
    TriviaI currentGame = new Trivia();
    String appLanguage = "ES";
    boolean sameLevel = false;
    
    // Set app language to evaluate from which data basis should be the level infos get from
    public void setLanguage(String language) {
    	this.appLanguage = language;
    }
    
    // Set alive when start to play
    public void setCurrentGameAlive(boolean newState) {
    	this.currentGame.setAlive(newState);
    }
    
    // CRUD
    
    // CREATE LEVEL   
    public void addNewLevel(Long level, String clue1, String clue2, String clue3, String word) {
        Trivia newLevel = new Trivia(level, clue1, clue2, clue3, word);
        repository.save(newLevel);
    }
    
    // GENERATE FIRST LEVEL
    public Long generateLevel() {
    	if ("ES".equals(appLanguage)) {
    		levelsNum = repository.count() - 1;
    	} else if ("PT".equals(appLanguage)) {
    		levelsNum = repositoryPT.count() - 1;
    	} else if ("DE".equals(appLanguage)) {
    		levelsNum = repositoryDE.count() - 1;
    	} else if ("EN".equals(appLanguage)) {
    		levelsNum = repositoryEN.count() - 1;
    	} else {
    		levelsNum = repository.count() - 1;
    	}

    	
    	Random randomize = new Random();
    	Long nextLevel = randomize.nextLong(1, levelsNum);
    	this.currentGame.setLifes((byte) 3);
    	this.currentGame.setPlayedLevels(null);
    	this.currentGame.setAlive(true);
    	this.currentGame.setScoreMultiplier((byte)30);
    	return nextLevel;
    }
    
    // READ LEVEL
    public Trivia getLevel(Long level) {
        this.currentLevelService = level;
        TriviaI game2;
    	if (appLanguage.equals("ES")) {
    		Optional<Trivia> gameES = repository.findById(level);
    		game2 = gameES.get();
    	} else if (appLanguage.equals("PT")) {
    		Optional<TriviaPT> gamePT = repositoryPT.findById(level);
    		game2 = gamePT.get();
    	} else if (appLanguage.equals("DE")) {
    		Optional<TriviaDE> gameDE = repositoryDE.findById(level);
    		game2 = gameDE.get();
    	} else if (appLanguage.equals("EN")) {
    		Optional<TriviaEN> gameEN = repositoryEN.findById(level);
    		game2 = gameEN.get();
    	} else {
    		Optional<Trivia> gameES = repository.findById(level);
    		game2 = gameES.get();
    	}
    	if (game2!=null) {
	    	String clue1 = game2.getClue1().contains("1.") ? removeNumberAndDot(game2.getClue1()) : game2.getClue1();
	        String clue2 = game2.getClue2().contains("2.") ? removeNumberAndDot(game2.getClue2()) : game2.getClue2();
	        String clue3 = game2.getClue3().contains("3.") ? removeNumberAndDot(game2.getClue3()): game2.getClue3();
	        System.out.println("getLevel clue1" + clue1);
	        Trivia newGame = new Trivia(level, clue1, clue2, clue3, game2.getWord());
	        currentGame.setWord(newGame.getWord());   
	        if (this.sameLevel==false) {
	        	currentGame.setLetters("");
	        	currentGame.setStartTime();
	        	currentGame.setAlive(true);
	        	this.sameLevel = true;
	        }	
	        return newGame;
    	} else     {
    		Long nextLevel = chooseNextLevel(game2);
    		getLevel(nextLevel);
    		Trivia gameI = getLevel(nextLevel);
	    	String clue1 = gameI.getClue1().contains("1.") ? removeNumberAndDot(gameI.getClue1()) : gameI.getClue1();
	        String clue2 = gameI.getClue2().contains("2.") ? removeNumberAndDot(gameI.getClue2()) : gameI.getClue2();
	        String clue3 = gameI.getClue3().contains("3.") ? removeNumberAndDot(gameI.getClue3()): gameI.getClue3();
	        System.out.println("getLevel clue1" + clue1);
	        Trivia newGame = new Trivia(level, clue1, clue2, clue3, gameI.getWord());
    		return newGame;
    		
    	}
    }
  
    // It will replace the numbers on the beginning, so that it doesn't show duplicated
    public String removeNumberAndDot(String clue) {
    	System.out.println("Se ejecutó remove");
        return clue.matches("\\d+\\..*") ? clue.replaceFirst("\\d+\\.\\s*", "") : clue;
    }
    
    
	// LOAD PORTUGUESE LEVEL
	public TriviaPT getLevelPT(Long level) {
	    Optional<TriviaPT> game = repositoryPT.findById(level);
	    TriviaPT game2 = game.get();
	    TriviaPT newGame = new TriviaPT(level, game2.getClue1(), game2.getClue2(), game2.getClue3(), game2.getWord());
	    return newGame;		
	}

	// LOAD ENGLISH LEVEL
	public TriviaEN getLevelEN(Long level) {
	    Optional<TriviaEN> game = repositoryEN.findById(level);
	    TriviaEN game2 = game.get();
	    TriviaEN newGame = new TriviaEN(level, game2.getClue1(), game2.getClue2(), game2.getClue3(), game2.getWord());
	    return newGame;		
	}

	// LOAD GERMAN LEVEL
	public TriviaDE getLevelDE(Long level) {
	    Optional<TriviaDE> game = repositoryDE.findById(level);
        TriviaDE game2 = game.get();
        TriviaDE newGame = new TriviaDE(level, game2.getClue1(), game2.getClue2(), game2.getClue3(), game2.getWord());
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
    public void updateDataBase(int initialLevel, int finalLevel, String language) throws Exception {
        try {
            // Opción 2: Utilizar la variable 'inp' obtenida del ClassLoader directamente
            Resource resource = new ClassPathResource("static/Trivia 31.12.xlsx");
            File file = resource.getFile();
            try (InputStream inp = new FileInputStream(file)) {
                Workbook workbook = new XSSFWorkbook(inp);
                Sheet sheet = workbook.getSheetAt(0);
                
                for (int level = initialLevel; level <= finalLevel; level++) {
                    String word = "a", clue1 ="a", clue2="a", clue3="a";

                    if (language.equals("ES")) {
                        word = sheet.getRow((int) level).getCell(0).getStringCellValue();
                        clue1 = sheet.getRow((int) level).getCell(1).getStringCellValue();
                        clue2 = sheet.getRow((int) level).getCell(2).getStringCellValue();
                        clue3 = sheet.getRow((int) level).getCell(3).getStringCellValue();
                    } else if (language.equals("EN")) {
                        word = sheet.getRow((int) level).getCell(22).getStringCellValue();
                        clue1 = sheet.getRow((int) level).getCell(23).getStringCellValue();
                        clue2 = sheet.getRow((int) level).getCell(24).getStringCellValue();
                        clue3 = sheet.getRow((int) level).getCell(25).getStringCellValue();
                    } else if (language.equals("DE")) {
                        word = sheet.getRow((int) level).getCell(18).getStringCellValue();
                        clue1 = sheet.getRow((int) level).getCell(19).getStringCellValue();
                        clue2 = sheet.getRow((int) level).getCell(20).getStringCellValue();
                        clue3 = sheet.getRow((int) level).getCell(21).getStringCellValue();
                    } else if (language.equals("PT")) {
                        word = sheet.getRow((int) level).getCell(10).getStringCellValue();
                        clue1 = sheet.getRow((int) level).getCell(11).getStringCellValue();
                        clue2 = sheet.getRow((int) level).getCell(12).getStringCellValue();
                        clue3 = sheet.getRow((int) level).getCell(13).getStringCellValue();
                    }

                    System.out.println(word);
                    System.out.println("Dentro del try");
                    
                    if ("ES".equals(language)) {
                        Trivia newLevel = new Trivia((long) level, clue1, clue2, clue3, word);
                        repository.save(newLevel);
                    } else if ("EN".equals(language)) {
                        TriviaEN newLevelEN = new TriviaEN((long) level, clue1, clue2, clue3, word);
                        repositoryEN.save(newLevelEN);
                    } else if ("PT".equals(language)) {
                        TriviaPT newLevelPT = new TriviaPT((long) level, clue1, clue2, clue3, word);
                        repositoryPT.save(newLevelPT);
                    } else if ("DE".equals(language)) {
                        TriviaDE newLevelDE = new TriviaDE((long) level, clue1, clue2, clue3, word);
                        repositoryDE.save(newLevelDE);
                    }
                }    
            }           
        } catch (IOException e) {
            System.out.println("exception");
            System.out.println(e);     
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
    	try {
    		if (currentGame!=null) {
    	        String levelAnswer = currentGame.getWord();
    	        if( levelAnswer.equalsIgnoreCase(userInput) ) {
    	        	this.sameLevel = false;
    	            return true;
    	        } else if (userInput.trim().isEmpty() || userInput == null) {
    	        	this.sameLevel = true;
    	        	return false;
    	        } else {
    	        	this.sameLevel = true;
    	            return false;
    	        }
            } else if (userInput==null) {
            	this.sameLevel = true;
            	return false;
            } else {
            	this.sameLevel = true;
            	return false;
            }
    	} catch(Exception e) {
    		this.sameLevel = true;
    		System.out.println(e.toString());
    		return false;
    	}
    }
    
    // CHECK TIME 
    @Scheduled(fixedDelay = 1000)
    public TriviaI activeTimeOption() {
    	if (currentLevelService != null) {
	        if (currentGame!=null && currentGame.getAlive()==true) {
	        	String word = currentGame.getWord();
	    		String letters = currentGame.getLetters();
	        	long currentTime = System.currentTimeMillis() - currentGame.getStartTime();
	        	System.out.println(TimeUnit.MILLISECONDS.toSeconds(currentTime));
	        	// In 9 seconds will the first clue letter be displayed on screen
	        	if (TimeUnit.MILLISECONDS.toSeconds(currentTime) == 9 && letters.length() < 1) {       		
	        		letters = letters.concat(Character.toString(word.charAt(0)));
	                byte spaces = (byte) (word.length() - 1);
	                for (byte i = 1; i <= spaces; i++) {
	                    System.out.println(letters);
	                    letters = letters.concat(" ");
	                }
	                
	                currentGame.setScoreMultiplier((byte)30);
	        		currentGame.setLetters(letters);
	        		System.out.println("9 s" + currentGame.getLetters());
	        	}
	        	
	        	// In 22 seconds will the third clue letter be displayed on screen
	        	if (TimeUnit.MILLISECONDS.toSeconds(currentTime) == 22) { 
	                byte spaces = (byte) (word.length() - 3);
	                
	                letters = letters.substring(0,2);
	                letters = letters.concat(Character.toString(word.charAt(2)));
	                for (byte i = 1; i <= spaces; i++) {
	                    System.out.println(letters);
	                    letters = letters.concat(" ");
	                }
	        		currentGame.setScoreMultiplier((byte)20);
	        		currentGame.setLetters(letters);
	        		System.out.println("22s" +letters);
	        	}
	        	
	        	// In 40 seconds will the last clue letter be displayed on screen
	        	if (TimeUnit.MILLISECONDS.toSeconds(currentTime) == 40) { 
	        		letters = letters.substring(0, word.length()-1);
	                letters = letters.concat(Character.toString(word.charAt(word.length() - 1)));
	        		currentGame.setScoreMultiplier((byte)10);
	        		currentGame.setLetters(letters);
	        	}
	        	if (TimeUnit.MILLISECONDS.toMinutes(currentTime) >= 3) {       		
	        		currentGame.setAlive(false);
	        	}
	        	System.out.println(currentGame.getLetters());
	        }	
        }
    	return currentGame;
        
    }
    
    // REDUCE LIFE
    public TriviaI reduceLife(byte lifes, TriviaI trivia) {
        if (lifes > -1) {
        	trivia.setLifes((byte) (lifes-1));
        	currentGame.setLifes((byte) (lifes - 1));
        } else {
        	trivia.setAlive(false);
        }
        return trivia;
    }
    
    // DETERMINE IF THE PLAYER HAS LOST
    public boolean hasLost(TriviaI game) {
        return game.getAlive();
    }
    
    // DETERMINE THE NEXT LEVEL
    public Long chooseNextLevel(TriviaI trivia) {
        Random randomize = new Random();
        Long nextLevel = randomize.nextLong(1, levelsNum);
        Long currentLevel = trivia.getLevel();
        List<Long> playedLevels = trivia.getPlayedLevels();
        while(playedLevels.contains(nextLevel) || nextLevel == currentLevel) {
            nextLevel = randomize.nextLong(1, 100);            
        } /*
        if (!playedLevels.contains(nextLevel)) {
                //Optional<Trivia> newLevel = repository.findById(nextLevel);
                //TriviaI newGame = newLevel.get();
                TriviaI newGame = getLevel(nextLevel);
               
            	if (appLanguage == "ES") {
            		newLevel = repository.findById(nextLevel);
            		newGame = newLevel.get();
            	} else if (appLanguage == "PT") {
            		Optional<TriviaPT> newLevelPT = repositoryPT.findById(nextLevel);
            		newGame = newLevelPT.get();
            	} else if (appLanguage == "DE") {
            		Optional<TriviaDE> newLevelDE = repositoryDE.findById(nextLevel);
            		newGame = newLevelDE.get();
            	} else if (appLanguage == "EN") {
            		Optional<TriviaEN> newLevelEN = repositoryEN.findById(nextLevel);
            		newGame = newLevelEN.get();
            	}
                
                trivia.setLevel(newGame.getLevel());
                trivia.setClue1(newGame.getClue1());
                trivia.setClue2(newGame.getClue2());
                trivia.setClue3(newGame.getClue3());
                trivia.setWord(newGame.getWord());    
                trivia.setStartTime();
                trivia.setScoreMultiplier((byte) 30);
                trivia.setLifes(currentGame.getLifes());
                currentGame = trivia;
        } */   
        return nextLevel;
    }
    
    
    
    
}
