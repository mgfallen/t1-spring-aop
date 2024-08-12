package holding.t.one.aop.unit;

import holding.t.one.aop.exceptions.OrderDomainException;
import holding.t.one.aop.model.Order;
import holding.t.one.aop.repository.OrderRepository;
import holding.t.one.aop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        order.setDescription("Test Order");

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);
        assertNotNull(createdOrder);
        assertEquals("Test Order", createdOrder.getDescription());
    }

    @Test
    public void testFindOrderById() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.getOrderById(orderId);
        assertTrue(foundOrder.isPresent());
        assertEquals(orderId, foundOrder.get().getOrderId());
    }

    @Test
    public void testDeleteOrderById() {
        UUID orderId = UUID.randomUUID();
        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrderById(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testUpdateOrderById_withUpdatingDescriptionAndStatus() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setDescription("Original Description");
        order.setStatus("Pending");

        Order updatedOrderDetails = new Order();
        updatedOrderDetails.setDescription("Updated Description");
        updatedOrderDetails.setStatus("Completed");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updateOrderById(orderId, updatedOrderDetails);

        assertNotNull(updatedOrder);
        assertEquals("Updated Description", updatedOrder.getDescription());
        assertEquals("Completed", updatedOrder.getStatus());
    }

    @Test
    public void testUpdateOrderById_OrderNotFound() {
        UUID orderId = UUID.randomUUID();
        Order updatedOrderDetails = new Order();
        updatedOrderDetails.setDescription("Updated Description");
        updatedOrderDetails.setStatus("Completed");

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderDomainException.class, () -> {
            orderService.updateOrderById(orderId, updatedOrderDetails);
        });
    }
}
