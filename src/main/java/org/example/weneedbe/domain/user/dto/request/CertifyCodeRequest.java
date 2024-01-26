package org.example.weneedbe.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CertifyCodeRequest {
    private int code;
    private String email;
}
