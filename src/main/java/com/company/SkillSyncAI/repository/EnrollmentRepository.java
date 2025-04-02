package com.company.SkillSyncAI.repository;

import com.company.SkillSyncAI.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByLearner_IdAndCourse_Id(Long userId, Long courseId);
}
