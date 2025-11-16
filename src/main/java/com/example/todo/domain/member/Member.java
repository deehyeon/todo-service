package com.example.todo.domain.member;

import com.example.todo.domain.global.AbstractEntity;
import com.example.todo.domain.global.vo.Email;
import com.example.todo.domain.member.enumerate.MemberRole;
import com.example.todo.domain.member.persistence.EmailAttributeConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "email", nullable = false, length = 30, unique = true)
    @NaturalId
    @Convert(converter = EmailAttributeConverter.class)
    private Email email;

    @Column(name = "password", nullable = false)
    private String hashedPassword;

    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.USER;

    private Member(String name, Email email, String hashedPassword, MemberRole role) {
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public static Member create(String name, String email, String hashedPassword) {
        return new Member(name, new Email(email), hashedPassword, MemberRole.USER);
    }

    public static Member createAdmin(String name, String email, String hashedPassword) {
        return new Member(name, new Email(email), hashedPassword, MemberRole.ADMIN);
    }
}