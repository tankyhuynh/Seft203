package com.kms.seft203.api.auth;

import static com.kms.seft203.utils.MailConstraint.EMAIL_CONTENT;
import static com.kms.seft203.utils.MailConstraint.EMAIL_TITLE;
import static com.kms.seft203.utils.UrlConstraint.AUTH_LOGIN_URL;
import static com.kms.seft203.utils.UrlConstraint.AUTH_LOGOUT_URL;
import static com.kms.seft203.utils.UrlConstraint.AUTH_REGISTER_URL;
import static com.kms.seft203.utils.UrlConstraint.AUTH_REGISTER_VERIFY_URL;
import static com.kms.seft203.utils.UrlConstraint.AUTH_URL;
import static com.kms.seft203.utils.UrlConstraint.AUTH_VERIFY_URL;

import java.util.Optional;

import javax.annotation.PostConstruct;

import com.kms.seft203.config.security.jwt.JwtUtils;
import com.kms.seft203.config.security.services.UserDetailsServiceImpl;
import com.kms.seft203.domain.auth.UserEntity;
import com.kms.seft203.domain.auth.UserRepository;
import com.kms.seft203.utils.MailUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(AUTH_URL)
@RequiredArgsConstructor
public class AuthApi {

    private final UserRepository userRepository;
    private final MailUtils mailUtils;

    @Autowired
	JwtUtils jwtUtils;

    @Autowired
	AuthenticationManager authenticationManager;

    @Autowired
	PasswordEncoder encoder;

    @Autowired
	private UserDetailsServiceImpl userDetailsService;

    @PostMapping(AUTH_REGISTER_URL)
    public ResponseEntity<UserEntity> register(@RequestBody RegisterRequest request){
        Optional<UserEntity> searchUSer = userRepository.findByEmail(request.getEmail());
        if(searchUSer.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String email = request.getEmail(); 
        String username = email.split("@")[0]; 
        String password = encoder.encode(request.getPassword()); 
        String fullName = request.getFullName(); 
        String link = AUTH_VERIFY_URL + username;
        
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);

        userRepository.save(user);

        mailUtils.sendMail(email, EMAIL_TITLE, EMAIL_CONTENT + link);

        // TODO: remove user's password
        return ResponseEntity.ok(user);
    }

    @GetMapping(AUTH_REGISTER_VERIFY_URL)
    public ResponseEntity<UserEntity> confirmRegister(@PathVariable String username) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);

        System.out.println(userOptional);
        System.out.println(username);
 
        if(userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setIsEnabled(true);
            return ResponseEntity.ok(userRepository.save(user));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping(AUTH_LOGIN_URL)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {
        String username = request.getUsername();
        String password = request.getPassword();

        authenticate(username, password);

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(username);

		final String token = jwtUtils.generateToken(userDetails);

        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isPresent()) {
                String refreshToken = "<refresh_token>";
                LoginResponse loginResponse = new LoginResponse(token, refreshToken);

                return ResponseEntity.ok(loginResponse);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

    @PostMapping(AUTH_LOGOUT_URL)
	public String logout() {
        jwtUtils.expireToken();
		return "OK";
	}

}
