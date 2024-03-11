package org.example.weneedbe.domain.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    PENDING("대기중"),
    ACCEPTED("수락함"),
    REFUSED("거절함");

    private final String status;
}
