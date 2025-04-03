package com.company.SkillSyncAI.dtos;

import com.company.SkillSyncAI.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {


    private Long id;
    private String title;
    private String description;
    private Long mentorId;
}
