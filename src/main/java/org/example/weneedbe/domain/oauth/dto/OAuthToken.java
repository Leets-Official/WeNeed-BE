package org.example.weneedbe.domain.oauth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class OAuthToken {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
