package org.example.weneedbe.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Fields {
    미디어("미디어"),
    기획("기획"),
    디자인("디자인"),
    예술("예술"),
    IT("IT");

    private final String fields;
}
