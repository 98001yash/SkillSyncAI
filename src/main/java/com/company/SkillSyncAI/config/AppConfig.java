package com.company.SkillSyncAI.config;


import com.company.SkillSyncAI.dtos.EnrollmentDto;
import com.company.SkillSyncAI.entities.Course;
import com.company.SkillSyncAI.entities.Enrollment;
import com.company.SkillSyncAI.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
