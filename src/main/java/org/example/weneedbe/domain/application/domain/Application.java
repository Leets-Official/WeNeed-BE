package org.example.weneedbe.domain.application.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.weneedbe.domain.application.dto.request.ApplicationFormRequest;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.global.shared.entity.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department doubleMajor;

    @Column(nullable = false)
    private boolean international;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String aboutMe;

    @Column(nullable = false)
    private String appeal;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status result;

    @ElementCollection
    @CollectionTable(name = "application_keywords", joinColumns = @JoinColumn(name = "application_id"))
    private List<String> keywords = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "crew_answers", joinColumns = @JoinColumn(name = "application_id"))
    private List<String> crewAnswers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    public static Application of(Recruit recruit, User user, ApplicationFormRequest request, String appeal){
        return Application.builder()
                .name(request.getName())
                .major(request.getMajor())
                .doubleMajor(request.getDoubleMajor())
                .international(request.isInternational())
                .grade(request.getGrade())
                .status(request.getStatus())
                .phone(request.getPhone())
                .aboutMe(request.getAboutMe())
                .appeal(appeal)
                .content(request.getContent())
                .result(Status.PENDING)
                .keywords(request.getKeywords())
                .crewAnswers(request.getCrewAnswers())
                .recruit(recruit)
                .user(user).build();
    }

    public void updateResult(Status result) {
        this.result = result;
    }
}
