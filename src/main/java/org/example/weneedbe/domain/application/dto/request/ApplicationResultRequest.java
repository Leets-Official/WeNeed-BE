package org.example.weneedbe.domain.application.dto.request;

import lombok.Getter;
import org.example.weneedbe.domain.application.domain.Status;

@Getter
public class ApplicationResultRequest {
    private Status result;
}
