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
@Table(name = "ban",uniqueConstraints={
        @UniqueConstraint(
                name="ban_pk",
                columnNames={"id"}
        )
})
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private Seller seller_id;

    @ManyToOne
    @JoinColumn(name="buyer_id")
    private Buyer buyer_id;

    @Column(length = 200, nullable = false)
    private String reason;

    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    private boolean status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

}
