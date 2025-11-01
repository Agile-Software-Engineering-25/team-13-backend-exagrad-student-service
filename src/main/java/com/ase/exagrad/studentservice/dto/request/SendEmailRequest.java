package com.ase.exagrad.studentservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class SendEmailRequest {

    @NotEmpty(message = "Empfänger darf nicht leer sein")
    private List<@Email(message = "Ungültige E-Mail-Adresse") String> to;

    @NotBlank(message = "Betreff darf nicht leer sein")
    private String subject;

    @NotBlank(message = "Text darf nicht leer sein")
    private String text;

    @Email(message = "Ungültige Reply-To-Adresse")
    private String replyTo;
}
