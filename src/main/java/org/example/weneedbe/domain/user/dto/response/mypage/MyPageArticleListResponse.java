package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.weneedbe.domain.article.dto.response.main.PageableDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyPageArticleListResponse {
    private List<MyPageArticleInfoResponse> myOutputList;
    private PageableDto pageableDto;

    public static MyPageArticleListResponse of(List<MyPageArticleInfoResponse> myOutputList, PageableDto pageableDto) {
        return MyPageArticleListResponse.builder()
                .myOutputList(myOutputList)
                .pageableDto(pageableDto)
                .build();
    }
}
