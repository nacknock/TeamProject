package com.example.HamuPochi.Repository;

import com.example.HamuPochi.Entity.Answer;
import com.example.HamuPochi.Entity.Buyer;
import com.example.HamuPochi.Repository.Custom.AnswerRepositoryCustom;
import com.example.HamuPochi.Repository.Custom.BuyerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> , AnswerRepositoryCustom {
}
