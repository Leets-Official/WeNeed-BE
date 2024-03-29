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
import org.example.weneedbe.domain.article.dto.response.main.MainSearchDto;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Main Controller", description = "메인페이지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @Operation(summary = "메인페이지 - 포트폴리오(전체조회)", description = "메인페이지에서 포트폴리오부분(전체) 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/portfolio/all")
    ResponseEntity<MainPortfolioDto> getMainPagePortfolioAll(
            @RequestParam int size, @RequestParam int page, @RequestParam(defaultValue = "DESC") String sort, @RequestParam(required = false, defaultValue = "ALL") String detailTags,
            @RequestHeader(name = "Authorization", required = false)  String authorizationHeader) {
        return ResponseEntity.ok(mainService.getPortfolioArticleList(size, page, sort, detailTags, authorizationHeader));
    }
    @Operation(summary = "메인페이지 - 포트폴리오(태그조회)", description = "메인페이지에서 포트폴리오부분(태그포함) 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/portfolio")
    ResponseEntity<MainPortfolioDto> getMainPagePortfolio(
            @RequestParam int size, @RequestParam int page, @RequestParam(defaultValue = "DESC") String sort, @RequestParam(required = false) String detailTags,
            @RequestHeader(name = "Authorization", required = false)  String authorizationHeader) {
        return ResponseEntity.ok(mainService.getPortfolioArticleList(size, page, sort, detailTags, authorizationHeader));
    }

    @Operation(summary = "메인페이지 - 리크루팅(전체조회)", description = "메인페이지에서 리크루팅(전체) 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/recruit/all")
    ResponseEntity<MainRecruitDto> getMainPageRecruitAll(
            @RequestParam int size, @RequestParam int page, @RequestParam(required = false, defaultValue = "ALL") String detailTags,
            @RequestHeader(name = "Authorization", required = false)  String authorizationHeader) {
        return ResponseEntity.ok(mainService.getRecruitArticleList(size, page, detailTags, authorizationHeader));
    }

    @Operation(summary = "메인페이지 - 리크루팅(태그조회)", description = "메인페이지에서 리크루팅(태그포함) 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/recruit")
    ResponseEntity<MainRecruitDto> getMainPageRecruit(
            @RequestParam int size, @RequestParam int page, @RequestParam(required = false) String detailTags,
            @RequestHeader(name = "Authorization", required = false)  String authorizationHeader) {
        return ResponseEntity.ok(mainService.getRecruitArticleList(size, page, detailTags, authorizationHeader));
    }

    @Operation(summary = "메인페이지 - 검색", description = "메인페이지에서 키워드로 검색하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    ResponseEntity<MainSearchDto> getMainPageSearch(
            @RequestParam int size, @RequestParam int page, @RequestParam String keyword,
            @RequestHeader(name = "Authorization", required = false)  String authorizationHeader) {
        return ResponseEntity.ok(mainService.getSearchArticleList(size, page, keyword, authorizationHeader));
    }
}
