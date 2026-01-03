package com.app.booking;

import com.app.booking.dto.request.LoginRequest;
import com.app.booking.dto.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class BookingSystemApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
    

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(kafka.isCreated()).isTrue();
    }

    @Test
    void testRegisterAndLogin() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Integration User");
        registerRequest.setEmail("integration@test.com");
        registerRequest.setPhone("1234567890");
        registerRequest.setPassword("password");

        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/auth/register", registerRequest, String.class);
        assertThat(registerResponse.getStatusCode().is2xxSuccessful()).isTrue();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("integration@test.com");
        loginRequest.setPassword("password");

        ResponseEntity<Object> loginResponse = restTemplate.postForEntity("/auth/login", loginRequest, Object.class);
        assertThat(loginResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
