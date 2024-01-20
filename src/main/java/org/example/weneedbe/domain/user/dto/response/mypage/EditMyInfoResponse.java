package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.Fields;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.dto.request.EditMyInfoRequest;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class EditMyInfoResponse {
    private String profile;
    private String nickname;
    private Department major;
    private int userGrade;
    private Department doubleMajor;
    private Fields interestField;
    private String email;
    private List<String> links;
    private String selfIntro;

    public static EditMyInfoResponse from(User user, EditMyInfoRequest request) {
        return EditMyInfoResponse.builder()
                .profile(user.getProfile())
                .userGrade(user.getGrade())
                .nickname(user.getNickname())
                .major(user.getMajor())
                .doubleMajor(user.getDoubleMajor())
                .interestField(user.getInterestField())
                .email(user.getEmail())
                .links(user.getLinks())
                .selfIntro(user.getAboutMe())
                .build();
    }
}
