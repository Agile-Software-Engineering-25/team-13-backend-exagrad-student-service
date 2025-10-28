package com.ase.exagrad.studentservice.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class SendEmailResponse {

    private boolean success;
    private String message;
}
