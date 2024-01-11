package org.example.weneedbe.domain.article.dto.response;

import lombok.Getter;
import org.example.weneedbe.domain.user.domain.User;

@Getter
public class MemberInfoResponse {
  private Long userId;
  private String nickname;

  public MemberInfoResponse(User user) {
    this.userId = user.getUserId();
    this.nickname = user.getNickname();
  }
}
