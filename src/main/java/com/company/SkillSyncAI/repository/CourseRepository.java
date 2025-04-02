package com.company.SkillSyncAI.repository;

import com.company.SkillSyncAI.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {

    List<Course> findByMentorId(Long mentorId);
}
