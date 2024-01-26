package org.example.weneedbe.domain.token.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.token.dto.request.TokenRequest;
import org.example.weneedbe.domain.token.dto.response.TokenResponse;
import org.example.weneedbe.global.error.ErrorResponse;
import org.example.weneedbe.global.jwt.TokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Token Controller", description = "토큰 재발급 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenProvider tokenProvider;

    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 대조하여 액세스 및 리프레시 토큰을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> regenerateToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(tokenProvider.regenerateToken(request.getRefreshToken()));
    }
}
