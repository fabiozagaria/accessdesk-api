package org.progetti.accessdeskapi.dtos.register;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRequestUser(
        @NotBlank
        @Size(min = 10, max = 30)
        String username,

        @NotBlank
        @Size(min = 8)
        String password
) {

}
