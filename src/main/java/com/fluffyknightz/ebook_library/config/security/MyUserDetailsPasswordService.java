package com.fluffyknightz.ebook_library.config.security;

import com.fluffyknightz.ebook_library.modules.user.entity.User;
import com.fluffyknightz.ebook_library.modules.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsPasswordService implements UserDetailsPasswordService {
    private final UserRepository userRepository;

    @Override
    public MyUserDetails updatePassword(UserDetails userDetails, String newPassword) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                                  .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setPassword(newPassword);    // update password in entity
        userRepository.save(user);        // persist change to DB
        return new MyUserDetails(user);
    }
}
