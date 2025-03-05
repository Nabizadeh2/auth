package com.atl23.authapp.controller;

import com.atl23.authapp.dto.LoginDto;
import com.atl23.authapp.dto.TeacherDto;
import com.atl23.authapp.dto.TeacherRequestDto;
import com.atl23.authapp.service.TeacherService;
import com.atl23.authapp.util.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private  final TeacherService teacherService;
    private  final JwtHelper jwtHelper;
    @PostMapping("/register")
    public ResponseEntity<Void>register(@RequestBody TeacherDto teacherDto){
         teacherService.register(teacherDto);
         return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public LoginDto login(@RequestBody TeacherRequestDto teacherRequestDto){
        return teacherService.login(teacherRequestDto);
    }

    @GetMapping("/decoder")
    public String getDecoder(@RequestParam ("token") String token){
        return jwtHelper.tokenByDeCoder(token);
    }
}
