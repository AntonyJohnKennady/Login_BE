package com.example.signup.service;

import com.example.signup.dto.LoginRequestDto;
import com.example.signup.dto.SignupRequestDto;
import com.example.signup.dto.UserResponseDto;
import com.example.signup.model.ResponseModel;
import com.example.signup.model.User;
import com.example.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> signup(SignupRequestDto signupRequest) {

        if (userRepository.existsByEmailAndIsDeletedIsFalse(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel("Email Id "+signupRequest.getEmail()+" already exists.",HttpStatus.BAD_REQUEST.value()));
        }

        User user = new User();

        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());

        userRepository.save(user);

        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setId(user.getId());
        responseDto.setUsername(user.getUsername());
        responseDto.setEmail(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel("User Registered Successfully",HttpStatus.CREATED.value(),responseDto));
    }

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new RuntimeException("The given emailId " + loginRequestDto.getEmail() + " Not Found"));

        if(!user.getPassword().equals(loginRequestDto.getPassword())){
            return
                    ResponseEntity.badRequest().body(new ResponseModel("Invalid Password",HttpStatus.BAD_REQUEST.value()));
        }
        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setId(user.getId());
        responseDto.setUsername(user.getUsername());
        responseDto.setEmail(user.getEmail());

        return ResponseEntity.ok().body(new ResponseModel("Login Successfully",HttpStatus.OK.value(),responseDto));
    }
}
