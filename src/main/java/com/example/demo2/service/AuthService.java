package com.example.demo2.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.EmailVerifyRequest;
import com.example.demo2.dto.request.EmailRequest;
import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.request.ResetPasswordRequest;
import com.example.demo2.dto.response.LoginResponse;
import com.example.demo2.entity.EmailVerifyCode;
import com.example.demo2.entity.PasswordResetToken;
import com.example.demo2.entity.User;
import com.example.demo2.enums.ErrorCode;
import com.example.demo2.enums.UserRole;
import com.example.demo2.exception.AccountStatusException;
import com.example.demo2.exception.NotFoundException;
import com.example.demo2.exception.UnauthorizedException;
import com.example.demo2.repository.CodeDao;
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
    private final CodeDao codeDao;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserName(request.userName())
                .filter(u -> passwordEncoder.matches(
                        request.password(), u.getPasswordHash()))
                .orElseThrow(UnauthorizedException::new);

        switch (user.getStatus()) {
            case PENDING:
                throw new AccountStatusException(ErrorCode.ACCOUNT_PENDING, "帳號尚未啟用");
            case INACTIVE:
                throw new AccountStatusException(ErrorCode.ACCOUNT_INACTIVE, "帳號已被停用");
            case ACTIVE:
                String accessToken = jwtTokenProvider.createToken(
                        user.getUserName(), user.getRole());
                LoginResponse login = new LoginResponse(accessToken);
                return login;
            default:
                throw new RuntimeException("未知帳號狀態");
        }

    }

    @Transactional(readOnly = true)
    public LoginResponse loginAdmin(LoginRequest request) {
        User user = userRepository.findByUserName(request.userName())
                .filter(u -> passwordEncoder.matches(
                        request.password(), u.getPasswordHash()))
                .orElseThrow(UnauthorizedException::new);

        if (!UserRole.ADMIN.equals(user.getRole())) {
            throw new RuntimeException("非管理員帳號");
        }

        String accessToken = jwtTokenProvider.createToken(
                user.getUserName(), user.getRole());

        return new LoginResponse(accessToken);
    }

    @Transactional
    public void forgotPassword(EmailRequest request) {

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
    public void resetPassword(ResetPasswordRequest request) {

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

    @Transactional
    public void generateEmailVerifyCode(String email) {
        Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent()) {
            throw new RuntimeException("此信箱已註冊");
        }
        SecureRandom random = new SecureRandom();
        String code = String.format("%06d", random.nextInt(1000000));

        EmailVerifyCode evc = new EmailVerifyCode();
        evc.setEmail(email);
        evc.setCode(code);
        evc.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        evc.setUsed(false);
        codeDao.save(evc);
        emailService.sendSimpleEmail(email, "帳號註冊認證碼", "六位認證碼: " + code);
    }

    @Transactional
    public void verifyEmailCode(EmailVerifyRequest r) {
        List<EmailVerifyCode> evcs = codeDao.findByEmailAndCodeAndUsed(r.email(), r.code(), false);
        for (EmailVerifyCode evc : evcs) {
            if (evc.getExpiryDate().isAfter(LocalDateTime.now())) {
                evc.setUsed(true);
                return;
            }
        }
        throw new RuntimeException("認證碼錯誤");
    }

    @Transactional
    public Boolean checkingUserName(String name) {
        Optional<User> userName = userDao.findByUserName(name);
        if (userName.isEmpty()) {
            return false;
        }
        return true;
    }

    // TODO: 【Phase 2】超級管理員密碼驗證和信箱驗證相關方法
    // 1. verifyOldPassword(User user, String rawPassword): boolean
    // - 驗證超級管理員舊密碼，進入編輯頁面前使用
    // - 使用 BCryptPasswordEncoder 對比密碼
    //
    // 2. generatePasswordChangeVerifyCode(String email): void
    // - 為密碼變更生成驗證碼，發送到現有信箱
    // - 驗證碼有效期 15 分鐘
    // - 利用現有的 EmailVerifyCode entity 和 emailService
    //
    // 3. verifyPasswordChangeCode(String email, String code): boolean
    // - 驗證密碼變更的驗證碼
    // - 返回 boolean，標記驗證碼為已使用
}
