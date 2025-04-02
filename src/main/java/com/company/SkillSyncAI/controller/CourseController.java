package com.company.SkillSyncAI.controller;

import com.company.SkillSyncAI.dtos.CourseDto;
import com.company.SkillSyncAI.dtos.CreateCourseRequestDto;
import com.company.SkillSyncAI.dtos.CreateCourseResponseDto;
import com.company.SkillSyncAI.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;


    @PostMapping("/create/{mentorId}")
    public ResponseEntity<CreateCourseResponseDto> createCourse(
            @PathVariable Long mentorId,
            @RequestBody CreateCourseRequestDto requestDto) throws BadRequestException {

        log.info("Received request to create a course by mentor ID:{}",mentorId);
        CreateCourseResponseDto response = courseService.createCourse(mentorId, requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long courseId){
        log.info("Fetching course details for course ID: {}",courseId);
        CourseDto courseDto = courseService.getCourseById(courseId);
        return ResponseEntity.ok(courseDto);
    }
}
