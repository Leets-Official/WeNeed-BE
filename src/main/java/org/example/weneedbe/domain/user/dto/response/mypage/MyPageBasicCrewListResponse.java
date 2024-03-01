package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyPageBasicCrewListResponse {
    private List<MyPageArticleInfoResponse> recruitCrewList;
    private List<MyPageArticleInfoResponse> appliedCrewList;

    public static MyPageBasicCrewListResponse of(List<MyPageArticleInfoResponse> recruitCrewList,
                                                 List<MyPageArticleInfoResponse> appliedCrewList) {
        return MyPageBasicCrewListResponse.builder()
                .recruitCrewList(recruitCrewList)
                .appliedCrewList(appliedCrewList)
                .build();
    }
}
