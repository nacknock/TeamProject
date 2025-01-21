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
@Table(name = "product_order",uniqueConstraints={
        @UniqueConstraint(
                name="product_order_pk",
                columnNames={"id"}
        )
})
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name="option_id",nullable = false)
    private ProductOption option_id;

    @ManyToOne
    @JoinColumn(name="buyer_id",nullable = false)
    private Buyer buyer_id;

    @ManyToOne
    @JoinColumn(name="payment_id",nullable = false)
    private Payment payment_id;

    @Column(length = 200, nullable = false)
    private String receiver;

    @Column(nullable = false)
    private long amount;

    @Column(length = 2000, nullable = false)
    private String address;

    @Column(length = 2000, nullable = false)
    private String phone_number;

    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    private boolean status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

}
