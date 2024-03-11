package com.fil.rouge.web.rest;


import com.fil.rouge.domain.AppUser;
import com.fil.rouge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUser> createUser(@RequestBody @Valid AppUser user) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> softDelete(@PathVariable Long id) {
        userService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<Object> forceDelete(@PathVariable Long id) {
        userService.forceDelete(id);
        return ResponseEntity.noContent().build();
    }
}
