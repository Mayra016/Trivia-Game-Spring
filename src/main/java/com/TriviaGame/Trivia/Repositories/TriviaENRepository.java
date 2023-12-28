package com.TriviaGame.Trivia.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.TriviaGame.Trivia.Entities.TriviaEN;

@Repository
public interface TriviaENRepository extends JpaRepository<TriviaEN, Long> {
	
}