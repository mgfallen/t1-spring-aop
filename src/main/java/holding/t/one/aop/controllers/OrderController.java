package holding.t.one.aop.controllers;

import holding.t.one.aop.model.Order;
import holding.t.one.aop.model.User;
import holding.t.one.aop.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder) {
        Order order = orderService.createOrder(newOrder);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable UUID id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrderById(@PathVariable UUID id, @RequestBody Order orderDetails) {
        Order updatedOrder = orderService.updateOrderById(id, orderDetails);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllUsers() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.of(Optional.ofNullable(orders));
    }
}
