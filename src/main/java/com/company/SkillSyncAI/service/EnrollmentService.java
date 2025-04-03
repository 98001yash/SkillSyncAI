package com.company.SkillSyncAI.service;


import com.company.SkillSyncAI.dtos.CourseDto;
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
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public List<CourseDto> getEnrolledCourses(Long userId) {
        log.info("Checking if user ID: {} exists", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with ID: " + userId);
                });

        log.info("Fetching enrolled courses for user ID: {}", userId);
        List<Enrollment> enrollments = enrollmentRepository.findByLearner_Id(userId);

        return enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment.getCourse(), CourseDto.class))
                .toList();
    }


    public int getCourseProgress(Long userId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByLearner_idAndCourse_Id(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for User ID: " + userId + " and Course ID: " + courseId));
        return enrollment.getProgress();
    }

    public EnrollmentDto updateCourseProgress(Long userId, Long courseId, int newProgress) throws BadRequestException {
        Enrollment enrollment = enrollmentRepository.findByLearner_idAndCourse_Id(userId, courseId)
                .orElseThrow(()->new ResourceNotFoundException("Enrollment not found for User ID: "+userId+ " and course Id: "+courseId));

        if(newProgress<0 ||newProgress>100){
            throw new BadRequestException("Progress must be between 0 and 100");
        }
        enrollment.setProgress(newProgress);
        if(newProgress==100){
           log.info("User {} has completed the course {}",userId, courseId);
        }
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return modelMapper.map(updatedEnrollment, EnrollmentDto.class);
    }



}
