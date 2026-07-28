package com.example.signup.service;

import com.example.signup.dto.UpdateUserDto;
import com.example.signup.dto.UserResponseDto;
import com.example.signup.model.ResponseModel;
import com.example.signup.model.User;
import com.example.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getAllUsers() {
        List<UserResponseDto> users = userRepository.findAll().stream().map(this::converter).toList();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel("Users Fetched Successfully", HttpStatus.OK.value(), users));
    }

    private UserResponseDto converter(User user) {

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setUsername(user.getUsername());

        return userResponseDto;
    }

    public ResponseEntity<?> getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel("User Fetched Successfully", HttpStatus.OK.value(), converter(user)));
    }

    public ResponseEntity<?> updateUser(Long id, UpdateUserDto updateUserDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!user.getEmail().equals(updateUserDto.getEmail()) && userRepository.existsByEmailAndIsDeletedIsFalse(updateUserDto.getEmail())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseModel("Email already Exists.", HttpStatus.CONFLICT.value()));
        }

        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel("User updated Successfully", HttpStatus.OK.value(), converter(user)));
    }

    public ResponseEntity<?> deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setIsDeleted(true);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel("User Deleted Successfully", HttpStatus.OK.value()));
    }
}
