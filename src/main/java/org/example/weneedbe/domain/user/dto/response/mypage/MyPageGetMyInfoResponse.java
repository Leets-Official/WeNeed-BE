package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.Fields;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyPageGetMyInfoResponse {
    private String profile;
    private String nickname;
    private Department major;
    private int userGrade;
    private Department doubleMajor;
    private Fields interestField;
    private String email;
    private List<String> links;
    private String selfIntro;
}
