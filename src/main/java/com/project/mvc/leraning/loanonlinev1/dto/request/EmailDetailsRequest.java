package com.project.mvc.leraning.loanonlinev1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetailsRequest {
    private String recipient;
    private String messageBody;
    private String subject;
}
