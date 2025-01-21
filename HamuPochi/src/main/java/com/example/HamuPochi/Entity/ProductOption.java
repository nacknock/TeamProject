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
@Table(name = "product_option",uniqueConstraints={
        @UniqueConstraint(
                name="product_option_pk",
                columnNames={"id"}
        )
})
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(length = 200, nullable = false)
    private String option_name;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean status; //0=비활성,1=활성

    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    private Product product_id;

}
