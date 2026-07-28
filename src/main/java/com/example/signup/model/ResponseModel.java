package com.example.signup.model;

import lombok.Data;

@Data
public class ResponseModel {

    private String message;
    private int status;
    private Object data;

    public ResponseModel(String message, int status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public ResponseModel(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
