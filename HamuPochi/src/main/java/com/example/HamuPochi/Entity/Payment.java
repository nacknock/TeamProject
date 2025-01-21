package com.example.HamuPochi.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "payment",uniqueConstraints={
        @UniqueConstraint(
                name="payment_pk",
                columnNames={"id"}
        )
})
public class Payment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String payment_number;

    @Column(length = 20000, nullable = false,columnDefinition = "TEXT")
    private String approved_at;

    @Column(nullable = false)
    private long total_payment;
}
