package org.example.weneedbe.domain.article.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.dto.response.main.MainPortfolioDto;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Main Controller", description = "메인페이지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class MainController {

    @Operation(summary = "메인페이지 - 포트폴리오", description = "메인페이지에서 포트폴리오부분 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/portfolio")
    ResponseEntity<MainPortfolioDto> getMainPagePortfolio(
            @RequestParam int size, @RequestParam int page, @RequestParam String sort, @RequestParam List<String> category){


    }
}
