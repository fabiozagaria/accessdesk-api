package org.progetti.accessdeskapi.models.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.progetti.accessdeskapi.models.request.Request;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 10, max = 30)
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String passwordHash;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Transient
    private Set<Authorities> authorities;

    @Transient
    @OneToMany(mappedBy = "requestedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> requestList;
}
