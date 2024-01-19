package org.example.weneedbe.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static Fields from (String value) {
        for (Fields field : Fields.values()) {
            if (field.getFields().equals(value)) {
                return field;
            }
        }
        return null;
    }
}
