package com.tatamotors.classbooking.service;

import com.tatamotors.classbooking.dto.LoginRequest;
import com.tatamotors.classbooking.dto.LoginResponse;
import com.tatamotors.classbooking.dto.SignupRequest;
import com.tatamotors.classbooking.entity.User;

public interface UserService {

    User signup(SignupRequest request);

    LoginResponse login(LoginRequest request);
}
