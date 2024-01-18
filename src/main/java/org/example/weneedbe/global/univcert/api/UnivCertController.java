package org.example.weneedbe.global.univcert.api;

import com.univcert.api.UnivCert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Mail Certification Controller", description = "메일 인증 관련 API입니다.")
@RestController
@RequestMapping("/api/v1")
@Transactional
@Slf4j
public class UnivCertController {
    private final static String UNIV_NAME = "가천대학교";
    @Value("${univcertKey}")
    private String apiKey;
    private String email;

    @Operation(summary = "학교 메일 조회", description = "입력한 이메일이 가천대학교 소속 이메일인지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/certify")
    public void checkMailWithUniv(@RequestParam String email) throws IOException {
        this.email = email;
        log.info("\nemail:{}", this.email);
        UnivCert.certify(apiKey, email, UNIV_NAME, true);
    }

    @Operation(summary = "인증 코드 입력", description = "입력한 이메일에 발송한 입력 코드를 토대로 메일을 인증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/certifycode")
    public void checkVerificationCode(@RequestParam int code) throws IOException {
        UnivCert.certifyCode(apiKey, email, UNIV_NAME, code);
    }
}
