package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.Getter;
import org.example.weneedbe.domain.application.domain.Application;

import java.util.List;

@Getter
public class MyPageApplicationInfoResponse {

    private final Long applicationId;
    private final String thumbnail;
    private final String title;
    private final int viewCount;
    private final int heartCount;
    private final List<String> teamProfiles;

    public MyPageApplicationInfoResponse(Application application, int heartCount, List<String> teamProfiles) {
        this.applicationId = application.getApplicationId();
        this.thumbnail = application.getRecruit().getArticle().getThumbnail();
        this.title = application.getRecruit().getArticle().getTitle();
        this.viewCount = application.getRecruit().getArticle().getViewCount();
        this.heartCount = heartCount;
        this.teamProfiles = teamProfiles;
    }
}
