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
@Table(name = "wishlist",uniqueConstraints={
        @UniqueConstraint(
                name="wishlist_pk",
                columnNames={"id"}
        )
})
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    private Product product_id;

    @ManyToOne
    @JoinColumn(name="buyer_id",nullable = false)
    private Buyer buyer_id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

}
