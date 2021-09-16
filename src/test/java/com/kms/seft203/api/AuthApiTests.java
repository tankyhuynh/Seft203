package com.kms.seft203.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.seft203.api.auth.LoginRequest;
import com.kms.seft203.config.security.jwt.JwtUtils;
import com.kms.seft203.config.security.services.UserDetailsServiceImpl;
import com.kms.seft203.domain.auth.UserEntity;
import com.kms.seft203.domain.auth.UserRepository;
import com.kms.seft203.api.auth.RegisterRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
	JwtUtils jwtUtils;

    @Autowired
	private UserDetailsServiceImpl userDetailsService;

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
   
    @Test
    void testRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("test");
        registerRequest.setFullName("Nguyen Van Test");

        this.mockMvc.perform(
                    post("/auth/register", registerRequest)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andReturn();
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(USERNAME);
        loginRequest.setPassword(PASSWORD);

        this.mockMvc.perform(
                    post("/auth/login", loginRequest)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    String generateToken(){
        final UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);
		final String token = jwtUtils.generateToken(userDetails);

        return token;
    }

    private String toJSONString(Object obj) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(obj);
    }
}
