package com.fluffyknightz.ebook_library.modules.user.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.user.dto.UserDTO;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import com.fluffyknightz.ebook_library.modules.user.repository.UserRepository;
import com.fluffyknightz.ebook_library.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(UserDTO userDTO) {
        User user = new User(userDTO.username(), userDTO.email(), passwordEncoder.encode(userDTO.password()), userDTO.role(), false);
        try {
            return userRepository.save(user);
        } catch (DuplicateKeyException d) {
            throw new DuplicateKeyException("User with username: " + userDTO.username() + " already exists");
        }

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public User update(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.id())
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userDTO.id()));
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        user.setRole(userDTO.role());
        return userRepository.save(user);
    }

}
