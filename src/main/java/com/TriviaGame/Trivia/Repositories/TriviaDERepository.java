package com.TriviaGame.Trivia.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.TriviaGame.Trivia.Entities.TriviaDE;

@Repository
public interface TriviaDERepository extends JpaRepository<TriviaDE, Long> {
	
}