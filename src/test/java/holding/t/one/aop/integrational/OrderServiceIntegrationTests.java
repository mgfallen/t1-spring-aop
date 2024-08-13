package holding.t.one.aop.integrational;

import holding.t.one.aop.model.Order;
import holding.t.one.aop.model.User;
import holding.t.one.aop.repository.UserRepository;
import holding.t.one.aop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("prod")
public class OrderServiceIntegrationTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        order.setDescription("Test Order");
        order.setStatus("NEW");

        Order createdOrder = orderService.createOrder(order);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getOrderId()).isNotNull();
        assertThat(createdOrder.getDescription()).isEqualTo("Test Order");
        assertThat(createdOrder.getStatus()).isEqualTo("NEW");
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        order.setDescription("Another Order");
        order.setStatus("NEW");

        Order savedOrder = orderService.createOrder(order);
        Optional<Order> retrievedOrder = orderService.getOrderById(savedOrder.getOrderId());

        assertThat(retrievedOrder).isPresent();
        assertThat(retrievedOrder.get().getDescription()).isEqualTo("Another Order");
    }

    @Test
    public void testDeleteOrderById() {
        Order order = new Order();
        order.setDescription("Order to be deleted");
        order.setStatus("NEW");

        Order savedOrder = orderService.createOrder(order);
        orderService.deleteOrderById(savedOrder.getOrderId());

        Optional<Order> retrievedOrder = orderService.getOrderById(savedOrder.getOrderId());
        assertThat(retrievedOrder).isNotPresent();
    }

    @Test
    public void testGetAllOrders() {
        User mainUser = new User();
        mainUser.setName("John Doe");
        mainUser.setEmail("john.doe@example.com");
        userRepository.save(mainUser);

        Order order1 = new Order();
        order1.setDescription("Order 1");
        order1.setStatus("NEW");
        order1.setUser(mainUser);

        Order order2 = new Order();
        order2.setDescription("Order 2");
        order2.setStatus("NEW");
        order2.setUser(mainUser);

        orderService.createOrder(order1);
        orderService.createOrder(order2);

        List<Order> orders = orderService.getAllOrders();
        assertThat(orders).hasSize(2);
    }

    @Test
    public void testUpdateOrderById() {
        Order order = new Order();
        order.setDescription("Order to be updated");
        order.setStatus("NEW");


        Order savedOrder = orderService.createOrder(order);
        savedOrder.setDescription("Updated Order");
        savedOrder.setStatus("UPDATED");

        Order updatedOrder = orderService.updateOrderById(savedOrder.getOrderId(), savedOrder);

        assertThat(updatedOrder.getDescription()).isEqualTo("Updated Order");
        assertThat(updatedOrder.getStatus()).isEqualTo("UPDATED");
    }
}
