package org.progetti.accessdeskapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "requests")

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 80)
    @Column(nullable = false, length = 80)
    private String softwareName;


    private String reason;

    @Enumerated(value = EnumType.STRING)
    private Urgency urgency;

    @Enumerated(value = EnumType.STRING)
    private StatusRequest status;

    private User requestedBy;

    private Instant createdAt;

    private String decidedAt;

}
