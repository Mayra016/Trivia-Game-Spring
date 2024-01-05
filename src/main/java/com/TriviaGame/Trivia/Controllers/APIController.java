package com.TriviaGame.Trivia.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.TriviaGame.Trivia.Interfaces.TriviaI;
import com.TriviaGame.Trivia.Services.TriviaService;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin(origins = {
        "https://peculiaridadesdelmundo.blogspot.com",
        "https://peculiaridadesdomundoblog.blogspot.com",
        "https://besonderheitenderwelt.blogspot.com",
        "https://peculiaritiesoftheworld.blogspot.com",
        "http://localhost:8080"
}, maxAge = 3600)
@RestController
public class APIController {
	
	@Autowired
	TriviaService service;
	
    @GetMapping("/getLetters")
    @ResponseBody
    public Map<String, Object> getLetters() {
    	TriviaI currentGame = service.activeTimeOption();    	
        System.out.println(currentGame.getLetters() + "Letters al llamar al /getLevel");
        Map<String, Object> response = new HashMap<>();
        response.put("letters", currentGame.getLetters());
    	return response;
    }
}