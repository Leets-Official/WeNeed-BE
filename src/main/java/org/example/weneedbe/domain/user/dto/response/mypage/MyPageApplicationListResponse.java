package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.weneedbe.domain.article.dto.response.main.PageableDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyPageApplicationListResponse {
    private List<MyPageApplicationInfoResponse> applicationInfoResponses;
    private PageableDto pageableDto;

    public static MyPageApplicationListResponse of(List<MyPageApplicationInfoResponse> applicationInfoResponses, PageableDto pageableDto) {
        return MyPageApplicationListResponse.builder()
                .applicationInfoResponses(applicationInfoResponses)
                .pageableDto(pageableDto)
                .build();
    }
}
