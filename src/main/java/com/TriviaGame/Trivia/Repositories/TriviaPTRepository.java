package com.TriviaGame.Trivia.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.TriviaGame.Trivia.Entities.TriviaPT;

@Repository
public interface TriviaPTRepository extends JpaRepository<TriviaPT, Long> {
	
}