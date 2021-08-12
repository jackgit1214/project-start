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
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class TestResServer {

    @Autowired
    private RestTemplate restTemplate;


    @Test
    public void getResData() {
        ResponseEntity<String> res = restTemplate.getForEntity("http://localhost:9002/res/res/hello", String.class);
        String body = res.getBody();
        System.out.println(body);
    }

    @Test
    public void getAuthToken() {
        ResponseEntity<String> res = restTemplate.getForEntity("http://localhost:9001/oauth/authorize", String.class);
        String body = res.getBody();
        System.out.println(body);
    }

    @Test
    public void getPassword() {

        String password="123456";
        PasswordEncoder pe = NoOpPasswordEncoder.getInstance();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoderPwd = pe.encode(password);
        System.out.println(encoderPwd);
    }


    @Test
    public void testToken(){
        String access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjY0MjE1NzQsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiNmI5MTIxMjNmNzJhNDY1NzgyYzkxZjBmNGQzMzBkZWMiLCJTdXBlckFkbWluOTk5OTkiXSwianRpIjoiZDBjZWY3Y2QtODBhZi00ODVmLWJjNjktNzg3YTMzNTkxYjg0IiwiY2xpZW50X2lkIjoidGVzdDEyMzEyMyIsInNjb3BlIjpbImFsbCJdfQ.G-M_uKtX94goBWygFuWj9QlaH-XnSdvh6S6-OhxC_uQ";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("http://localhost:7002/sec/vvvv", HttpMethod.GET, httpEntity, String.class);
        System.out.println(entity.getBody());
    }


}
