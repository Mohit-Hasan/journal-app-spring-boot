package com.mohithasan.journalapp.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "Username is required")
    @Size(max = 64)
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(max = 128)
    private String password;

    @Size(max = 128)
    private String email;
}
