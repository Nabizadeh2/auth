package com.atl23.authapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    //register
    private String name;
    private String surname;
    private String username;
    private String password;
}
