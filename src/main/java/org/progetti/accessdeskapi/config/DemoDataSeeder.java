package org.progetti.accessdeskapi.config;

import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.models.request.Request;
import org.progetti.accessdeskapi.models.request.StatusRequest;
import org.progetti.accessdeskapi.models.request.Urgency;
import org.progetti.accessdeskapi.models.user.Role;
import org.progetti.accessdeskapi.models.user.User;
import org.progetti.accessdeskapi.repository.RequestRepository;
import org.progetti.accessdeskapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/** Crea utenti e richieste di esempio, solo con il profilo "demo". */
@Component
@Profile("demo")
@RequiredArgsConstructor
public class DemoDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${demo.password}")
    private String demoPassword;

    @Override
    public void run(String... args) {
        User employee = userRepository.save(newUser("demo.employee", Role.EMPLOYEE));
        userRepository.save(newUser("demo.manager", Role.MANAGER));

        requestRepository.save(newRequest(employee, "IntelliJ IDEA Ultimate",
                "Serve per sviluppare le API Spring Boot del progetto.",
                Urgency.STANDARD, StatusRequest.PENDING, 3, null));
        requestRepository.save(newRequest(employee, "Postman",
                "Test manuale degli endpoint durante il collaudo.",
                Urgency.URGENT, StatusRequest.PENDING, 1, null));
        requestRepository.save(newRequest(employee, "DBeaver",
                "Consultare le tabelle PostgreSQL in sola lettura.",
                Urgency.STANDARD, StatusRequest.APPROVED, 7, 5));
    }

    private User newUser(String username, Role role) {
        return User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(demoPassword))
                .role(role)
                .build();
    }

    private Request newRequest(User user, String software, String reason, Urgency urgency,
                               StatusRequest status, int createdDaysAgo, Integer decidedDaysAgo) {
        Instant now = Instant.now();
        return Request.builder()
                .softwareName(software)
                .reason(reason)
                .urgency(urgency)
                .status(status)
                .requestedBy(user)
                .createdAt(now.minus(createdDaysAgo, ChronoUnit.DAYS))
                .decidedAt(decidedDaysAgo == null ? null : now.minus(decidedDaysAgo, ChronoUnit.DAYS))
                .build();
    }
}
