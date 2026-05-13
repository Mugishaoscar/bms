package com.bar.bms.controller;

import com.bar.bms.model.User;
import com.bar.bms.repository.UserRepository;
import com.bar.bms.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
public class AuthController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("boss", new User());
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot_password";
    }

//    @PostMapping("/forgot-password")
//    public String resetPassword(@RequestParam String username,
//                                @RequestParam String newPassword) {
//
//        User user = userRepository.findByUsername(username).orElse(null);
//
//        if (user != null) {
//            user.setPassword(passwordEncoder.encode(newPassword));
//            userRepository.save(user);
//        }
//
//        return "redirect:/login";
//    }
@PostMapping("/forgot-password")
public String sendResetCode(@RequestParam String email) {

    User user = userRepository.findByEmail(email).orElse(null);

    if (user == null) {
        return "redirect:/forgot-password?error=email_not_found";
    }

    try {
        String code = String.valueOf(100000 + new Random().nextInt(900000));

        user.setResetCode(code);
        userRepository.save(user);

        emailService.sendResetCode(email, code);

        return "redirect:/verify-code?sent=success";

    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/forgot-password?error=email_failed";
    }
}
    @GetMapping("/verify-code")
    public String verifyCodePage() {
        return "verify_code";
    }
    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String email,
                             @RequestParam String code,
                             @RequestParam String newPassword,
                             @RequestParam String confirmPassword) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/verify-code?error=user_not_found";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/verify-code?error=password_mismatch";
        }

        if (user.getResetCode() == null || !user.getResetCode().equals(code)) {
            return "redirect:/verify-code?error=invalid_code";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null);
        userRepository.save(user);

        return "redirect:/login?reset=success";
    }
}