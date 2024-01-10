package org.example.weneedbe.domain.article.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    PORTFOLIO("PORTFOLIO"),
    RECRUITING("RECRUITING");

    private final String type;

}
