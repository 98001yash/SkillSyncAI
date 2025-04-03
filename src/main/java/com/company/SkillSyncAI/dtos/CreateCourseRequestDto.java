package com.company.SkillSyncAI.dtos;


import com.company.SkillSyncAI.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequestDto {

    private String title;
    private String description;
    private Category category;
}
