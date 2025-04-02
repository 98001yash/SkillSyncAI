package com.company.SkillSyncAI.service;


import com.company.SkillSyncAI.dtos.CourseDto;
import com.company.SkillSyncAI.dtos.CreateCourseRequestDto;
import com.company.SkillSyncAI.dtos.CreateCourseResponseDto;
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
}
