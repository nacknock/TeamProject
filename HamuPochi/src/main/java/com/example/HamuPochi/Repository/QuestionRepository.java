package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Question;
import com.example.HamuPochi.Repository.Custom.QuestionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> , QuestionRepositoryCustom {
}
