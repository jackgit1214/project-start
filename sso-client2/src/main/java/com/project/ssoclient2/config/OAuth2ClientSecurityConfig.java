package com.project.ssoclient2.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;

import java.util.*;

@EnableWebSecurity
@Slf4j
public class OAuth2ClientSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/aaa")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(oauth2UserService());
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(authorization -> log.info(authorization.toString()))
//                .redirectionEndpoint(redirection ->log.info(redirection.toString()))
//                .tokenEndpoint(token ->log.info(token.toString()))
//                .userInfoEndpoint(userInfo ->log.info(userInfo.toString())));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {

        return (userRequest) -> {
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
            if (!StringUtils.hasText(userNameAttributeName)) {
                userNameAttributeName = "sub";
            }
            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            try {
                SignedJWT jwt = SignedJWT.parse(accessToken.getTokenValue());
                String claimJsonString = jwt.getJWTClaimsSet().toString();

                Collection<String> roles = new HashSet<>();
                JsonNode treeNode = objectMapper.readTree(claimJsonString);
                List<JsonNode> jsonNodes = treeNode.findValues("roles");
                jsonNodes.forEach(jsonNode -> {
                    if (jsonNode.isArray()) {
                        jsonNode.elements().forEachRemaining(e -> {
                            roles.add(e.asText());
                        });
                    } else {
                        roles.add(jsonNode.asText());
                    }
                });

                jsonNodes = treeNode.findValues("authorities");
                jsonNodes.forEach(jsonNode -> {
                    if (jsonNode.isArray()) {
                        jsonNode.elements().forEachRemaining(e -> {
                            roles.add(e.asText());
                        });
                    } else {
                        roles.add(jsonNode.asText());
                    }
                });

                for (String authority : roles) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                }
                Map<String, Object> userAttributes = new HashMap<>(16);
                userAttributes.put(userNameAttributeName, treeNode.findValue(userNameAttributeName));
//                userAttributes.put("preferred_username", treeNode.findValue("preferred_username"));
//                userAttributes.put("email", treeNode.findValue("email"));
                OAuth2User oAuth2User = new DefaultOAuth2User(grantedAuthorities, userAttributes, userNameAttributeName);

                return oAuth2User;
            } catch (Exception e) {
                log.error("oauth2UserService Exception", e);
            }
            return null;
        };
    }
}
