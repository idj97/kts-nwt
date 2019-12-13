package com.mbooking.utils;

import com.mbooking.dto.LoginRequestDTO;
import com.mbooking.dto.UserDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class SecurityHelper {

    public static HttpHeaders loginAndCreateHeaders(String email, String password, TestRestTemplate rest) {
        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);
        ResponseEntity<UserDTO> response = rest.postForEntity("/api/auth/login", loginRequest, UserDTO.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getBody().getToken());
        return headers;
    }

}
