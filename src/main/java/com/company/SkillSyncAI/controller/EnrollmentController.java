package com.company.SkillSyncAI.controller;


import com.company.SkillSyncAI.dtos.CourseDto;
import com.company.SkillSyncAI.dtos.EnrollmentDto;
import com.company.SkillSyncAI.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentDto> enrollUser(@RequestBody EnrollmentDto enrollmentDto){
        return ResponseEntity.ok(enrollmentService.enrollUser(enrollmentDto));
    }


    @GetMapping("/enrolled/{userId}")
    public ResponseEntity<List<CourseDto>> getEnrolledCourses(@PathVariable Long userId) {
        log.info("Fetching enrolled courses for user ID: {}", userId);
        return ResponseEntity.ok(enrollmentService.getEnrolledCourses(userId));
    }

    @GetMapping("/progress/{userId}/{courseId}")
    public ResponseEntity<Integer> getCourseProgress(@PathVariable Long userId, @PathVariable Long courseId) {
        int progress = enrollmentService.getCourseProgress(userId, courseId);
        return ResponseEntity.ok(progress);
    }

    @PutMapping("/progress/update")
    public ResponseEntity<EnrollmentDto> updateCourseProgress(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestParam int progress) throws BadRequestException {

        EnrollmentDto updatedEnrollment = enrollmentService.updateCourseProgress(userId, courseId, progress);
        return ResponseEntity.ok(updatedEnrollment);
    }



}
