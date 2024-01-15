package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageableDto {
    private int size;
    private int page;
    private int totalPages;
    private long totalElements;
}
