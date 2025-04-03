package com.company.SkillSyncAI.repository;

import com.company.SkillSyncAI.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByLearner_IdAndCourse_Id(Long userId, Long courseId);
    List<Enrollment> findByLearner_Id(Long userId);

    Optional<Enrollment> findByLearner_idAndCourse_Id(Long userId, Long courseId);

}
