package com.ase.exagrad.studentservice.dto.external;

import lombok.Data;

@Data
public class CourseDto {
    private int semester;
    private String exam_type;
    private int credit_points;
    private int total_units;
    private int template_id;
    private int id;
    private TemplateDto template;
}
