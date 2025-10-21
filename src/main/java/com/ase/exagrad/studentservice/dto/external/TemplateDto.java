package com.ase.exagrad.studentservice.dto.external;

import lombok.Data;

@Data
public class TemplateDto {
    private String name;
    private String code;
    private boolean elective;
    private int planned_semester;
    private int id;
}
