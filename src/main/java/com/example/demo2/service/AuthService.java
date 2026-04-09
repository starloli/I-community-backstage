package com.example.demo2.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo2.dto.request.ForgotPasswordRequest;
import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.request.ResetPasswordRequest;
import com.example.demo2.dto.response.LoginResponse;
import com.example.demo2.entity.PasswordResetToken;
import com.example.demo2.entity.User;
import com.example.demo2.enums.UserRole;
import com.example.demo2.exception.NotFoundException;
import com.example.demo2.exception.UnauthorizedException;
import com.example.demo2.repository.TokenDao;
import com.example.demo2.repository.UserDao;
import com.example.demo2.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
	private final UserDao userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenDao tokenDao;
	private final EmailService emailService;
	private final UserDao userDao;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserName(request.userName())
                .filter(u -> passwordEncoder.matches(
                        request.password(), u.getPasswordHash()
                ))
                .orElseThrow(UnauthorizedException::new);

        String accessToken = jwtTokenProvider.createToken(
                user.getUserName(), user.getRole()
        );

        return new LoginResponse(accessToken);
    }

    @Transactional(readOnly = true)
    public LoginResponse loginAdmin(LoginRequest request) {
        User user = userRepository.findByUserName(request.userName())
                .filter(u -> passwordEncoder.matches(
                        request.password(), u.getPasswordHash()
                ))
                .orElseThrow(UnauthorizedException::new);

        if (!UserRole.ADMIN.equals(user.getRole())) {
                throw new RuntimeException("非管理員帳號");
        }

        String accessToken = jwtTokenProvider.createToken(
                user.getUserName(), user.getRole()
        );

        return new LoginResponse(accessToken);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

		Optional<User> userOpt = userDao.findByEmail(request.getEmail());

		if (userOpt.isPresent()) {
			String token = createResetToken(userOpt.get());

			String link = "http://localhost:4200/reset-password?token=" + token;
			emailService.sendResetEmail(request.getEmail(), link);
		} else {
            throw new NotFoundException("未註冊的信箱");
        }
	}

    @Transactional
	public void resetPassword(@RequestBody ResetPasswordRequest request) {

		PasswordResetToken token = tokenDao.findByToken(request.getToken())
				.orElseThrow(() -> new RuntimeException("無效的Token"));
        
		if (token.isUsed() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
				throw new RuntimeException("Token過期或已被使用");
		}

		User user = token.getUser();
		user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));

		token.setUsed(true);
	}

    @Transactional
    public String createResetToken(User user) {
        String token = UUID.randomUUID().toString();

        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setUser(user);
        prt.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        prt.setUsed(false);

        tokenDao.save(prt);
        return token;
    } 
}
