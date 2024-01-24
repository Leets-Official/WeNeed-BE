package org.example.weneedbe.domain.article.dto.response.main;

import lombok.Getter;

import java.util.List;

@Getter
public class MainSearchDto {
    private MainUserDto user;
    private PageableDto pageable;
    private List<SearchArticleDto> articleList;

    public MainSearchDto(MainUserDto mainUserDto, PageableDto pageableDto, List<SearchArticleDto> articleList){
        this.user = mainUserDto;
        this.pageable = pageableDto;
        this.articleList = articleList;
    }

}


