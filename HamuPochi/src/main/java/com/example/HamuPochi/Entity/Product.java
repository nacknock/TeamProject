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
@Table(name = "product",uniqueConstraints={
        @UniqueConstraint(
                name="product_pk",
                columnNames={"id"}
        )
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name="seller_id",nullable = false)
    private Seller seller_id;

    @ManyToOne
    @JoinColumn(name="category_id",nullable = false)
    private Category category_id;

    @ManyToOne
    @JoinColumn(name="category_detail_id")
    private CategoryDetail category_detail_id;

    @Column(length = 2000, nullable = false)
    private String title;

    @Column(length = 60000, nullable = false,columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private long price;

    @Column(length = 2000, nullable = false)
    private String thumbnail_url;

    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    private boolean status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

    @Column(nullable = false)
    private long amount;

}
