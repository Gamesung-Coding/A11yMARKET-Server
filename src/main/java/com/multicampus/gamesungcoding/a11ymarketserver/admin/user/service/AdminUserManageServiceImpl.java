package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.User;
import com.multicampus.gamesungcoding.a11ymarketserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserManageServiceImpl implements AdminUserManageService {
    private final UserRepository userRepository;

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }
}
