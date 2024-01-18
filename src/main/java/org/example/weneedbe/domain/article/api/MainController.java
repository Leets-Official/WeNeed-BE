package org.example.weneedbe.domain.article.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.application.MainService;
import org.example.weneedbe.domain.article.dto.response.main.MainPortfolioDto;
import org.example.weneedbe.domain.article.dto.response.main.MainRecruitDto;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Main Controller", description = "메인페이지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @Operation(summary = "메인페이지 - 포트폴리오", description = "메인페이지에서 포트폴리오부분 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/portfolio")
    ResponseEntity<MainPortfolioDto> getMainPagePortfolio(
            @RequestParam int size, @RequestParam int page, @RequestParam String sort, @RequestParam String[] detailTags) {
        return ResponseEntity.ok(mainService.getPortfolioArticleList(size, page, sort, detailTags));
    }

    @Operation(summary = "메인페이지 - 리크루팅", description = "메인페이지에서 리크루팅 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/recruit")
    ResponseEntity<MainRecruitDto> getMainPageRecruit(
            @RequestParam int size, @RequestParam int page, @RequestParam String[] detailTags) {
        return ResponseEntity.ok(mainService.getRecruitArticleList(size, page, detailTags));
    }
}
