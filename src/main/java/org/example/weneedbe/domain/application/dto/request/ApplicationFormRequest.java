package org.example.weneedbe.domain.application.dto.request;

import lombok.Getter;
import java.util.List;

@Getter
public class ApplicationFormRequest {

    private String name;
    private String major;
    private String doubleMajor;
    private boolean international;
    private Integer grade;
    private String status;
    private String phone;
    private String aboutMe;
    private String content;
    private List<String> keywords;
    private List<String> crewAnswers;

}
