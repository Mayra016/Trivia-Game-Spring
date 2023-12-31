package com.TriviaGame.Trivia.Services;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.TriviaGame.Trivia.Entities.Trivia;
import com.TriviaGame.Trivia.Repositories.TriviaRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class ExcelReaderService {
	public ExcelReaderService(int initialLevel, int finalLevel, TriviaRepository repository) {
		
	}

    public static void readExcelFile(int initialLevel, int finalLevel, TriviaRepository repository) {
    	int i = initialLevel;
    	String path = "/src/main/resources/static/Trivia 30.04.xlsx";
    	try (InputStream inp = ExcelReaderService.class.getClassLoader().getResourceAsStream(path)) {
            Workbook workbook = new XSSFWorkbook(inp);
            Sheet sheet = workbook.getSheetAt(0);
	    	for (int level = i; level <= finalLevel; level++) {	    	    
	    	    String word = "a";
	    	    String clue1 = "a";
	    	    String clue2 = "a";
	    	    String clue3 = "a";
	    	    System.out.println(word);
	    	    word = sheet.getRow(level).getCell(0).getStringCellValue();
	    	    clue1 = sheet.getRow(level).getCell(1).getStringCellValue();
	    	    clue2 = sheet.getRow(level).getCell(2).getStringCellValue();
	    	    clue3 = sheet.getRow(level).getCell(3).getStringCellValue();
	    	    Trivia newLevel = new Trivia((long) level, clue1, clue2, clue3, word);
	    		System.out.println(word);
	    		System.out.println("Dentro del try");
	    	    repository.save(newLevel);
	    	}    
    	} catch (IOException e) {
    	    	System.out.println("exception");
    	    	e.printStackTrace();
    	    
    	    
    	    
    	}
    }
    
}    