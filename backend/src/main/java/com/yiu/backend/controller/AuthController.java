package com.yiu.backend.controller;

import com.yiu.backend.model.User;
import com.yiu.backend.repository.UserRepository;
import com.yiu.backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // adjust if needed
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ======================
    // SIGNUP
    // ======================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {

        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username and password are required");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully!");
    }

    // ======================
    // LOGIN
    // ======================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username and password are required");
        }

        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        User dbUser = optionalUser.get();

        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        String token = jwtUtils.generateToken(dbUser.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
