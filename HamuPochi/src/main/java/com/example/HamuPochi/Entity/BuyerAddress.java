package com.example.HamuPochi.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "buyer_address",uniqueConstraints={
        @UniqueConstraint(
                name="buyer_addr_pk",
                columnNames={"id"}
        )
})
public class BuyerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer_id;

    @Column(length = 2000, nullable = false)
    private String prefecture; //都道府県

    @Column(length = 2000, nullable = false)
    private String city;//市区町村

    @Column(length = 2000)
    private String block_number; //丁目・番地・号

    @Column(length = 2000)
    private String building_name; //建物名／会社名・部屋番号

    @Column(length = 2000, nullable = false)
    private String post_number;

    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    private boolean status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;




}
