package com.atl23.authapp.service.impl;

import com.atl23.authapp.dao.TeacherEntity;
import com.atl23.authapp.dao.TeacherRepository;
import com.atl23.authapp.dto.LoginDto;
import com.atl23.authapp.dto.TeacherDto;
import com.atl23.authapp.dto.TeacherRequestDto;
import com.atl23.authapp.exception.TeacherNotFoundException;
import com.atl23.authapp.service.TeacherService;
import com.atl23.authapp.util.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements UserDetailsService , TeacherService {

    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtHelper jwtHelper;
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var teacherEntity=teacherRepository.findByUsername(username)
                .orElseThrow(()-> new TeacherNotFoundException("BU istifadeci artiq movcududr"+username));
        return new User(username,teacherEntity.getPassword(), List.of());
    }

    @Override
    public void register(TeacherDto teacherDto) {
        if (teacherRepository.findByUsername(teacherDto.getUsername()).isPresent()){
            throw new RuntimeException();
        }

        var password= bCryptPasswordEncoder.encode(teacherDto.getPassword());
        teacherRepository.save(
                TeacherEntity.builder()
                        .name(teacherDto.getName())
                        .surname(teacherDto.getSurname())
                        .password(password)
                        .username(teacherDto.getUsername())
                        .build()
        );
    }
    @Override
    public LoginDto login(TeacherRequestDto teacherRequestDto) {
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(teacherRequestDto.getUsername(),
                        teacherRequestDto.getPassword()));
        var token =jwtHelper.generateToken(teacherRequestDto.getUsername());
        return new LoginDto(token,null) ;
    }
}
