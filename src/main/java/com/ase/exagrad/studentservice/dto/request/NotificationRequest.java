package com.ase.exagrad.studentservice.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
  private List<String> users;
  private String title;
  private String message;
  private String notifyType;
  private String notificationType;
}
