package org.example.weneedbe.domain.user.dto.request;

import lombok.Data;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.Fields;

@Data
public class UserInfoRequest {
    private String nickname;
    private int userGrade;
    private Department major;
    private Department doubleMajor;
    private Fields interestField;
}
