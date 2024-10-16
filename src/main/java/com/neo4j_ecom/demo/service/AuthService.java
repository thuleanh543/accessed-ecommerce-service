package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ChangePasswordRequest;
import com.neo4j_ecom.demo.model.dto.request.RegisterRequest;
import com.neo4j_ecom.demo.model.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {


    User register(RegisterRequest registerRequest);

    Map<String, Object> login(Map<String, String> request);

    Void handleForgotPassword(String email);

    void verifyAccount(String email);

    void handleResetPassword(ChangePasswordRequest request, String token);

}
