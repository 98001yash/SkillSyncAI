package com.company.SkillSyncAI.service;


import com.company.SkillSyncAI.dtos.EnrollmentDto;
import com.company.SkillSyncAI.entities.Enrollment;
import com.company.SkillSyncAI.exceptions.ResourceNotFoundException;
import com.company.SkillSyncAI.exceptions.RuntimeConflictException;
import com.company.SkillSyncAI.repository.CourseRepository;
import com.company.SkillSyncAI.repository.EnrollmentRepository;
import com.company.SkillSyncAI.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.file.ReadOnlyFileSystemException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public EnrollmentDto enrollUser(EnrollmentDto enrollmentDto) {
        log.info("Enrolling user {} in course {}", enrollmentDto.getUserId(), enrollmentDto.getCourseId());

        var user = userRepository.findById(enrollmentDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + enrollmentDto.getUserId()));

        var course = courseRepository.findById(enrollmentDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + enrollmentDto.getCourseId()));

        if (enrollmentRepository.existsByLearner_IdAndCourse_Id(enrollmentDto.getUserId(), enrollmentDto.getCourseId())) {
            throw new RuntimeConflictException("User is already enrolled in this course!");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setLearner(user);
        enrollment.setCourse(course);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        log.info("User {} successfully enrolled in course {}", enrollmentDto.getUserId(), enrollmentDto.getCourseId());

        EnrollmentDto savedDto = new EnrollmentDto();
        savedDto.setUserId(savedEnrollment.getLearner().getId());
        savedDto.setCourseId(savedEnrollment.getCourse().getId());

        return savedDto;
    }
}
