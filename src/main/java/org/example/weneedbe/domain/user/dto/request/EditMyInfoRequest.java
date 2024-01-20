package org.example.weneedbe.domain.user.dto.request;

import lombok.Data;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.Fields;

import java.util.List;

@Data
public class EditMyInfoRequest {
    private String profile;
    private String nickname;
    private Department major;
    private Integer userGrade;
    private Department doubleMajor;
    private Fields interestField;
    private List<String> links;
    private String selfIntro;
}
