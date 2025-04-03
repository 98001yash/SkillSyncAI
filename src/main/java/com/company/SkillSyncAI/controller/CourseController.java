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

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;
    //private final RecommendationService recommendationService;


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

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses(){
        log.info("Fetching all courses....");
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto){
        log.info("Updating a course with ID: {}",id);
        return ResponseEntity.ok(courseService.updateCourse(id, courseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id){
        log.info("Deleting course with ID: {}",id);
        courseService.deleteCourse(id);
        return ResponseEntity.ok("course deleted successfully");
    }



//    @GetMapping("/recommendations/{userId}")
//    public ResponseEntity<List<CourseDto>> getRecommendations(@PathVariable Long userId){
//        log.info("Request for course recommendation for user {}",userId);
//        return ResponseEntity.ok(recommendationService.recommendCourse(userId));
//    }



}
