package com.example.health.service;

import com.example.health.domain.User;
import com.example.health.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }


    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }


    public User findById(Long id) {
        return repo.findById(id).orElse(null);
    }


    public List<User> findAll() {
        return repo.findAll();
    }


    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }


    public boolean matches(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }


    public User update(Long id, User newData) {
        User user = repo.findById(id).orElse(null);
        if (user == null) return null;

        user.setUsername(newData.getUsername());
        user.setAge(newData.getAge());

        if (newData.getPassword() != null && !newData.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newData.getPassword()));
        }

        return repo.save(user);
    }


    public void delete(Long id) {
        repo.deleteById(id);
    }
}
