package com.multicampus.gamesungcoding.a11ymarketserver.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.validation.Validator;

@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    private UserRepository userRepository;
    private Validator validator;

    @Test
    @DisplayName("사용자 저장 및 조회 테스트")
    void saveAndSearchUserTest() {
        Users user = Users.builder()
                .userEmail("user1@example.com")
                .userPass("password123")
                .userName("User One")
                .userNickname("user-one")
                .userPhone("01023456789")
                .build();

        Users savedUser = userRepository.save(user);
        Users foundUser = userRepository.findById(savedUser.getUserId()).orElse(null);

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUserEmail()).isEqualTo("user1@example.com");
        Assertions.assertThat(foundUser.getUserId()).isEqualTo(savedUser.getUserId());
    }
}
