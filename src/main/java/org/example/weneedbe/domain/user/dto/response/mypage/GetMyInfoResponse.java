package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.Fields;
import org.example.weneedbe.domain.user.domain.User;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetMyInfoResponse {
    private String profile;
    private String nickname;
    private Department major;
    private int userGrade;
    private Department doubleMajor;
    private Fields interestField;
    private String email;
    private List<String> links;
    private String selfIntro;

    public static GetMyInfoResponse from(User user) {
        return GetMyInfoResponse.builder()
                .profile(user.getProfile())
                .nickname(user.getNickname())
                .major(user.getMajor())
                .userGrade(user.getGrade())
                .doubleMajor(user.getDoubleMajor())
                .interestField(user.getInterestField())
                .email(user.getEmail())
                .links(user.getLinks())
                .selfIntro(user.getAboutMe())
                .build();
    }
}
