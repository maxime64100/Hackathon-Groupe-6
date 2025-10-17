package com.api.hackathon.service;

import com.api.hackathon.model.UserBabyfoot;
import com.api.hackathon.repository.UserBabyfootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserBabyfootRepository userRepo;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private UserBabyfoot user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserBabyfoot(1, "Alice", "Dupont", "alice@example.com", "hashedpass", "USER", "ATTAQUANT", null);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        when(userRepo.findByMail("alice@example.com")).thenReturn(Optional.of(user));

        UserDetails details = userDetailsService.loadUserByUsername("alice@example.com");

        assertThat(details.getUsername()).isEqualTo("alice@example.com");
        assertThat(details.getPassword()).isEqualTo("hashedpass");
        verify(userRepo).findByMail("alice@example.com");
    }

    @Test
    void loadUserByUsername_shouldThrowWhenUserNotFound() {
        when(userRepo.findByMail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("unknown@example.com"));

        verify(userRepo).findByMail("unknown@example.com");
    }
}
