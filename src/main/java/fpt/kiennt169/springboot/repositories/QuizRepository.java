package fpt.kiennt169.springboot.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.kiennt169.springboot.entities.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    
    boolean existsByTitle(String title);
    
    @EntityGraph(attributePaths = {"questions", "questions.answers"})
    Optional<Quiz> findById(UUID id);
    
    @EntityGraph(attributePaths = {"questions", "questions.answers"})
    List<Quiz> findAll();
    
    @EntityGraph(attributePaths = {"questions", "questions.answers"})
    Page<Quiz> findAll(Pageable pageable);
}
