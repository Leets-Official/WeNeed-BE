package org.example.weneedbe.domain.application.dto.response;

import lombok.Getter;
import org.example.weneedbe.domain.application.domain.Application;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.User;

import java.util.List;

@Getter
public class ApplicationFormResponse {
    private String nickname;
    private boolean sameUser;
    private UserDetailDto user;
    private ApplicationFormDto applicationForm;

    public ApplicationFormResponse(User user, Application application, String nickname, boolean sameUser){
        this.nickname = nickname;
        this.sameUser = sameUser;
        this.user = new UserDetailDto(user);
        this.applicationForm = new ApplicationFormDto(application);
    }

    @Getter
    public static class UserDetailDto {
        private Long userId;
        private String profile;
        private String nickname;
        private Department major;
        private Integer grade;

        public UserDetailDto(User user) {
            this.userId = user.getUserId();
            this.profile = user.getProfile();
            this.nickname = user.getNickname();
            this.major = user.getMajor();
            this.grade = user.getGrade();
        }
    }

    @Getter
    public static class ApplicationFormDto{
        private String name;
        private Department major;
        private Department doubleMajor;
        private boolean international;
        private Integer grade;
        private String status;
        private String phone;
        private String aboutMe;
        private String content;
        private List<String> keywords;
        private List<String> crewAnswers;
        private String appeal;

        public ApplicationFormDto(Application application){
            this.name = application.getName();
            this.major = application.getMajor();
            this.doubleMajor = application.getDoubleMajor();
            this.international = application.isInternational();
            this.grade = application.getGrade();
            this.status = application.getStatus();
            this.phone = application.getPhone();
            this.aboutMe = application.getAboutMe();
            this.content = application.getContent();
            this.keywords = application.getKeywords();
            this.crewAnswers = application.getCrewAnswers();
            this.appeal = application.getAppeal();
        }
    }

}