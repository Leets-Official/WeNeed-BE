package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MainRecruitDto {
    private MainUserDto user;
    private PageableDto pageable;
    private List<RecruitArticleDto> recruitList;
}
