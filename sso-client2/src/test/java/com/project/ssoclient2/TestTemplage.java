package com.project.ssoclient2;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest
public class TestTemplage {

    @Autowired
    private RestTemplate restTemplate;


    @Test
    public void getResData() {
        ResponseEntity<String> res = restTemplate.getForEntity("http://localhost:9002/res/api/v1/user/currentUser", String.class);
        String body = res.getBody();
        System.out.println(body);
    }

    @Test
    public void testAccessBytoken() {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "UserManagement");
        map.add("username","admin");
        map.add("password","123456");
        map.add("client_secret", "user123");
        map.add("redirect_uri", "http://localhost:9002/res/login");
        map.add("grant_type", "password");
        Map<String, String> resp = restTemplate.postForObject("http://localhost:9001/oauth/token", map, Map.class);
        String access_token = resp.get("access_token");
        System.out.println(access_token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<SysUser> entity = restTemplate.exchange("http://localhost:9002/res/api/v1/user/currentUser", HttpMethod.GET, httpEntity, SysUser.class);

        System.out.println(entity);
    }


    @org.junit.jupiter.api.Test
    public void getAuthToken() {
        ResponseEntity<String> res = restTemplate.getForEntity("http://localhost:9001/oauth/authorize", String.class);
        String body = res.getBody();
        System.out.println(body);
    }

    @org.junit.jupiter.api.Test
    public void getPassword() {

        String password = "123456";
        PasswordEncoder pe = NoOpPasswordEncoder.getInstance();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoderPwd = pe.encode(password);
        System.out.println(encoderPwd);
    }


    @org.junit.jupiter.api.Test
    public void testToken() {
        String access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjY0MjE1NzQsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiNmI5MTIxMjNmNzJhNDY1NzgyYzkxZjBmNGQzMzBkZWMiLCJTdXBlckFkbWluOTk5OTkiXSwianRpIjoiZDBjZWY3Y2QtODBhZi00ODVmLWJjNjktNzg3YTMzNTkxYjg0IiwiY2xpZW50X2lkIjoidGVzdDEyMzEyMyIsInNjb3BlIjpbImFsbCJdfQ.G-M_uKtX94goBWygFuWj9QlaH-XnSdvh6S6-OhxC_uQ";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("http://localhost:7002/sec/vvvv", HttpMethod.GET, httpEntity, String.class);
        System.out.println(entity.getBody());
    }


}
