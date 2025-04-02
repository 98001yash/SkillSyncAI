package com.company.SkillSyncAI.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseResponseDto {

    private Long id;
    private String title;
    private String description;
    private String mentorName;
}
