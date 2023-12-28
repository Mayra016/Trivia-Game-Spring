package com.TriviaGame.Trivia.Controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.TriviaGame.Trivia.Entities.Trivia;
import com.TriviaGame.Trivia.Entities.TriviaDTO;
import com.TriviaGame.Trivia.Services.TriviaService;


@Controller
public class TriviaController {

    @Autowired
    TriviaService service;
    @Autowired
    TriviaDTO persistentData = new TriviaDTO();
    
    // READ
    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        
    }
    
    @GetMapping("menu") 
    public String getMenu() {
    	if (persistentData!=null) {
    		persistentData = new TriviaDTO();
    	}
    
    	return "menu";
    }
    
    @GetMapping("levels") 
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
    
    @GetMapping("/{level}")
    public String home(@PathVariable Long level, Model model) {
        Trivia trivia = service.getLevel(level);
        if (trivia!=null && persistentData.getLifes()==4) {
        	trivia.setStartTime();
        	System.out.println("Nuevo nivel lifes: " + trivia.getLifes());
        	System.out.println(trivia.getClue1() + trivia.getAlive() + "\n" + trivia.getLifes());       	
        	System.out.println(trivia.getWord() + "\n" + trivia.getStartTime());       	

        } else if (trivia!=null && persistentData.getLifes()>4) {        	
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
        Trivia trivia = service.getLevel(level);
        if (userInput==null) {
        	userInput = "";
        }
        if (persistentData.getLifes()!=4) {
	        trivia.setLifes(persistentData.getLifes());
	        trivia.setPlayedLevels(persistentData.getPlayedLevels());
        }    
        if (trivia!=null) {
        	
        	System.out.println("Lifes (antes): " + trivia.getLifes());
        	byte lifes = trivia.getLifes();
        	model.addAttribute("trivia", trivia);
        	boolean win = service.compareAnswer(level, userInput); 
        	System.out.println(win);
        	if ( win &&  lifes > 0 ) {
        		System.out.println(win + "lifes>0");
        		Trivia nextLevel = service.chooseNextLevel(trivia);
        		persistentData.setLifes(lifes);
        		persistentData.setPlayedLevels(trivia.getPlayedLevels(), level);
        		model.addAttribute("trivia", trivia);
        		model.addAttribute("persistentData", persistentData);
        		return "redirect:/".concat(nextLevel.getLevel().toString());
        	} else if ( win == false && lifes > 0) {
        		System.out.println("repuesta" + win + "lifes>0");        	
        		trivia = service.reduceLife(lifes, trivia);
        		persistentData.setLifes(trivia.getLifes());
        		System.out.println("Lifes persistentData: " + persistentData.getLifes());
        		model.addAttribute("trivia", trivia);
        		model.addAttribute("persistentData", persistentData);
        		return "level";
        	}
        	if ( win == false && lifes <= 0) {
        		trivia.getPlayedLevels().clear();
	        	service.calculateScore(level);
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
    public String addNewLevelDB(@PathVariable Long level, @PathVariable String clue1, @PathVariable String clue2, @PathVariable String clue3, @PathVariable String word) {
    	try {
    		service.addNewLevel(level, clue1, clue2, clue3, word);
    		return "El level " + level + "fue agregado a la base de datos";
    	} catch (Exception e) {
    		return "Hubo un problema al agregar el level " + level;
    	}
    }
    
    @GetMapping("updateData/{initialLevel}/{finalLevel}")
    public String updateDataBaseLevels(@PathVariable int initialLevel, @PathVariable int finalLevel) {
    	try {
    		service.updateDataBase(initialLevel, finalLevel);
    		List<Trivia> updatedLevels = service.findAll();
            return "redirect:/levels";
    	} catch (Exception e) {
    		return "redirect:/levels";
    	}
    }
    
    // UPDATE
    @PutMapping("/update/{level}/{clue1}/{clue2}/{clue3}/{word}")
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
    public String deleteThisLevel(@PathVariable Long level) {
        try {
        	service.deleteLevel(level);
        	return "Se deletó el level " + level;
        } catch (Exception e ) {
        	return "Hubo un problema al deletar el level " + level;
        }
    }
    
    

}
