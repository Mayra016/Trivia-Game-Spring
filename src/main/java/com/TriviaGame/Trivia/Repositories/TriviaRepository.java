package com.TriviaGame.Trivia.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TriviaGame.Trivia.Entities.Trivia;

@Repository
public interface TriviaRepository extends JpaRepository<Trivia, Long> {
	
}