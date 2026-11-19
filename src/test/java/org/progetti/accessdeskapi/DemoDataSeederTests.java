package org.progetti.accessdeskapi;

import org.junit.jupiter.api.Test;
import org.progetti.accessdeskapi.config.DemoDataSeeder;
import org.progetti.accessdeskapi.repository.RequestRepository;
import org.progetti.accessdeskapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Su Neon (profilo "demo,neon") il database sopravvive ai riavvii: un secondo avvio del seeder
 * non deve duplicare utenti e richieste, ma deve applicare l'eventuale nuova DEMO_PASSWORD.
 * Qui gira sul profilo demo (H2 in memoria) chiamando il seeder una seconda volta.
 */
// Datasource H2 fissato qui: le proprietà inline battono SPRING_DATASOURCE_URL dell'ambiente.
@SpringBootTest(properties = {
        "security.jwt.secret=dGVzdC1vbmx5LXNlY3JldC1rZXktYXQtbGVhc3QtMzItYnl0ZXMtbG9uZw==",
        "spring.datasource.url=jdbc:h2:mem:seeder-test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@ActiveProfiles("demo")
class DemoDataSeederTests {

    @Autowired DemoDataSeeder seeder;
    @Autowired UserRepository users;
    @Autowired RequestRepository requests;
    @Autowired PasswordEncoder encoder;

    @Test
    void secondRunKeepsExistingDataAndRefreshesThePassword() {
        assertThat(users.count()).isEqualTo(2);
        assertThat(requests.count()).isEqualTo(3);

        ReflectionTestUtils.setField(seeder, "demoPassword", "NuovaPasswordDemo2026");
        seeder.run();

        assertThat(users.count()).isEqualTo(2);
        assertThat(requests.count()).isEqualTo(3);
        assertThat(encoder.matches("NuovaPasswordDemo2026",
                users.findByUsername("demo.employee").orElseThrow().getPasswordHash())).isTrue();
        assertThat(encoder.matches("NuovaPasswordDemo2026",
                users.findByUsername("demo.manager").orElseThrow().getPasswordHash())).isTrue();
    }
}
