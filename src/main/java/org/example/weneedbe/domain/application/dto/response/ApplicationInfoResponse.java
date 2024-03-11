package org.example.weneedbe.domain.application.dto.response;

import lombok.Getter;
import org.example.weneedbe.domain.application.domain.Application;
import org.example.weneedbe.domain.application.domain.Status;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.User;

import java.time.LocalDateTime;

@Getter
public class ApplicationInfoResponse {
    private final UserDetailDto user;
    private final Long applicationId;
    private final LocalDateTime appliedAt;
    private final Status result;

    public ApplicationInfoResponse(User user, Application application) {
        this.user = new UserDetailDto(user);
        this.applicationId = application.getApplicationId();
        this.appliedAt = application.getCreatedAt();
        this.result = application.getResult();
    }

    @Getter
    public static class UserDetailDto {
        private final Long userId;
        private final String profile;
        private final String nickname;
        private final Department major;
        private final Integer grade;

        public UserDetailDto(User user) {
            this.userId = user.getUserId();
            this.profile = user.getProfile();
            this.nickname = user.getNickname();
            this.major = user.getMajor();
            this.grade = user.getGrade();
        }
    }
}


