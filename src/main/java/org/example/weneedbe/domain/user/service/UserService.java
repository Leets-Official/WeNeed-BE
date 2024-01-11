package org.example.weneedbe.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
