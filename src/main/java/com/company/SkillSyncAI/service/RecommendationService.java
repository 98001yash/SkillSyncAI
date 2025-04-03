//package com.company.SkillSyncAI.service;
//
//
//import com.company.SkillSyncAI.dtos.CourseDto;
//import com.company.SkillSyncAI.entities.Course;
//import com.company.SkillSyncAI.entities.Enrollment;
//import com.company.SkillSyncAI.repository.CourseRepository;
//import com.company.SkillSyncAI.repository.EnrollmentRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class RecommendationService {
//
//    private final EnrollmentRepository enrollmentRepository;
//    private final CourseRepository courseRepository;
//    private final ModelMapper modelMapper;
//
//    public List<CourseDto> recommendCourse(Long userId){
//        log.info("Fetching course recommendation for user {}",userId);
//
//        // get enrolled courses
//        List<Enrollment> enrollments = enrollmentRepository.findByLearner_Id(userId);
//        if(enrollments.isEmpty()){
//            log.warn("User {} has no enrolled courses, retuning popular courses",userId);
//            return getPopularCourses();
//        }
//
//        // get categories of enrolled courses
//        List<String> enrolledCategories = Optional.ofNullable(enrollments)
//                .orElse(Collections.emptyList()) // If enrollments is null, use an empty list
//                .stream()
//                .map(enrollment -> enrollment.getCourse() != null ? enrollment.getCourse().getCategory() : null)
//                .filter(Objects::nonNull)
//                .distinct()
//                .collect(Collectors.toList());
//
//
//
//        // find similar courses in the same categories
//        List<Course> recommendedCourses = courseRepository.findByCategoryIn(enrolledCategories);
//
//        // Convert to DTO and return
//        return recommendedCourses.stream()
//                .map(course -> modelMapper.map(course, CourseDto.class))
//                .collect(Collectors.toList());
//    }
//
//    private List<CourseDto> getPopularCourses() {
//        List<Course> popularCourses = courseRepository.findTop5ByOrderByEnrollmentsDesc();
//        return popularCourses.stream()
//                .map(course -> modelMapper.map(course, CourseDto.class))
//                .collect(Collectors.toList());
//    }
//}
