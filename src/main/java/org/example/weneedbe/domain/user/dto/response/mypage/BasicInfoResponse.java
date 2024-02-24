package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.weneedbe.domain.article.dto.response.main.PageableDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BasicInfoResponse {
    private String userNickname;
    private Boolean sameUser;
    private GetMyInfoResponse userInfo;
    private List<MyPageArticleInfoResponse> myOutputList;
    private PageableDto pageableDto;

    public static BasicInfoResponse of(String userNickname, Boolean sameUser, GetMyInfoResponse userInfo, List<MyPageArticleInfoResponse> myOutputList, PageableDto pageableDto) {
        return BasicInfoResponse.builder()
                .userNickname(userNickname)
                .sameUser(sameUser)
                .userInfo(userInfo)
                .myOutputList(myOutputList)
                .pageableDto(pageableDto)
                .build();
    }
}