package com.TriviaGame.Trivia.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.TriviaGame.Trivia.Entities.Trivia;
import com.TriviaGame.Trivia.Entities.TriviaDE;
import com.TriviaGame.Trivia.Entities.TriviaDTO;
import com.TriviaGame.Trivia.Entities.TriviaEN;
import com.TriviaGame.Trivia.Entities.TriviaPT;
import com.TriviaGame.Trivia.Interfaces.TriviaI;
import com.TriviaGame.Trivia.Services.TriviaService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;


@Controller
@RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
public class TriviaController {

    @Autowired
    TriviaService service;
    @Autowired
    TriviaDTO persistentData = new TriviaDTO();
    @Autowired
    private LocaleResolver localeResolver = new CookieLocaleResolver();
    String appLanguage;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("text");
    TriviaI trivia;
    TriviaI currentGame;
    
    // READ
    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        
    }
    
    @GetMapping("/menu") 
    public String getMenu() {
    	persistentData = new TriviaDTO();
    	service.setCurrentGameAlive(true);
    	if (trivia!=null) {
    		service.resetLetters();
    	}
    	return "menu";
    }
    
    @GetMapping("languages") 
    public String getLanguages() {
    	return "languages";
    }
    
    @GetMapping("infos")
    public String getInfoPage() {
    	return "infos";
    }
    
    @GetMapping("/menu/{language}")
    public String changeLanguage(@PathVariable String language, HttpServletRequest request) {
    	service.setLanguage(language);
    	this.appLanguage = language;
    	Locale newLocale;
    	if ("ES".equals(language)) {
            newLocale = new Locale("es", "ES");
        } else if ("EN".equals(language)) {
            newLocale = new Locale("en");
        } else if ("PT".equals(language)) {
            newLocale = new Locale("pt", "BR"); 
        } else if ("DE".equals(language)) {
            newLocale = new Locale("de");
        } else {
            newLocale = new Locale("es"); //default
        }
        localeResolver.setLocale(request, null, newLocale);   	
    	return "redirect:/menu";
    }
    
    @GetMapping("levels") 
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getLevels(Model model) {
    	List<Trivia> levels = service.findAll();
    	model.addAttribute("levels", levels);
        return "levels";
    }
    
    @GetMapping("nextLevel")
    public String generateLevel() {
    	return "redirect:/".concat(service.generateLevel().toString());
    }
    
    @GetMapping("lost") 
    public String getLost() {
    	return "lost";
    }
    

    
    // CHECK THE LANGUAGE TO OBTAIN THE LEVEL FROM THE RESPECTIVE DATA BASIS
    @GetMapping("/{level}")
    public String home(@PathVariable Long level, Model model) {
    	List<Long> playedLevelsPersistent;
    	try {
    		playedLevelsPersistent = persistentData.getPlayedLevels();
    		if (playedLevelsPersistent.contains(level)) {
        		
        	} else {
        		trivia = service.getLevel(level);
        	}
    	} catch (Exception e ) {
    		System.out.print(e);
    		trivia = service.getLevel(level);
    	}
    	
        
        TriviaI currentGame = service.activeTimeOption();
        trivia.setLetters(currentGame.getLetters());
    	trivia.setScore(persistentData.getScore());
    	System.out.println(currentGame.getLetters() + "Letters al llamar al /level");
        if (trivia!=null && persistentData.getLifes()==4) {
        	System.out.println("Nuevo nivel lifes: " + trivia.getLifes());
        	System.out.println(trivia.getClue1() + trivia.getAlive() + "\n" + trivia.getLifes());       	
        	System.out.println(trivia.getWord() + "\n" + trivia.getStartTime());       	
            if (currentGame.getAlive()==false) {
            	return "lost";
            }
        } else if (trivia!=null && persistentData.getLifes()<4) {        	
        	System.out.println("Usando el persistentData");
        	trivia.setLifes(persistentData.getLifes());
        	trivia.setPlayedLevels(persistentData.getPlayedLevels());
            if (currentGame.getAlive()==false) {
            	return "lost";
            }
        }
        if (currentGame.getAlive()==false) {
        	return "lost";
        }
        model.addAttribute("trivia", trivia);
        model.addAttribute("currentGame", currentGame);
        model.addAttribute("persistentData", persistentData);
        return "level"; 
    }
    // GET LEVEL ON ENGLISH
    @GetMapping("/EN/{level}")
    public String levelEN(@PathVariable Long level, Model model) {
        TriviaEN trivia = service.getLevelEN(level);
        if (trivia!=null && persistentData.getLifes()==4) {
        	trivia.setStartTime();
        	System.out.println("Nuevo nivel lifes: " + trivia.getLifes());
        	System.out.println(trivia.getClue1() + trivia.getAlive() + "\n" + trivia.getLifes());       	
        	System.out.println(trivia.getWord() + "\n" + trivia.getStartTime());       	

        } else if (trivia!=null && persistentData.getLifes()<4) {        	
        	System.out.println("Usando el persistentData");
        	trivia.setStartTime();
        	trivia.setLifes(persistentData.getLifes());
        	trivia.setPlayedLevels(persistentData.getPlayedLevels());
        }
        model.addAttribute("trivia", trivia);
        model.addAttribute("persistentData", persistentData);
        return "level"; 
    }
    
    // GET LEVEL ON PORTUGUESE
    @GetMapping("/PT/{level}")
    public String levelPT(@PathVariable Long level, Model model) {
        TriviaPT trivia = service.getLevelPT(level);
        if (trivia!=null && persistentData.getLifes()==4) {
        	trivia.setStartTime();
        	System.out.println("Nuevo nivel lifes: " + trivia.getLifes());
        	System.out.println(trivia.getClue1() + trivia.getAlive() + "\n" + trivia.getLifes());       	
        	System.out.println(trivia.getWord() + "\n" + trivia.getStartTime());       	

        } else if (trivia!=null && persistentData.getLifes()<4) {        	
        	System.out.println("Usando el persistentData");
        	trivia.setStartTime();
        	trivia.setLifes(persistentData.getLifes());
        	trivia.setPlayedLevels(persistentData.getPlayedLevels());
        }
        model.addAttribute("trivia", trivia);
        model.addAttribute("persistentData", persistentData);
        return "level"; 
    }
    
    // GET LEVEL ON GERMAN
    @GetMapping("/DE/{level}")
    public String levelDE(@PathVariable Long level, Model model) {
        TriviaDE trivia = service.getLevelDE(level);
        if (trivia!=null && persistentData.getLifes()==4) {
        	trivia.setStartTime();
        	System.out.println("Nuevo nivel lifes: " + trivia.getLifes());
        	System.out.println(trivia.getClue1() + trivia.getAlive() + "\n" + trivia.getLifes());       	
        	System.out.println(trivia.getWord() + "\n" + trivia.getStartTime());       	

        } else if (trivia!=null && persistentData.getLifes()<4) {        	
        	System.out.println("Usando el persistentData");
        	trivia.setStartTime();
        	trivia.setLifes(persistentData.getLifes());
        	trivia.setPlayedLevels(persistentData.getPlayedLevels());
        }
        model.addAttribute("trivia", trivia);
        model.addAttribute("persistentData", persistentData);
        return "level"; 
    }
    
    @GetMapping("/checkAnswer/{level}/{userInput}")
    public String checkAnswer(@PathVariable Long level, @PathVariable String userInput, Model model) {
    	TriviaI currentGame = service.activeTimeOption();
        if (userInput==null) {
        	userInput = "";
        }
        if (persistentData.getLifes()!=4) {
	        trivia.setLifes(persistentData.getLifes());
	        trivia.setPlayedLevels(persistentData.getPlayedLevels());
	        trivia.setScore(persistentData.getScore());
	        trivia.setLetters(currentGame.getLetters());
        }    
        if (trivia!=null) {
        	
        	System.out.println("Lifes (antes): " + trivia.getLifes());
        	byte lifes = trivia.getLifes();
        	trivia.setScore(persistentData.getScore());
        	System.out.println(trivia.getClue1());
        	model.addAttribute("trivia", trivia);
        	boolean win = service.compareAnswer(level, userInput); 
        	System.out.println(win);
        	if ( win &&  lifes > 0 ) {
        		System.out.println(win + "lifes>0");
        		System.out.println("Score" + trivia.getScore());
        		Long nextLevel = service.chooseNextLevel(trivia);
        		persistentData.setLifes(lifes);
        		persistentData.setScore(persistentData.getScore()+currentGame.getScoreMultiplier());
        		persistentData.setPlayedLevels(trivia.getPlayedLevels(), level);
        		trivia.setLetters("");     
        		model.addAttribute("trivia", trivia);
        		model.addAttribute("persistentData", persistentData);
        		return "redirect:/".concat(nextLevel.toString());
        	} else if ( win == false && lifes > 0) {
        		System.out.println("repuesta" + win + "lifes>0");        	
        		trivia = service.reduceLife(lifes, trivia);
        		persistentData.setLifes(trivia.getLifes());
        		System.out.println("Lifes persistentData: " + persistentData.getLifes());
        		System.out.println(trivia.getLetters());
        		model.addAttribute("trivia", trivia);
        		model.addAttribute("persistentData", persistentData);
        		return "level";
        	}
        	if ( win == false && lifes <= 0) {
        		trivia.getPlayedLevels().clear();
	        	trivia = new Trivia();
	        	trivia = null;
	        	model.addAttribute("persistentData", persistentData);
	        	return "lost";
	        	
        	}
        	System.out.println("Lifes: " + trivia.getLifes());
        	
        	model.addAttribute("trivia", trivia);
        }
        
		return "level";	
    }
    
    
    // CREATE
    @PostMapping("/add/{level}/{clue1}/{clue2}/{clue3}/{word}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addNewLevelDB(@PathVariable Long level, @PathVariable String clue1, @PathVariable String clue2, @PathVariable String clue3, @PathVariable String word) {
    	try {
    		service.addNewLevel(level, clue1, clue2, clue3, word);
    		return "El level " + level + "fue agregado a la base de datos";
    	} catch (Exception e) {
    		return "Hubo un problema al agregar el level " + level;
    	}
    }
    
    @GetMapping("updateData/{secret_pass}/{initialLevel}/{finalLevel}/{language}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateDataBaseLevels(@PathVariable String secret_pass, @PathVariable int initialLevel, @PathVariable int finalLevel, @PathVariable String language) {
    	String code = resourceBundle.getString("PASS");
    	if (secret_pass.equals(code)) {
	    	try {
	    		service.updateDataBase(initialLevel, finalLevel, language);
	            return "redirect:/levels";
	    	} catch (Exception e) {
	    		return "redirect:/levels";
	    	}
    	} else {
    		return "No tienes permiso";
    	}
    }
    
    // UPDATE
    @PutMapping("/update/{level}/{clue1}/{clue2}/{clue3}/{word}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateThisLevel(@PathVariable Long level, @PathVariable String clue1, @PathVariable String clue2, @PathVariable String clue3, @PathVariable String word){
        try {
        	service.updateLevel(level, clue1, clue2, clue3, word);  
        	return "El level " + level + " fue actualizado. \n Pista 1: " + clue1 + "\nPista 2: " + clue2 + "\nPista 3: " + clue3 + "\nLa palabra es: " + word;
        } catch (Exception e ) {
        	return "Hubo un problema al actualizar el level " + level;
        }
    }
    
    // 
    @DeleteMapping("/delete/{level}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteThisLevel(@PathVariable Long level) {
        try {
        	service.deleteLevel(level);
        	return "Se deletó el level " + level;
        } catch (Exception e ) {
        	return "Hubo un problema al deletar el level " + level;
        }
    }
    
    

}
