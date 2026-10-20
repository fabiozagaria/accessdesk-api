package org.progetti.accessdeskapi.models.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.progetti.accessdeskapi.models.user.User;

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

    @NotBlank
    @Size(min = 10, max = 500)
    @Column(length = 500, nullable = false)
    private String reason;

    @NotNull
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Urgency urgency;

    @NotNull
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusRequest status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User requestedBy;

    @NotNull
    @Column(nullable = false)
    private Instant createdAt;

    private String decidedAt;

    @Version
    @Column(nullable = false)
    private Integer version;

}
