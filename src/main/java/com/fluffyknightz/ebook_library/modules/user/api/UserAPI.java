package com.fluffyknightz.ebook_library.modules.user.api;

import com.fluffyknightz.ebook_library.modules.user.dto.UserDTO;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import com.fluffyknightz.ebook_library.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<User>> getForTable(@RequestParam(required = false) String search, Pageable pageable) {
        Page<User> users = userService.findForTable(search, pageable);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
