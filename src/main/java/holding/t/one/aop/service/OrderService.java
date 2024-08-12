package holding.t.one.aop.service;

import holding.t.one.aop.exceptions.OrderDomainException;
import holding.t.one.aop.model.Order;
import holding.t.one.aop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order newOrder) {
        return orderRepository.save(newOrder);
    }

    public Optional<Order> getOrderById(UUID uuid) {
        return orderRepository.findById(uuid);
    }

    public void deleteOrderById(UUID uuid) {
        orderRepository.deleteById(uuid);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public Order updateOrderById(UUID uuid, Order orderDetails) {
        Order order = orderRepository.findById(uuid).orElseThrow(() -> new OrderDomainException("Order not found"));
        order.setDescription(orderDetails.getDescription());
        order.setStatus(orderDetails.getStatus());
        return orderRepository.save(order);
    }
}
