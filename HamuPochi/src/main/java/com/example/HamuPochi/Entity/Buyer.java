package com.example.HamuPochi.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
//@Table name = 테이블명 지정, @UniqueConstraint name=제약조건 이름,columnNames = primary key 이름
@Table(name = "buyer",uniqueConstraints={
        @UniqueConstraint(
                name="buyer_pk",
                columnNames={"id"}
        )
})
//파일명 == 테이블명
public class Buyer {

    //@Id,@GeneratedValue(strategy = GenerationType.IDENTITY) 는 primary key에만 붙이기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    //@Column = 컬럼의 세부 설정
    //length = 200 = 컬럼의 length
    //nullable = false = not null
    //@Column(length = 200, nullable = false) private String email; == email/varchar(200) not null 조건의 컬럼 생성
    //변수 타입별 컬럼 타입 변화
    // 정수형 = int or Bigint
    // String = Varchar
    // LocalDateTime = TIMESTAMP
    @Column(length = 200, nullable = false)
    private String email;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 200, nullable = false)
    private String nickname;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 100)
    private String phone_number;

    //@CreatedDate는 insert시 날짜 자동생성 해주는 어노테이션. 생성 날짜 컬럼에는 꼭 붙일것
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;

    //@LastModifiedDate는 update시 날짜 자동생성 해주는 어노테이션. 수정 날짜 컬럼에는 꼭 붙일것
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

}
