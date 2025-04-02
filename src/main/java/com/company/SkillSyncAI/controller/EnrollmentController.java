package com.company.SkillSyncAI.controller;


import com.company.SkillSyncAI.dtos.EnrollmentDto;
import com.company.SkillSyncAI.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentDto> enrollUser(@RequestBody EnrollmentDto enrollmentDto){
        return ResponseEntity.ok(enrollmentService.enrollUser(enrollmentDto));
    }
}
