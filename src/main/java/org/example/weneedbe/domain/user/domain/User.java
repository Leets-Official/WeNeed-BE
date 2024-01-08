package org.example.weneedbe.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.weneedbe.global.shared.entity.BaseTimeEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "major", nullable = true)
    private Department major;

    @Enumerated(EnumType.STRING)
    @Column(name = "double_major", nullable = true)
    private Department doubleMajor;

    @Column(name = "user_nickname", nullable = true)
    private String nickname;

    @Column(name = "user_grade", nullable = true)
    private Integer grade;

    @Column(name = "user_age", nullable = true)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_field", nullable = true)
    private Interests interestField;

    @ElementCollection
    @CollectionTable(name = "user_links", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> links;

    @Column(name = "about_me", nullable = true)
    private String aboutMe ;

    @Column(name = "has_registered", nullable = false)
    private LocalDateTime hasRegistered;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "users")
    private List<UserArticle> userArticles = new ArrayList<>();

}
