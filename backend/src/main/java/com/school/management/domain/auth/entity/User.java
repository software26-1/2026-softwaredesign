package com.school.management.domain.auth.entity;

import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
//@getter 모든 필드의 getter 자동 생성
//@NoArgsConstructor 파라미터 필요없는 기본 생성자 자동 생성
//@Entity 이 클래스가 DB테이블과 매핑된다는 선언
//@Table(name="users") 매핑할 테이블 이름 지정

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id  //기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) //db의 auto increment 에 맡김
    private Long id;

    @Column(nullable = false) //이름
    private String username;

    @Column(nullable = false, unique = true) //이메일,중복불가
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING) //문자열로 enum db에 저장
    @Column(nullable = false)
    private Role role;

    public enum Role {
        ADMIN, TEACHER, STUDENT, PARENT
    }

    public User(String username, String email, String password,
                String phoneNumber, String name, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.role = role;
    }

    // 비밀번호 변경 (BCrypt 인코딩된 값을 받아야 함)
    // 엔티티 안에 넣는 이유: password 필드가 private이라 외부에서 직접 못 바꿈
    // 서비스에서 passwordEncoder.encode(newPassword) 한 결과를 넘겨야 함
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
