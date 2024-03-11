package org.example.weneedbe.domain.application.application;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.application.domain.Application;
import org.example.weneedbe.domain.application.domain.Recruit;
import org.example.weneedbe.domain.application.dto.request.ApplicationFormRequest;
import org.example.weneedbe.domain.application.dto.request.ApplicationResultRequest;
import org.example.weneedbe.domain.application.dto.request.RecruitFormRequest;
import org.example.weneedbe.domain.application.dto.response.ApplicationFormResponse;
import org.example.weneedbe.domain.application.dto.response.ApplicationInfoResponse;
import org.example.weneedbe.domain.application.dto.response.RecruitFormResponse;
import org.example.weneedbe.domain.application.exception.ApplicationNotFoundException;
import org.example.weneedbe.domain.application.exception.RecruitNotFoundException;
import org.example.weneedbe.domain.application.repository.ApplicationRepository;
import org.example.weneedbe.domain.application.repository.RecruitRepository;
import org.example.weneedbe.domain.article.application.ArticleService;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.bookmark.service.BookmarkService;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.service.UserService;
import org.example.weneedbe.global.s3.application.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {
    private final RecruitRepository recruitRepository;
    private final UserService userService;
    private final ArticleService articleService;
    private final BookmarkService bookmarkService;
    private final S3Service s3Service;
    private final ApplicationRepository applicationRepository;

    public void createRecruitForm(String authorizationHeader, Long articleId, RecruitFormRequest request) {

        Article article = articleService.findArticle(articleId);
        User user = userService.findUser(authorizationHeader);

        Recruit recruit = Recruit.of(article, user, request);

        recruitRepository.save(recruit);
    }

    public RecruitFormResponse getRecruitForm(String authorizationHeader, Long articleId) {

        User user = userService.findUser(authorizationHeader);

        Article article = articleService.findArticle(articleId);
        int heartCount = articleService.countHeartByArticle(article);
        int bookmarkCount = bookmarkService.countBookmarkByArticle(article);

        Recruit recruit = recruitRepository.findByArticle_ArticleId(articleId).orElseThrow(RecruitNotFoundException::new);

        return new RecruitFormResponse(user, article, heartCount, bookmarkCount, recruit);
    }

    public void createApplicationForm(String authorizationHeader, Long articleId, MultipartFile appeal, ApplicationFormRequest request) throws IOException {
        User user = userService.findUser(authorizationHeader);
        Recruit recruit = recruitRepository.findByArticle_ArticleId(articleId).orElseThrow(RecruitNotFoundException::new);

        String appealUrl = s3Service.uploadFile(appeal);

        Application application = Application.of(recruit, user, request, appealUrl);

        applicationRepository.save(application);
    }

    public ApplicationFormResponse getApplicationForm(String authorizationHeader, Long applicationId) {
        User user = userService.findUser(authorizationHeader);
        Application application = applicationRepository.findById(applicationId).orElseThrow(ApplicationNotFoundException::new);

        return new ApplicationFormResponse(application.getUser(), application, user, application.getRecruit());
    }

    public void updateApplicationStatus(Long applicationId, ApplicationResultRequest request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(ApplicationNotFoundException::new);
        application.updateResult(request.getResult());

        applicationRepository.save(application);
    }

    public List<List<ApplicationInfoResponse>> getApplications(Long recruitId) {
        List<Application> applications = applicationRepository.findAllByRecruit_RecruitId(recruitId);

        List<ApplicationInfoResponse> acceptedApplications = sortApplicationsByStatus(applications, "수락함");
        List<ApplicationInfoResponse> pendingApplications = sortApplicationsByStatus(applications, "대기중");
        List<ApplicationInfoResponse> refusedApplications = sortApplicationsByStatus(applications, "거절함");

        return Arrays.asList(acceptedApplications, pendingApplications, refusedApplications);
    }

    private List<ApplicationInfoResponse> sortApplicationsByStatus(List<Application> applications, String status) {
        return applications.stream()
                .filter(application -> application.getResult().getStatus().equals(status))
                .map(application -> new ApplicationInfoResponse(application.getUser(), application))
                .toList();
    }
}