package com.company.SkillSyncAI.service;


import com.company.SkillSyncAI.dtos.CourseDto;
import com.company.SkillSyncAI.dtos.CreateCourseRequestDto;
import com.company.SkillSyncAI.dtos.CreateCourseResponseDto;
import com.company.SkillSyncAI.entities.Category;
import com.company.SkillSyncAI.entities.Course;
import com.company.SkillSyncAI.entities.User;
import com.company.SkillSyncAI.enums.Role;
import com.company.SkillSyncAI.exceptions.ResourceNotFoundException;
import com.company.SkillSyncAI.repository.CourseRepository;
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
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public CreateCourseResponseDto createCourse(Long mentorId, CreateCourseRequestDto requestDto) throws BadRequestException {
        log.info("creating a course for mentor ID: {}",mentorId);

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(()->new ResourceNotFoundException("Mentor not found with ID: "+mentorId));

        if(!mentor.getRoles().contains(Role.MENTOR)){
            throw new BadRequestException("User is not mentor and cannot create courses.");
        }

        Course course = new Course();
        course.setTitle(requestDto.getTitle());
        course.setDescription(requestDto.getDescription());
        course.setMentor(mentor);
        course.setCategory(requestDto.getCategory()); // Set category

        Course savedCourse = courseRepository.save(course);
        log.info("Course created successfully with id: {}", savedCourse.getId());
        return new CreateCourseResponseDto(
                savedCourse.getId(),
                savedCourse.getTitle(),
                savedCourse.getDescription(),
                mentor.getName()
        );
    }

    public CourseDto getCourseById(Long courseId){
        log.info("Fetching course details for course ID: {}",courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(()->new ResourceNotFoundException("course not found with ID: "+courseId));
        return modelMapper.map(course, CourseDto.class);
    }

    public List<CourseDto> getAllCourses(){
        log.info("Fetching all the courses......");
        List<Course> courses = courseRepository.findAll();
        return courses
                .stream()
                .map(course->modelMapper.map(course, CourseDto.class))
                .toList();
    }

    public List<CourseDto> getCoursesByCategory(String category) {
        log.info("Fetching all courses for category: {}", category);
        List<Course> courses = courseRepository.findByCategory(category);
        return courses.stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .toList();
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto){
        log.info("Fetching course with ID: {}",id);
        Course course = courseRepository.findById(id)
                .orElseThrow(()->{
                    log.error("Course not found with ID: {}",id);
                    return new ResourceNotFoundException("Course not found with ID: "+id);
                });

        modelMapper.map(courseDto, course);
        Course updatedCourse = courseRepository.save(course);
        log.info("Course with ID: {} updated successfully", id);
        return modelMapper.map(updatedCourse, CourseDto.class);
    }

        public CourseDto updateCourseCategory(Long courseId, Category newCategory) {
        log.info("Updating category for course ID: {} to {}", courseId, newCategory);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
        course.setCategory(newCategory);
        Course updatedCourse = courseRepository.save(course);
        log.info("Course ID: {} category updated successfully", courseId);
        return modelMapper.map(updatedCourse, CourseDto.class);
    }

    public void deleteCourse(Long id){
        log.info("Checking if course with ID: {} exists",id);
        Course course = courseRepository.findById(id)
                .orElseThrow(()->{
                    log.error("Course not found with ID: {}",id);
                    return new ResourceNotFoundException("Course not found with ID: "+id);
                });
        courseRepository.delete(course);
        log.info("Course with ID: {} deleted successfully",id);
    }
}
