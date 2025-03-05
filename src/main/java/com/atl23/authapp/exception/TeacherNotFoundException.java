package com.atl23.authapp.exception;

public class TeacherNotFoundException extends RuntimeException{

    public TeacherNotFoundException(String message){
        super(message);
    }
}
