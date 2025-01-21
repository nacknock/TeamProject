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
@Table(name = "notice",uniqueConstraints={
        @UniqueConstraint(
                name="notice_pk",
                columnNames={"id"}
        )
})
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name="admin_id",nullable = false)
    private Admin admin_id;

    @Column(length = 2000, nullable = false)
    private String title;

    @Column(length = 20000, nullable = false,columnDefinition = "TEXT")
    private String content;

    @Column(length = 20000, nullable = false,columnDefinition = "TEXT")
    private String notice_picture_url;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

}
