package com.ase.exagrad.studentservice.dto.request;

import lombok.Data;

@Data
public class SendEmailResponse {

    private boolean success;
    private String message;

    public SendEmailResponse() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
