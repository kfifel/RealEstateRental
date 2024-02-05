package com.fil.rouge.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    Long id;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotBlank
    String email;
    @NotBlank
    @Length(min = 8, max = 20)
    String password;

    LocalDateTime createdAt;
    LocalDateTime verifiedAt;
    List<RoleDto> roles;
}