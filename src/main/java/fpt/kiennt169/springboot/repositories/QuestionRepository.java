package fpt.kiennt169.springboot.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.kiennt169.springboot.entities.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    
    
}
