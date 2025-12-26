package fpt.kiennt169.springboot.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.kiennt169.springboot.entities.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    
    @EntityGraph(attributePaths = {"answers", "quizzes"})
    Optional<Question> findById(UUID id);
    
    @EntityGraph(attributePaths = {"answers"})
    Page<Question> findAll(Pageable pageable);
    
    @EntityGraph(attributePaths = {"answers", "quizzes"})
    List<Question> findByQuizzesId(UUID quizId);
}
