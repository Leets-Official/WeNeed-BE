package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BasicInfoResponse {
    private String userNickname;
    private Boolean sameUser;
    private GetMyInfoResponse userInfo;
    private List<MyPageArticleInfoResponse> myOutputList;

    public static BasicInfoResponse from(String userNickname, Boolean sameUser, GetMyInfoResponse userInfo, List<MyPageArticleInfoResponse> myOutputList) {
        return BasicInfoResponse.builder()
                .userNickname(userNickname)
                .sameUser(sameUser)
                .userInfo(userInfo)
                .myOutputList(myOutputList)
                .build();
    }
}