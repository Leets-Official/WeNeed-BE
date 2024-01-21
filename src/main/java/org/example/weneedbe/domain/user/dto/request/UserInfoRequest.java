package org.example.weneedbe.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.Fields;

@Getter
@AllArgsConstructor
public class UserInfoRequest {
    private String nickname;
    private Integer userGrade;
    private Department major;
    private Department doubleMajor;
    private Fields interestField;
}
