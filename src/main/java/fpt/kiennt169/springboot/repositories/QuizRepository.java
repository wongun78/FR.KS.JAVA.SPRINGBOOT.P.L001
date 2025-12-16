package fpt.kiennt169.springboot.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.kiennt169.springboot.entities.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    
    boolean existsByTitle(String title);
}
