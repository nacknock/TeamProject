package com.example.HamuPochi.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "answer",uniqueConstraints={
        @UniqueConstraint(
                name="answer_pk",
                columnNames={"id"}
        )
})
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question_id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller_id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin_id;

    @Column(length = 20000, nullable = false,columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

}
