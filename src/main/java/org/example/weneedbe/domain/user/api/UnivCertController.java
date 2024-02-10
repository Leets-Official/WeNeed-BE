package org.example.weneedbe.domain.user.api;

import com.univcert.api.UnivCert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.user.dto.request.CertifyCodeRequest;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "Mail Certification Controller", description = "메일 인증 관련 API입니다.")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UnivCertController {
    private final static String UNIV_NAME = "가천대학교";
    @Value("${univcertKey}")
    private String apiKey;

    @Operation(summary = "학교 메일 조회", description = "입력한 이메일이 가천대학교 소속 이메일인지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/certify")
    public ResponseEntity<Map<String, Object>> checkMailWithUniv(@RequestParam String email) throws IOException {
        log.info("\nemail:{}", email);
        return ResponseEntity.ok(UnivCert.certify(apiKey, email, UNIV_NAME, true));
    }

    @Operation(summary = "인증 코드 입력", description = "입력한 이메일에 발송한 입력 코드를 토대로 메일을 인증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/certifycode")
    public ResponseEntity<Map<String, Object>> checkVerificationCode(@RequestBody CertifyCodeRequest request) throws IOException {
        return ResponseEntity.ok(UnivCert.certifyCode(apiKey, request.getEmail(), UNIV_NAME, request.getCode()));
    }

    @Operation(summary = "인증된 메일 목록 초기화", description = "인증된 모든 메일 목록을 초기화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearAllMail() throws IOException {
        return ResponseEntity.ok(UnivCert.clear(apiKey));
    }
}
