package org.progetti.accessdeskapi.models;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    private Long id;
    private String name;
    private Role role;
}
