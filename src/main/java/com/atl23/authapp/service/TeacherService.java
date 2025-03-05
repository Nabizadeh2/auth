package com.atl23.authapp.service;
import com.atl23.authapp.dto.LoginDto;
import com.atl23.authapp.dto.TeacherDto;
import com.atl23.authapp.dto.TeacherRequestDto;

public interface TeacherService {

     void register(TeacherDto teacherDto);
     LoginDto login(TeacherRequestDto teacherRequestDto);
}
