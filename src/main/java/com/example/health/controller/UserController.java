package com.example.health.controller;

import com.example.health.domain.User;
import com.example.health.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.health.security.JwtUtil;
import com.example.health.dto.LoginRequest;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    @PostMapping("/create")
    public User create(@RequestBody User user) {
        return service.save(user);
    }


    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return service.findById(id);
    }


    @GetMapping("/all")
    public List<User> getAll() {
        return service.findAll();
    }


    @GetMapping("/me")
    public User me(@RequestHeader("Authorization") String token) {

        String realToken = token.replace("Bearer ", "");
        String username = JwtUtil.getUsername(realToken);

        return service.findByUsername(username);
    }


    @PutMapping("/update/{id}")
    public User update(@PathVariable Long id, @RequestBody User userData) {
        return service.update(id, userData);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = service.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(401).body("존재하지 않는 사용자");
        }

        if (!service.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("비밀번호 틀림");
        }

        return ResponseEntity.ok(JwtUtil.createToken(user.getUsername()));
    }

}
