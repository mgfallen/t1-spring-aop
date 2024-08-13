package holding.t.one.aop.integrational;

import holding.t.one.aop.exceptions.UserDomainException;
import holding.t.one.aop.model.Order;
import holding.t.one.aop.model.User;
import holding.t.one.aop.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest()
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("prod")
public class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        postgresContainer.start();
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        System.out.println("PostgreSQL URL: " + postgresContainer.getJdbcUrl());
        System.out.println("PostgreSQL Username: " + postgresContainer.getUsername());
        System.out.println("PostgreSQL Password: " + postgresContainer.getPassword());
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        User createdUser = userService.createUser(user);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUserId()).isNotNull();
        assertThat(createdUser.getName()).isEqualTo("John Doe");
        assertThat(createdUser.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");

        User savedUser = userService.createUser(user);
        Optional<User> retrievedUser = userService.getUserById(savedUser.getUserId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    public void testRemoveUserById() {
        User user = new User();
        user.setName("Mark Smith");
        user.setEmail("mark.smith@example.com");

        User savedUser = userService.createUser(user);
        userService.removeUserById(savedUser.getUserId());

        Optional<User> retrievedUser = userService.getUserById(savedUser.getUserId());
        assertThat(retrievedUser).isNotPresent();
    }

    @Test
    public void testUpdateUserById() {
        User user = new User();
        user.setName("Peter Parker");
        user.setEmail("peter.parker@example.com");

        User savedUser = userService.createUser(user);
        savedUser.setName("Updated Name");
        savedUser.setEmail("updated.email@example.com");

        User updatedUser = userService.updateUserById(savedUser.getUserId(), savedUser);

        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated.email@example.com");
    }

    @Test
    public void testUpdateUserByIdThrowsException() {
        UUID nonExistentId = UUID.randomUUID();
        User userDetails = new User();
        userDetails.setName("Non-existent user");
        userDetails.setEmail("non.existent@example.com");

        assertThatThrownBy(() -> userService.updateUserById(nonExistentId, userDetails))
                .isInstanceOf(UserDomainException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testAddOrderToUser() {
        User user = new User();
        user.setName("Bruce Wayne");
        user.setEmail("bruce.wayne@example.com");

        User savedUser = userService.createUser(user);

        Order order = new Order();
        order.setDescription("Batmobile");
        order.setStatus("NEW");

        User updatedUser = userService.addOrderToUser(savedUser.getUserId(), order);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getOrders()).hasSize(1);
        assertThat(updatedUser.getOrders().getFirst().getDescription()).isEqualTo("Batmobile");
    }
}
