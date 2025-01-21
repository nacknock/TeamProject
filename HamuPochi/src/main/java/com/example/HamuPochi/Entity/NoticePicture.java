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
@Table(name = "notice_picture",uniqueConstraints={
        @UniqueConstraint(
                name="notice_picture_pk",
                columnNames={"id"}
        )
})
public class NoticePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice_id;

    @Column(length = 20000, nullable = false,columnDefinition = "TEXT")
    private String notice_picture_url;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

}
