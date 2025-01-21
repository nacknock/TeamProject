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
@Table(name = "category_detail",uniqueConstraints={
        @UniqueConstraint(
                name="category_detail_pk",
                columnNames={"id"}
        )
})
public class CategoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false,name="category_id")
    private Category category_id;

    @Column(length = 200, nullable = false)
    private String category_detail_name;

}
