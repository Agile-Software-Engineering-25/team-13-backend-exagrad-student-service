package com.ase.exagrad.studentservice.util;

import com.ase.exagrad.studentservice.dto.request.PubDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.PubDocumentResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public final class TestData {

  // Common constants
  public static final String STUDENT_ID = "STUDENT123";
  public static final String TEST_FILE_NAME = "test.pdf";
  public static final int TEST_YEAR = 2025;
  public static final int TEST_MONTH = 5;
  public static final int TEST_DAY_START = 1;
  public static final int TEST_DAY_END = 10;
  // Dates
  public static final LocalDate START_DATE = LocalDate.of(TEST_YEAR, TEST_MONTH, TEST_DAY_START);
  public static final LocalDate END_DATE = LocalDate.of(TEST_YEAR, TEST_MONTH, TEST_DAY_END);

  private TestData() {
    // utility class
  }

  // Factory methods
  public static PubDocumentRequest createPubDocumentRequest() {
    PubDocumentRequest request = new PubDocumentRequest();
    request.setStudentId(STUDENT_ID);
    request.setStartDate(START_DATE);
    request.setEndDate(END_DATE);
    return request;
  }

  public static PubDocumentResponse createPubDocumentResponse(UUID id) {
    return PubDocumentResponse.builder()
        .id(id)
        .studentId(STUDENT_ID)
        .fileName(TEST_FILE_NAME)
        .uploadDate(ZonedDateTime.now(ZoneId.systemDefault()))
        .startDate(START_DATE)
        .endDate(END_DATE)
        .downloadUrl("http://example.com/download/" + TEST_FILE_NAME)
        .build();
  }
}
