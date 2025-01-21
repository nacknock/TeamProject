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
@Table(name = "product_notice",uniqueConstraints={
        @UniqueConstraint(
                name="product_notice_pk",
                columnNames={"id"}
        )
})
public class ProductNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name="seller_id",nullable = false)
    private Seller seller_id;

    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    private Product product_id;

    @Column(length = 2000, nullable = false)
    private String title;

    @Column(length = 20000, nullable = false,columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

}
