package com.fil.rouge.web.rest;


import com.fil.rouge.domain.AppUser;
import com.fil.rouge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<AppUser> createUser(AppUser user) {
        URI uri = URI.create("/api/v1/users/" + user.getId());
        return ResponseEntity.created(uri).body(userService.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        userService.findById(id).ifPresent(user ->
            ResponseEntity.ok().body(user)
        );
        return ResponseEntity.notFound().build();
    }
}
