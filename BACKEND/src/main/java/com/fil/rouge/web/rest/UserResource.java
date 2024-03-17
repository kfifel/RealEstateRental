package com.fil.rouge.web.rest;


import com.fil.rouge.domain.AppUser;
import com.fil.rouge.service.UserService;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.dto.response.UserResponseDto;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import com.fil.rouge.web.mapper.UserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @ParameterObject Pageable pageable,
            @RequestParam(name = "query", required = false) String query
            ) {
        Page<AppUser> page = userService.findAll(pageable, query);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(
                page
                    .stream()
                    .map(UserDtoMapper::toDto)
                    .toList()
        );
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

    @PatchMapping("/{id}/{enable}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> enableUser(@PathVariable Long id, @PathVariable boolean enable) {
        userService.enable(id, enable);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<Object> forceDelete(@PathVariable Long id) {
        userService.forceDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateRoles(@PathVariable Long id, @RequestBody List<String> roles) throws ValidationException, ResourceNotFoundException {
        userService.handleRoles(id, roles);
        return ResponseEntity.noContent().build();
    }
}
