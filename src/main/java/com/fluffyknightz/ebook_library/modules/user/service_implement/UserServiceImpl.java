package com.fluffyknightz.ebook_library.modules.user.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.user.dto.UserDTO;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import com.fluffyknightz.ebook_library.modules.user.repository.UserRepository;
import com.fluffyknightz.ebook_library.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(UserDTO userDTO) {
        User user = new User(userDTO.username(), userDTO.email(), passwordEncoder.encode(userDTO.password()),
                             userDTO.role(), false);
        try {
            userRepository.insert(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateKeyException("User with username: " + userDTO.username() + " already exists");
        }

    }

    @Override
    public Page<User> findForTable(String search, Pageable pageable) {
        return userRepository.findForTable(search, pageable);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        User user = findById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void update(UserDTO userDTO) {
        User user = findById(userDTO.id());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        user.setRole(userDTO.role());
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateKeyException("User with username: " + userDTO.username() + " already exists");
        }
    }

}
