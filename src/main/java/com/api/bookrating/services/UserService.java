package com.api.bookrating.services;

import com.api.bookrating.dto.PasswordDto;
import com.api.bookrating.dto.UserDto;
import com.api.bookrating.exception.BadRequestException;
import com.api.bookrating.enums.Role;
import com.api.bookrating.repositories.UserRepository;
import com.api.bookrating.model.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Transactional
    public User save(UserDto userDto){
        if(userRepository.existsByUsername(userDto.getUsername())){
            throw new BadRequestException(userDto.getUsername() + " already exists!");
        }
        User user = new User();
        user.setRole(Role.USER);
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
    @Transactional
    public String updatePassword(PasswordDto passwordDto){
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        userRepository.save(user);
        return user.getPassword();
    }
    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
